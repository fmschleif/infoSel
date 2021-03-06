package com.rapidminer.ispr.operator.learner;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ValueDouble;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.DistanceMeasurePrecondition;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.MDInteger;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.PassThroughRule;
import com.rapidminer.parameter.UndefinedParameterError;

/**
 * This class is used as a base for any instance selection and optimization
 * operator. It delivers the prototype output port and defines the basic
 * metadate requirements.
 *
 * @author Marcin
 */
public abstract class AbstractPRulesOperator extends AbstractPRulesBasicOperator {

    /**
     * Output port which is used to return selected prototypes
     */
    protected final OutputPort prototypesOutputPort = getOutputPorts().createPort("prototypes");
    protected double numberOfInstancesBeaforeSelection = -1;
    protected double numberOfInstancesAfterSelection = -1;
    protected double compression = -1;

    /**
     * Constructor of the AbstractPRulesOperator class.
     *
     * @param description
     */
    public AbstractPRulesOperator(OperatorDescription description) {
        super(description);//
        exampleSetInputPort.addPrecondition(new DistanceMeasurePrecondition(exampleSetInputPort, this));
        getTransformer().addRule(new PassThroughRule(exampleSetInputPort, prototypesOutputPort, true) {

            @Override
            public MetaData modifyMetaData(MetaData metaData) {
                if (metaData instanceof ExampleSetMetaData) {
                    try {
                        ExampleSetMetaData exampleSetMetaData = (ExampleSetMetaData) metaData;
                        ExampleSetMetaData exampleSetMetaDataFinal = AbstractPRulesOperator.this.modifyPrototypeOutputMetaData(exampleSetMetaData);
                        return exampleSetMetaDataFinal;
                    } catch (UndefinedParameterError ex) {
                        return metaData;
                    }
                } else {
                    return metaData;
                }
            }
        });
        addValue(new ValueDouble("Instances_beafore_selection", "Number Of Examples in the training set") {

            @Override
            public double getDoubleValue() {
                return numberOfInstancesBeaforeSelection;
            }
        });
        addValue(new ValueDouble("Instances_after_selection", "Number Of Examples after selection") {

            @Override
            public double getDoubleValue() {
                return numberOfInstancesAfterSelection;
            }
        });
        addValue(new ValueDouble("Compression", "Compressin = #Instances_after_selection/#Instances_beafore_selection") {

            @Override
            public double getDoubleValue() {
                return compression;
            }
        });
    }

    /**
     * It overloads the executeOperator, and executes the processExamples
     * method.
     *
     * @param trainingSet - training set
     * @throws OperatorException
     */
    @Override
    public void executeOperator(ExampleSet trainingSet) throws OperatorException {
        numberOfInstancesBeaforeSelection = trainingSet.size();
        ExampleSet outputSet = processExamples(trainingSet);
        prototypesOutputPort.deliver(outputSet);
        numberOfInstancesAfterSelection = outputSet.size();
        compression = numberOfInstancesAfterSelection / numberOfInstancesBeaforeSelection;
    }

    /**
     * Method which implements the prototype selection process.
     *
     * @param trainingSet - training set
     * @return
     * @throws OperatorException
     */
    public abstract ExampleSet processExamples(ExampleSet trainingSet) throws OperatorException;

    /**
     * It returns number of proptotypes in the ExampleSetMetaData returned by the prototypeOutput
     *
     * @return
     * @throws UndefinedParameterError
     */
    public abstract MDInteger getNumberOfPrototypesMetaData() throws UndefinedParameterError;
    /**
     * Used to define the metadata properties of prototypeOutput
     *
     * @param prototypeOutputMetaData
     * @return
     * @throws UndefinedParameterError
     */
    protected ExampleSetMetaData modifyPrototypeOutputMetaData(ExampleSetMetaData prototypeOutputMetaData)
            throws UndefinedParameterError {   
         try { 
            prototypeOutputMetaData.setNumberOfExamples(getNumberOfPrototypesMetaData());
        } catch (UndefinedParameterError e){
            prototypeOutputMetaData.setNumberOfExamples(new MDInteger());
        }                
        return prototypeOutputMetaData;
    }
}
