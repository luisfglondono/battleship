package com.example.battleship.models;

public class Game {
    private int turn;
    public Game() {
        this.turn = 0;
    }
    public void setTurn() {
        this.turn += 1;
    }
    public int getTurn() {
        return this.turn;
    }
}
