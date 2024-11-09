package com.example.battleship.models;

public class Ship {
    private int tailX;
    private int tailY;
    private int length;
    private boolean isHorizontal;
    private Type type;

    public enum Type {
        AIRCRAFT_CARRIER,
        SUBMARINE,
        DESTROYER,
        FRIGATE
    }

    public Ship(Type type) {
        this.type = type;

        switch (type) {
            case AIRCRAFT_CARRIER:
                length = 4;
                break;

            case SUBMARINE:
                length = 3;
                break;

            case DESTROYER:
                length = 2;
                break;
            case FRIGATE:
                length = 1;
                break;
        }
    }

    public int getTailX() {
        return tailX;
    }

    public void setTailX(int tailX) {
        this.tailX = tailX;
    }

    public int getTailY() {
        return tailY;
    }

    public void setTailY(int tailY) {
        this.tailY = tailY;
    }

    public void setOrientation(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public int getHeight() {
        return isHorizontal ? 1 : length;
    }

    public int getWidth() {
        return isHorizontal ? length : 1;
    }

    public int getLength() {
        return length;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public boolean checkPosition(int x, int y) {

        System.out.println("getHeight() = " + getHeight());
        System.out.println("getWidth() = " + getWidth());
        return tailX <= x && tailX + getWidth() - 1 >= x && tailY <= y && tailY + getHeight() - 1 >= y;
    }
}