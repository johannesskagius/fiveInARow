package com.company;

import java.util.Objects;

public class Coordinate implements Comparable<Coordinate>{
    private int x;
    private int y;
    private int coordinateValue;

    public Coordinate (int y,int x) {
        this.x = x;
        this.y = y;
    }

    public Coordinate (int x,int y,int coordinateValue) {
        this.x = x;
        this.y = y;
        this.coordinateValue = coordinateValue;
    }

    public int getCoordinateValue () {
        return coordinateValue;
    }

    public void setCoordinateValue (int coordinateValue) {
        this.coordinateValue = coordinateValue;
    }

    public int getX () {
        return x;
    }

    public void setX (int x) {
        this.x = x;
    }

    public int getY () {
        return y;
    }

    public void setY (int y) {
        this.y = y;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode () {
        return Objects.hash ( x,y );
    }

    @Override
    public int compareTo (Coordinate o) {
        return o.coordinateValue <= this.coordinateValue ? -1 : 1;
    }
}
