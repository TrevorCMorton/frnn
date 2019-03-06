package frnn.environments;

import frnn.organisms.IOrganism;

import java.util.HashMap;

public interface IEnvironment<T extends IOrganism> extends Runnable{
    HashMap<T, Double> getPerformance();
    void addOrganism(T organism);
    void releasePopulation();
}
