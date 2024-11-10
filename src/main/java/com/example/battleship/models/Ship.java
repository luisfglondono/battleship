package com.example.battleship.models;

import java.util.Random;

public class Ship {
    private int tailX;
    private int tailY;
    private int length;
    private Direction direction;

    public enum Type {
        CARRIER,
        SUBMARINE,
        DESTROYER,
        FRIGATE
    }

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    public Ship(Type type) {
        Random random = new Random();

        direction = Direction.values()[random.nextInt(Direction.values().length)];

        switch (type) {
            case CARRIER:
                this.length = 4;
                break;

            case SUBMARINE:
                this.length = 3;
                break;

            case DESTROYER:
                this.length = 2;
                break;
            case FRIGATE:
                this.length = 1;
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

    public void setOrientation(Direction direction) {
        this.direction = direction;
    }

    public int getHeight() {
        return direction == Direction.VERTICAL ? length : 1;
    }

    public int getWidth() {
        return direction == Direction.HORIZONTAL ? length : 1;
    }

    public int getLength() {
        return length;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setPosition(int x, int y) {
        this.tailX = x;
        this.tailY = y;
    }

    public void rotate() {
        direction = direction == Direction.HORIZONTAL ? Direction.VERTICAL : Direction.HORIZONTAL;
    }

    public boolean checkPosition(int x, int y) {

        return tailX <= x && tailX + getWidth() - 1 >= x && tailY <= y && tailY + getHeight() - 1 >= y;
    }
}