package frnn.environments;

import frnn.organisms.IOrganism;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public abstract class AbstractEnvironment<T extends IOrganism> implements IEnvironment<T>{
    private Queue<T> organisms;
    private Queue<T> looseOrganisms;
    private int threads;
    HashMap<T, Double> performance;

    AbstractEnvironment(){
        this.organisms = new LinkedList<>();
        this.looseOrganisms = new LinkedList<>();
        this.performance = new HashMap<>();
        this.threads = Runtime.getRuntime().availableProcessors();
    }

    protected abstract Runnable getSimulation(T organism);

    @Override
    public void run() {
        int count = 0;
        while (!this.organisms.isEmpty()) {

            T organism = this.organisms.poll();
            synchronized (this.looseOrganisms) {
                this.looseOrganisms.add(organism);
            }

            while(this.looseOrganisms.size() >= this.threads){
                try {
                    Thread.sleep(10);
                }
                catch (Exception e){
                    System.out.println("Sleep failed");
                }
            }

            System.out.println("Starting simulation " + count);

            Runnable simulation = this.getSimulation(organism);

            Thread t = new Thread(simulation);
            t.start();

            count++;
        }
    }

    @Override
    public HashMap<T, Double> getPerformance() {
        return this.performance;
    }

    @Override
    public void addOrganism(T organism) {
        this.organisms.add(organism);
    }

    @Override
    public void releasePopulation() {
        this.performance.clear();

        Thread t = new Thread(this);
        t.start();

        int count = 0;
        boolean cleared = false;

        boolean empty = true;
        while(empty){
            synchronized (this.looseOrganisms){
                empty = this.looseOrganisms.isEmpty();
            }
        }

        System.out.println("The organisms are loose");

        while(!cleared) {

            T organism;
            synchronized (this.looseOrganisms){
                organism = this.looseOrganisms.poll();
            }

            boolean finished;
            synchronized (this.performance){
                finished = this.performance.containsKey(organism);
            }

            synchronized (this.looseOrganisms){
                if(!finished){
                    if(organism != null) {
                        this.looseOrganisms.add(organism);
                    }
                }
                else{
                    count++;
                    System.out.println(count + " Simulations Completed");
                }
                cleared = this.looseOrganisms.isEmpty();
            }
        }
    }
}
