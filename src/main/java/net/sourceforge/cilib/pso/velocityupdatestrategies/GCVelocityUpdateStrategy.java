package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.Random;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/** 
 * <p>
 * An implementation of the Guaranteed Convergence PSO algorithm. The GCPSO is a simple extension
 * to the normal PSO algorithm and the modifications to the algorithm is implemented as
 * a simple {@link VelocityUpdateStrategy}
 * </p><p>
 * References:
 * </p><p><ul><li>
 * F. van den Bergh and A. Engelbrecht, "A new locally convergent particle swarm optimizer,"
 * in Proceedings of IEEE Conference on Systems, Man and Cybernetics,
 * (Hammamet, Tunisia), Oct. 2002.
 * </li><li>
 * F. van den Bergh, "An Analysis of Particle Swarm Optimizers,"
 * PhD thesis, Department of Computer Science, 
 * University of Pretoria, South Africa, 2002.
 * </li></ul></p>
 * 
 * @TODO: The Rho value should be a vector to hold the rho value for each dimension!
 * 
 * It is very important to realise the importance of the <code>rho</code> values. <code>rho</code>
 * determines the local search size of the global best particle and depending on the domain
 * this could result in poor performance if the <code>rho</code> value is too small or too large depending
 * on the specified problem domain. For example, a <code>rho</code> value of 1.0 is not a good
 * value within problems which have a domain that spans <code>[0,1]</code> 
 */
public class GCVelocityUpdateStrategy extends StandardVelocityUpdate {
	private static final long serialVersionUID = 5985694749940610522L;
	private Random randomNumberGenerator;
	private ControlParameter rhoLowerBound;
	private ControlParameter rho;
	private int successCount;
	private int failureCount;
	private int successCountThreshold;
	private int failureCountThreshold;
	
	private Fitness oldFitness;
	private ControlParameter rhoExpandCoefficient;
	private ControlParameter rhoContractCoefficient;

	public GCVelocityUpdateStrategy() {
		super();
		randomNumberGenerator = new MersenneTwister();
		oldFitness = InferiorFitness.instance();
		
		rho = new ConstantControlParameter(1.0);
		rhoLowerBound = new ConstantControlParameter(1.0e-323);
		successCount = 0;
		failureCount = 0;
		successCountThreshold = 15;
		failureCountThreshold = 5;
		rhoExpandCoefficient = new ConstantControlParameter(1.2);
		rhoContractCoefficient = new ConstantControlParameter(0.5);
		
		vMax = new ConstantControlParameter(0.5); // This needs to be set dynamically - this is not valid for all problems
	}
	
	public GCVelocityUpdateStrategy(GCVelocityUpdateStrategy copy) {
		super(copy);
		this.randomNumberGenerator = copy.randomNumberGenerator;
		this.oldFitness = copy.oldFitness.getClone();
		
		this.rho = copy.rho.getClone();
		this.rhoLowerBound = copy.rhoLowerBound.getClone();
		this.successCount = copy.successCount;
		this.failureCount = copy.failureCount;
		this.successCountThreshold = copy.successCountThreshold;
		this.failureCountThreshold = copy.failureCountThreshold;
		this.rhoExpandCoefficient = copy.rhoExpandCoefficient;
		this.rhoContractCoefficient = copy.rhoContractCoefficient;
		
		this.vMax = copy.vMax.getClone();
	}

	@Override
	public GCVelocityUpdateStrategy getClone() {
		return new GCVelocityUpdateStrategy(this);
	}

	public void updateVelocity(Particle particle) {
		PSO pso = (PSO) Algorithm.get();
		final Particle globalBest = pso.getBestParticle();
		
		if (particle == globalBest) {
			final Vector velocity = (Vector) particle.getVelocity();
			final Vector position = (Vector) particle.getPosition();
//			final Vector pBestPosition = (Vector) particle.getBestPosition();
			final Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getPosition();
//			final Vector gbestPosition = (Vector) globalBest.getPosition();
			
			for (int i = 0; i < velocity.getDimension(); ++i) {
				double component = -position.getReal(i) + nBestPosition.getReal(i) 
					+ this.inertiaWeight.getParameter()*velocity.getReal(i)
					+ rho.getParameter()*(1-2*randomNumberGenerator.nextDouble());
				
				velocity.setReal(i, component);
				clamp(velocity, i);
			}
			
			oldFitness = particle.getFitness().getClone(); // Keep a copy of the old Fitness object - particle.calculateFitness() within the IterationStrategy resets the fitness value
			return;
		}

		super.updateVelocity(particle);
	}
	
	public void updateControlParameters(Particle particle) {
		// Remember NOT to reset the rho value to 1.0
		PSO pso = (PSO) Algorithm.get();
		
		if (particle == pso.getBestParticle()) {
			Fitness newFitness = pso.getOptimisationProblem().getFitness(particle.getPosition(), false);
			
			if (!newFitness.equals(oldFitness)) {
				this.failureCount = 0;
				this.successCount++;
			}
			else {
				this.successCount = 0;
				this.failureCount++;
			}
			
			updateRho((Vector) particle.getPosition());
			return;
		}
		
		failureCount = 0;
		successCount = 0;
		super.updateControlParameters(particle);
	}
	
	/**
	 * Update the <code>rho</code> value.
	 * @param position
	 */
	private void updateRho(Vector position) { // the Rho value is problem and dimension dependent
		double tmp = 0.0;
		
		Numeric component = (Numeric) position.get(0);
		double average = (component.getUpperBound() - component.getLowerBound()) / rhoExpandCoefficient.getParameter();
	
		if (successCount >= successCountThreshold) tmp = rhoExpandCoefficient.getParameter()*rho.getParameter();
		if (failureCount >= failureCountThreshold) tmp = rhoContractCoefficient.getParameter()*rho.getParameter();
		
		if (tmp <= rhoLowerBound.getParameter()) tmp = rhoLowerBound.getParameter();
		if (tmp >= average) tmp = average;
		
		rho.setParameter(tmp);
	}

	public ControlParameter getRhoLowerBound() {
		return rhoLowerBound;
	}

	public void setRhoLowerBound(ControlParameter rhoLowerBound) {
		this.rhoLowerBound = rhoLowerBound;
	}

	public ControlParameter getRho() {
		return rho;
	}

	public void setRho(ControlParameter rho) {
		this.rho = rho;
	}

	public int getSuccessCountThreshold() {
		return successCountThreshold;
	}

	public void setSuccessCountThreshold(int successCountThreshold) {
		this.successCountThreshold = successCountThreshold;
	}

	public int getFailureCountThreshold() {
		return failureCountThreshold;
	}

	public void setFailureCountThreshold(int failureCountThreshold) {
		this.failureCountThreshold = failureCountThreshold;
	}

	public ControlParameter getRhoExpandCoefficient() {
		return rhoExpandCoefficient;
	}

	public void setRhoExpandCoefficient(ControlParameter rhoExpandCoefficient) {
		this.rhoExpandCoefficient = rhoExpandCoefficient;
	}

	public ControlParameter getRhoContractCoefficient() {
		return rhoContractCoefficient;
	}

	public void setRhoContractCoefficient(ControlParameter rhoContractCoefficient) {
		this.rhoContractCoefficient = rhoContractCoefficient;
	}
	
}
