package frnn.organisms;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;

import static org.nd4j.linalg.ops.transforms.Transforms.*;

public class Frnn {
    INDArray connectionMatrix;
    INDArray firePoints;
    INDArray arcOutCounts;
    INDArray levels;

    int timeout;

    public Frnn(INDArray connectionMatrix, INDArray firePoints, int timeout){
        this.connectionMatrix = connectionMatrix;
        this.firePoints = firePoints;
        this.arcOutCounts = max(this.connectionMatrix.mmul(Nd4j.ones(this.firePoints.shape())), Nd4j.ones(this.firePoints.shape()));
        this.levels = Nd4j.zeros(this.firePoints.shape());
        this.timeout = timeout;
    }

    public INDArray getBlankVector(){
        return Nd4j.zeros(this.firePoints.shape());
    }

    public void takeInput(INDArray input){
        this.levels = this.levels.add(input);
    }

    public INDArray getOutput(INDArray outputMask){
        INDArray output = this.levels.mul(outputMask);
        this.levels.sub(output);
        return output;
    }

    public void iterate(){
        INDArray firing = greaterThanOrEqual(this.levels, this.firePoints);
        INDArray toDistribute = (this.levels.mul(firing)).div(arcOutCounts);
        INDArray distributedLevels = this.connectionMatrix.mmul(toDistribute);
        INDArray updatedLevels = this.levels.mul(not(firing));
        this.levels = updatedLevels.add(distributedLevels);
    }

    public void print(){
        double[][] connections = this.connectionMatrix.toDoubleMatrix();

        System.out.println("Connections");
        for(int i = 0; i < connections.length; i++){
            System.out.println(Arrays.toString(connections[i]));
        }

        System.out.println("Firing Points");
        System.out.println(Arrays.toString(this.firePoints.toDoubleVector()));

    }

    public int getTimeout(){
        return this.timeout;
    }
}
