package frnn.organisms;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class FrnnBooleanOrganism extends FrnnOrganism implements IBooleanOrganism {

    INDArray falseIn;
    INDArray trueIn;
    INDArray falseOut;
    INDArray trueOut;

    public FrnnBooleanOrganism(INDArray connections, INDArray fires, int timeout){
        super(connections, fires, timeout);

        float[] blankVector = this.getBlankVector().toFloatVector();

        blankVector[0] = 1;
        falseIn = Nd4j.create(blankVector).transpose();
        blankVector[0] = 0;

        blankVector[2] = 1;
        falseOut = Nd4j.create(blankVector).transpose();
        blankVector[2] = 0;

        blankVector[1] = 1;
        trueIn = Nd4j.create(blankVector).transpose();
        blankVector[1] = 0;

        blankVector[3] = 1;
        trueOut = Nd4j.create(blankVector).transpose();
        blankVector[3] = 0;
    }

    @Override
    public Boolean interpret(boolean input) {

        INDArray networkInput;

        if (input) {
            networkInput = trueIn;
        }
        else {
            networkInput = falseIn;
        }

        int iters = 0;

        while(iters < this.getTimeout()) {
            this.takeInput(networkInput);

            this.iterate();

            boolean resFalse = this.getOutput(falseOut).sumNumber().doubleValue() > 0;
            boolean resTrue = this.getOutput(trueOut).sumNumber().doubleValue() > 0;

            if(resFalse ^ resTrue){
                if(resFalse){
                    return false;
                }
                return true;
            }

            iters++;
        }

        return null;
    }
}
