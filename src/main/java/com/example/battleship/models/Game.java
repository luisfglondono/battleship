package com.example.battleship.models;

public class Game {
    private int turn;
    private int sunkShips;
    public Game() {
        this.turn = 0;
        this.sunkShips = 0;
    }
    public void setSunkShips() {
        this.sunkShips += 1;
    }
    public int getSunkShips() {
        return this.sunkShips;
    }
    public void setTurn() {
        this.turn += 1;
    }
    public int getTurn() {
        return this.turn;
    }
}
