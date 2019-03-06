package frnn.organisms;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static org.nd4j.linalg.ops.transforms.Transforms.greaterThanOrEqual;
import static org.nd4j.linalg.ops.transforms.Transforms.lessThanOrEqual;
import static org.nd4j.linalg.ops.transforms.Transforms.not;

public class FrnnOrganism extends Frnn implements IOrganism {

    FrnnOrganism(INDArray connections, INDArray fires, int timeout){
        super(connections, fires, timeout);
    }

    public IOrganism breed(IOrganism organism, double crossoverRate, double mutationRate){
        FrnnOrganism mate = (FrnnOrganism) organism;
        INDArray randConnectionCrossover = lessThanOrEqual(Nd4j.rand(this.connectionMatrix), Nd4j.ones(this.connectionMatrix.shape()).mul(crossoverRate));
        INDArray randConnectionMutation = greaterThanOrEqual(Nd4j.rand(this.connectionMatrix), Nd4j.ones(this.connectionMatrix.shape()).mul(mutationRate));

        INDArray shared = this.connectionMatrix.eq(mate.connectionMatrix);
        INDArray crossover = not(shared);
        crossover = crossover.mul(randConnectionCrossover);

        INDArray evolvedConnections = shared.add(crossover).mul(randConnectionMutation);

        /*
        INDArray fireCrossover = greaterThanOrEqual(Nd4j.rand(this.firePoints.shape()), Nd4j.ones(this.firePoints.shape()).mul(.5));
        INDArray crossedFires = this.firePoints.mul(fireCrossover);
        crossedFires.add(mate.firePoints.mul(not(fireCrossover)));

        INDArray fireMutationPos = greaterThanOrEqual(Nd4j.rand(this.firePoints.shape()), Nd4j.ones(this.firePoints.shape()).mul(fireMutation / 2));
        INDArray fireMutationNeg = greaterThanOrEqual(Nd4j.rand(this.firePoints.shape()), Nd4j.ones(this.firePoints.shape()).mul(fireMutation / 2));
        fireMutationNeg = fireMutationNeg.mul(greaterThanOrEqual(crossedFires, Nd4j.ones(this.firePoints.shape()).mul(2)));

        INDArray evolvedFires = crossedFires.add(fireMutationPos).add(fireMutationNeg.mul(-1));

        */
        try {
            return (IOrganism) organism.getClass().getConstructors()[0].newInstance(evolvedConnections, this.firePoints, this.timeout);
        }
        catch(Exception e){
            return null;
        }
    }
}
