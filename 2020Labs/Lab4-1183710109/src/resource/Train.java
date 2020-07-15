package resource;

import java.util.Objects;

/**
 * immutable object
 */
public class Train implements Resource {
    private final String trainNumber;
    private final String trainType;
    private final int trainCapacity;

    /*
     * AF:
     * trainNumber represents the number of the train
     * trainType represents the type of the train
     * trainCapacity represents the number of the seats
     * 
     * RI:
     * trainCapacity > 0
     * 
     * safety:
     * do not provide mutator or expose various
     */

    /**
     * constructor
     * @param trainNumber
     * @param trainType
     * @param trainCapacity
     */
    public Train(String trainNumber, String trainType, int trainCapacity) {
        this.trainNumber = trainNumber;
        this.trainType = trainType;
        this.trainCapacity = trainCapacity;
    }

    /**
     * get the String of train number
     * @return the String of train number
     */
    public String getTrainNumber() {
        return this.trainNumber;
    }

    /**
     * get the String of train type
     * @return the String of train type
     */
    public String getTrainType() {
        return this.trainType;
    }

    /**
     * get the int of train capacity
     * @return the int of train capacity
     */
    public int getTrainCapacity() {
        return this.trainCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Train)) {
            return false;
        }
        Train train = (Train) o;
        return Objects.equals(trainNumber, train.trainNumber) && Objects.equals(trainType, train.trainType)
                && trainCapacity == train.trainCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainNumber, trainType, trainCapacity);
    }

    @Override
    public String toString() {
        return "{" + " trainNumber='" + getTrainNumber() + "'" + ", trainType='" + getTrainType() + "'"
                + ", trainCapacity='" + getTrainCapacity() + "'" + "}";
    }

}