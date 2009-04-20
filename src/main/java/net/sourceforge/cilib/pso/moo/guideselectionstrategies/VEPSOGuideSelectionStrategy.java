/**
 * Copyright (C) 2003 - 2008
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.pso.moo.guideselectionstrategies;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationCriterionBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.knowledgetransferstrategies.KnowledgeTransferStrategy;
import net.sourceforge.cilib.algorithm.population.knowledgetransferstrategies.SelectiveKnowledgeTransferStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.moo.criterion.CriterionBasedMOProblemAdapter;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Vector-Evaluated Particle Swarm Optimisation Guide Selection Strategy
 *
 * <p>
 * This {@link GuideSelectionStrategy} implements the basic behaviour of VEPSO where each
 * particle's global guide is selected as the position of a particle within another swarm (see
 * {@link MultiPopulationCriterionBasedAlgorithm}). Each swarm is evaluated according to a
 * different sub-objective (see {@link CriterionBasedMOProblemAdapter}) of a Multi-objective
 * optimisation problem. A {@link KnowledgeTransferStrategy} is used to determine which swarm
 * is selected (either random or ring-based) as well as which particle's position within this
 * swarm will be used as guide (usually the gBest particle).
 * </p>
 *
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li> K. E. Parsopoulos, D. K. Tasoulis and M. N. Vrahatis, "Multiobjective Optimization using
 * Parallel Vector Evaluated Particle Swarm Optimization", in Proceedings of the IASTED International
 * Conference on Artificial Intelligence and Applications, vol 2, pp. 823-828, 2004.
 * </li>
 * </ul>
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class VEPSOGuideSelectionStrategy implements GuideSelectionStrategy {

    private static final long serialVersionUID = -8916378051119235043L;
    private KnowledgeTransferStrategy knowledgeTransferStrategy;

    public VEPSOGuideSelectionStrategy() {
        this.knowledgeTransferStrategy = new SelectiveKnowledgeTransferStrategy();
    }

    public VEPSOGuideSelectionStrategy(VEPSOGuideSelectionStrategy copy) {
        this.knowledgeTransferStrategy = copy.knowledgeTransferStrategy.getClone();
    }

    @Override
    public VEPSOGuideSelectionStrategy getClone() {
        return new VEPSOGuideSelectionStrategy(this);
    }

    public void setKnowledgeTransferStrategy(KnowledgeTransferStrategy knowledgeTransferStrategy) {
        this.knowledgeTransferStrategy = knowledgeTransferStrategy;
    }

    public KnowledgeTransferStrategy getKnowledgeTransferStrategy() {
        return this.knowledgeTransferStrategy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vector selectGuide(Particle particle) {
        MultiPopulationBasedAlgorithm topLevelAlgorithm = (MultiPopulationBasedAlgorithm) Algorithm.getAlgorithmList().get(0);
        Blackboard<Enum<?>, Type> knowledge = (Blackboard<Enum<?>, Type>) this.knowledgeTransferStrategy.transferKnowledge(topLevelAlgorithm.getPopulations());
        return (Vector) knowledge.get(EntityType.Particle.BEST_POSITION);
    }
}
