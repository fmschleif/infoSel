/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rapidminer.ispr.operator.learner.selection.models.decisionfunctions;

import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.ispr.operator.learner.tools.BasicMath;
import com.rapidminer.ispr.operator.learner.tools.KNNTools;
import com.rapidminer.ispr.tools.math.container.GeometricCollectionTypes;
import com.rapidminer.ispr.tools.math.container.ISPRGeometricDataCollection;
import com.rapidminer.operator.OperatorCapability;
import com.rapidminer.tools.math.similarity.DistanceMeasure;
import java.util.Collection;

/**
 * ISLocalThresholdLinearDecisionFunction is an implementation of IISThresholdDecisionFunction. It represents
 * decision function which calculates the difference between real (R) and predicted (P) value of given instance (R-P) 
 * then checks if the error is greater then the standard deviation of k nearest
 * output values multiply be the threshold. If so returns 1 
 * @author Marcin
 */
public class ISLocalThresholdLinearDecisionFunction implements IISThresholdDecisionFunction, IISLocalDecisionFunction {
    
    private double threshold = 0;
    private int k = 3;
    private ISPRGeometricDataCollection<Number> samples;
    private boolean blockInit = false;    


    public ISLocalThresholdLinearDecisionFunction() {
    }
    
    @Override
    public void setBlockInit(boolean block) {        
        blockInit = block;
    }

    @Override
    public boolean isBlockInit() {        
        return blockInit;
    }
    
    @Override
    public void init(ExampleSet exampleSet, DistanceMeasure distance) {
        if (!blockInit)
            samples = KNNTools.initializeKNearestNeighbourFactory(GeometricCollectionTypes.LINEAR_SEARCH, exampleSet, distance);
    }

    @Override
    public void init(ISPRGeometricDataCollection<Number> samples){        
        if (!blockInit)
            this.samples = samples;
    }
            
    @Override
    public double getValue(double real, double predicted, double[] values) {
        Collection<Number> nn = samples.getNearestValues(k, values);
        double std = BasicMath.mean(nn);
        double value = Math.abs(real - predicted) / std > threshold ? 1 : 0;
        return value;
    }
    
    @Override
    public double getValue(double real, double predicted, Example example){
        double[] values = new double[example.getAttributes().size()];
        KNNTools.extractExampleValues(example, values);
        return getValue(real, predicted, values);
    }
        
    @Override
    public void setThreshold(double threshold){
        this.threshold = threshold;
    }

    @Override
    public String name() {
        return "Local Threshold Linear Loss";
    }

    @Override
    public String description() {
        return "Y=(R-P) > noise(k) * Thres";
    }
    
    @Override
    public boolean supportedLabelTypes(OperatorCapability capabilities){
        switch (capabilities){
            case NUMERICAL_LABEL:
                return true;
        }        
        return false;
    }
    
    @Override
    public void setK(int k) {
        this.k = k;
    }

    @Override
    public int getK() {
        return k;
    }
}

