package Entities;

public class TransportationNetwork {
    public String  sourceAreaCode;
    public String  destinationAreaCode;
    public Integer moveTime;

    public TransportationNetwork(
            String sourceAreaCode, String destinationAreaCode,
            Integer moveTime) {
        this.sourceAreaCode = sourceAreaCode;
        this.destinationAreaCode = destinationAreaCode;
        this.moveTime = moveTime;
    }
}
