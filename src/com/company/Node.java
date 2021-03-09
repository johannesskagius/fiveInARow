package com.company;

public class Node {
    private Brick status;

    public Node (Brick s) {
        status = s;
    }

    public void setStatus (Brick status) {
        this.status = status;
    }

    public enum Brick{
        PLAYER("P"),
        COMPUTER("C"),
        NOTPLAYED("-"),
        THISNODE("*");

        String value;

        Brick (String s) {
            value = s;
        }

        @Override
        public String toString () {
            return value;
        }
    }

    public String getStatus () {
        return status.toString ();
    }

    @Override
    public String toString () {
        return "|"+status+"|";
    }
}
