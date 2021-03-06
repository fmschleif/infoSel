/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rapidminer.ispr.operator.learner.selection.models;

import com.rapidminer.example.set.SelectedExampleSet;
import com.rapidminer.ispr.operator.learner.selection.models.decisionfunctions.IISDecisionFunction;
import com.rapidminer.ispr.operator.learner.tools.DataIndex;
import com.rapidminer.tools.math.similarity.DistanceMeasure;

/**
 * Class implements RNG (Relative Neighbourhood Graph) instance selection
 * algorithm. It is similar to Gabriel Editing, but use simplified criteria
 * function which measures just relation d13 < max(d12,d23) @author Ma
 *
 * rcin
 */
public class RNGInstanceSelectionModel extends AbstractInstanceSelectorModel implements EditedDistanceGraphCriteria {

    private AbstractInstanceSelectorModel model;

    /**
     * Constructor for class which implements RNG (Relative Neighbourhood Graph)
     * instance selection algorithm. It is similar to Gabriel Editing, but use
     * simplified criteria function which measures just relation d13 <
     * max(d12,d23) @param
     *
     * distance
     */
    public RNGInstanceSelectionModel(DistanceMeasure distance, IISDecisionFunction loss) {
        model = new EditedDistanceGraphModel(distance, this, loss);
    }

    /**
     * Performs instance selection
     *
     * @param inputExampleSet - example set for which instance selection will be
     * performed
     * @return - index of selected examples
     */
    @Override
    public DataIndex selectInstances(SelectedExampleSet inputExampleSet) {
        return model.selectInstances(inputExampleSet);

    }

    /**
     * Criteria function
     * @param a - distance between samples 1 and 3
     * @param b - distance between samples 1 and 2
     * @param c - distance between samples 2 and 3
     * @return 
     */
    @Override
    public boolean evaluate(double a, double b, double c) {
        return a > Math.max(b, c);
    }
}
