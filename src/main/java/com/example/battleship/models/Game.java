package com.example.battleship.models;
/**
 * Represents a game of Battleship.
 * Manages the turn count.
 */
public class Game {
    /**
     * The current turn count in the game.
     */
    private int turn;
    /**
     * Constructs a new Game instance with initial values for turn and sunk ships.
     */
    public Game() {
        this.turn = 0;
    }
    /**
     * Increments the turn count by one.
     */
    public void setTurn() {
        this.turn += 1;
    }
    /**
     * Returns the current turn count.
     *
     * @return the current turn count
     */
    public int getTurn() {
        return this.turn;
    }
}
