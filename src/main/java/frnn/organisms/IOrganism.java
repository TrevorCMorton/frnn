package frnn.organisms;

public interface IOrganism {
    IOrganism breed(IOrganism organism, double crossoverRate, double mutationRate);
    void print();
}
