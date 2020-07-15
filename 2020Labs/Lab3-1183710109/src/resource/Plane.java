package resource;

import java.util.Objects;

/**
 * immutable object
 * a plane containing number, type, seats, age
 */
public class Plane implements Resource {
    private final String number;
    private final String strType;
    private final int intSeats;
    private final double age;
    /*
     * AF:
     * number represents the number of the plane
     * strType represent the type of the plane
     * intSeats represents the number of the seats
     * age represents the age of the plane
     * 
     * RI:
     * intSeats > 0
     * age > 0
     * 
     * safety:
     * do not provide mutator or expose various
     */

    /**
     * constructor
     * @param number
     * @param strType
     * @param intSeats
     * @param age
     */
    public Plane(String number, String strType, int intSeats, double age) {
        this.number = number;
        this.strType = strType;
        this.intSeats = intSeats;
        this.age = age;
    }

    /**
     * get the String of plane number
     * @return the String of plane number
     */
    public String getNumber() {
        return this.number;
    }

    /**
     * get the String of plane type
     * @return the String of plane type
     */
    public String getStrType() {
        return this.strType;
    }

    /**
     * get the int of total seats amount
     * @return the int of total seats amount
     */
    public int getIntSeats() {
        return this.intSeats;
    }

    /**
     * get the double of plane age
     * @return the double of plane age
     */
    public double getAge() {
        return this.age;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Plane)) {
            return false;
        }
        Plane plane = (Plane) o;
        return Objects.equals(number, plane.number) && Objects.equals(strType, plane.strType)
                && intSeats == plane.intSeats && age == plane.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, strType, intSeats, age);
    }

    @Override
    public String toString() {
        return "{" + " number='" + getNumber() + "'" + ", strType='" + getStrType() + "'" + ", intSeats='"
                + getIntSeats() + "'" + ", age='" + getAge() + "'" + "}";
    }

}