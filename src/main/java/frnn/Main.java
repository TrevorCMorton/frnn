package frnn;

import frnn.environments.IEnvironment;
import frnn.environments.NotEnvironment;
import frnn.organisms.Frnn;
import frnn.organisms.FrnnBooleanOrganism;
import frnn.organisms.IOrganism;
import frnn.runners.BreedingRunner;
import frnn.runners.IRunner;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.List;

import static org.nd4j.linalg.ops.transforms.Transforms.*;

public class Main {
    public static void main(String[] args){
        List<IOrganism> initialPop = new ArrayList<>();

        int initialPopSize = 100;
        int numNodes = 4;
        double initialArcDensity = .10;
        double connectionCrossover = .5;
        double connectionMutation = .10;

        for(int i = 0; i < initialPopSize; i++){
            INDArray connectionMatrix = lessThanOrEqual(Nd4j.rand(numNodes, numNodes), Nd4j.ones(numNodes, numNodes).mul(initialArcDensity));
            INDArray fireVector = Nd4j.ones(numNodes).transpose();
            FrnnBooleanOrganism initialNetwork = new FrnnBooleanOrganism(connectionMatrix, fireVector, 10);
            initialPop.add(initialNetwork);
        }

        IEnvironment env = new NotEnvironment(50);
        IRunner runner = new BreedingRunner(env, .2, 100, 100, connectionCrossover, connectionMutation, initialPop);
        IOrganism best = runner.simulate();

        best.print();
    }
}
