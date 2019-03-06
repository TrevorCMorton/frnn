package frnn.runners;

import frnn.organisms.Frnn;
import frnn.environments.IEnvironment;
import frnn.organisms.IOrganism;
import javafx.util.Pair;

import java.util.*;

public class BreedingRunner implements IRunner {
    private IEnvironment env;
    private double dieOffRate;
    private int maxPopulation;
    private int generations;

    private double crossoverRate;
    private double mutationRate;

    private List<IOrganism> population;

    public BreedingRunner(IEnvironment env, double dieOffRate, int maxPopulation, int generations , double crossoverRate, double mutationRate, List<IOrganism> initialPop){
        this.env = env;
        this.dieOffRate = dieOffRate;
        this.maxPopulation = maxPopulation;
        this.generations = generations;

        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;

        this.population = initialPop;
    }

    public IOrganism simulate(){
        IOrganism best = null;
        double bestScore = 0;

        //replace with env call
        double goalScore = 50.0;

        for(int i = 0; i < this.generations; i++){
            for(IOrganism organism : this.population) {
                env.addOrganism(organism);
            }

            System.out.println("Releasing networks");
            env.releasePopulation();

            System.out.println("Generation " + i + " done");

            //For now, we use rudimentary fitness measurement that only decides whether an organism survives or dies
            HashMap<IOrganism, Double> performances = env.getPerformance();

            PriorityQueue<Pair<Double, IOrganism>> networkPerformances = new PriorityQueue<>(new Comparator<Pair<Double, IOrganism>>() {
                @Override
                public int compare(Pair<Double, IOrganism> t0, Pair<Double, IOrganism> t1) {
                    return (int)( t1.getKey() - t0.getKey());
                }
            });

            for(IOrganism organism : this.population){
                Double perfVals = performances.get(organism);
                double fitness = perfVals;
                networkPerformances.add(new Pair<>(fitness, organism));
            }

            List<Pair<Double, IOrganism>> survivors = new ArrayList<>();

            int survivorCount = (int)Math.min(this.maxPopulation * this.dieOffRate, networkPerformances.size());
            survivorCount = survivorCount - (survivorCount % 2);

            for(int j = 0; j < survivorCount; j++){
                Pair<Double, IOrganism> survivor = networkPerformances.poll();

                System.out.println("Surviving organism with score " + survivor.getKey());
                if(survivor.getKey() > bestScore){
                    best = survivor.getValue();
                    bestScore = survivor.getKey();
                }

                survivors.add(survivor);
            }

            if(bestScore >= goalScore){
                return best;
            }

            System.out.println("Breeeding networks");

            this.population.clear();
            for(int j = 0; j < survivors.size(); j++){
                int numChildren = (int)(1.0 / this.dieOffRate);
                for(int k = 0; k < numChildren; k++){
                    int randInd = (int)(Math.random() * survivors.size());
                    IOrganism child = survivors.get(j).getValue().breed(survivors.get(randInd).getValue(), this.crossoverRate, this.mutationRate);
                    this.population.add(child);
                }
            }
        }

        return best;
    }
}
