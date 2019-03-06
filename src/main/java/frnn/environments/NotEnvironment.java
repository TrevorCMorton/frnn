package frnn.environments;

import frnn.organisms.IBooleanOrganism;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Random;

public class NotEnvironment extends AbstractEnvironment<IBooleanOrganism> {
    private int iterations;

    public NotEnvironment(int iterations){
        this.iterations = iterations;
    }

    @Override
    protected Runnable getSimulation(IBooleanOrganism organism) {
        return new Runnable() {
            @Override
            public void run() {
                double score = 0;

                for(int i = 0; i < iterations; i++){
                    double prob = Math.random();

                    boolean input = prob < .5;
                    Boolean result = organism.interpret(input);

                    if(result == null){
                        score -= 1;
                        continue;
                    }

                    boolean correct = input != result;

                    if(correct){
                        score += 1;
                    }
                    else{
                        score += -1;
                    }
                }

                double networkPerformance = score;

                synchronized (performance) {
                    performance.put(organism, networkPerformance);
                }
            }
        };
    }
}
