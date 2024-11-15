package com.example.battleship.models;

import java.io.Serializable;
import java.util.*;

/**
 * Represents the game board and manages the state of the game.
 */
public class Matrix implements Serializable{
    /**
     * The game board, represented as a 2D array of states.
     */
    private final ArrayList<ArrayList<State>> board = new ArrayList<>();
    /**
     * The list of ships on the board.
     */
    private final List<Ship> ships = new ArrayList<>();
    /**
     * The size of the game board.
     */
    private final int BOARD_SIZE = 10;
    /**
     * The username of the player.
     */
    private String username;
    /**
     * The number of sunk ships.
     */
    private int sunkShips;
    /**
     * Increments the count of sunk ships by one.
     */
    public void setSunkShips() {
        this.sunkShips += 1;
    }
    /**
     * Returns the number of sunk ships.
     *
     * @return the number of sunk ships
     */
    public int getSunkShips() {
        return this.sunkShips;
    }
    /**
     * Sets the username of the player.
     *
     * @param username the username of the player
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Returns the username of the player.
     *
     * @return the username of the player
     */
    public String getUsername() {
        return this.username;
    }
    /**
     * Represents the state of a cell on the game board.
     */
    public enum State {
        EMPTY,
        OCCUPIED,
        WATER,
        HIT,
        SUNK
    }
    /**
     * Constructs a new Matrix instance and initializes the game board and ships.
     */
    public Matrix() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            board.add(new ArrayList<>());

            for (int j = 0; j < BOARD_SIZE; j++) {
                board.get(i).add(State.EMPTY);
            }
        }

        final Random random = new Random();

        int x, y;

        initializeShips();

        for (Ship ship : ships) {
            do {
                x = random.nextInt(BOARD_SIZE);
                y = random.nextInt(BOARD_SIZE);
            } while (!this.putShip(x, y, ship));

            ship.setPosition(x, y);
        }
    }
    /**
     * Returns the ship at the specified index.
     *
     * @param i the index of the ship
     * @return the ship at the specified index
     */
    public Ship getShip(int i) {
        return ships.get(i);
    }
    /**
     * Returns the list of ships on the board.
     *
     * @return the list of ships
     */
    public List<Ship> getShips() {
        return ships;
    }
    /**
     * Initializes the ships on the board.
     */
    public void initializeShips() {
        Map<Ship.Type, Integer> shipTypes = new HashMap<>();
        shipTypes.put(Ship.Type.CARRIER, 1);
        shipTypes.put(Ship.Type.SUBMARINE, 2);
        shipTypes.put(Ship.Type.DESTROYER, 3);
        shipTypes.put(Ship.Type.FRIGATE, 4);

        for (Map.Entry<Ship.Type, Integer> entry : shipTypes.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                ships.add(new Ship(entry.getKey()));
            }
        }
    }
    /**
     * Places a ship on the board at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param ship the ship to be placed
     * @return true if the ship was successfully placed, false otherwise
     */
    public boolean putShip(int x, int y, Ship ship) {
        Ship.Direction direction = ship.getDirection();
        int length = ship.getLength();
        int c, r;

        if (validatePosition(x, y, ship)) {
            for (int i = 0; i < length; i++) {
                r = direction == Ship.Direction.VERTICAL ? y + i : y;
                c = direction == Ship.Direction.HORIZONTAL ? x + i : x;

                board.get(r).set(c, State.OCCUPIED);
            }
        } else {
            return false;
        }

        return true;
    }
    /**
     * Validates if a ship can be placed at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param ship the ship to be placed
     * @return true if the position is valid, false otherwise
     */
    public boolean validatePosition(int x, int y, Ship ship) {
        Ship.Direction direction = ship.getDirection();
        int length = ship.getLength();
        int c, r;

        for (int i = 0; i < length; i++) {
            r = direction == Ship.Direction.VERTICAL ? y + i : y;
            c = direction == Ship.Direction.HORIZONTAL ? x + i : x;

            if (c >= BOARD_SIZE || r >= BOARD_SIZE || board.get(r).get(c) != State.EMPTY) {
                return false;
            }
        }

        return true;
    }
    /**
     * Removes a ship from the board at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param ship the ship to be removed
     */
    public void removeShip(int x, int y, Ship ship) {
        Ship.Direction direction = ship.getDirection();
        int length = ship.getLength();

        int c, r;

        for (int i = 0; i < length; i++) {
            r = direction == Ship.Direction.VERTICAL ? y + i : y;
            c = direction == Ship.Direction.HORIZONTAL ? x + i : x;

            board.get(r).set(c, State.EMPTY);
        }
    }
    /**
     * Prints the current state of the game board to the console.
     */
    public void getBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board.get(i).get(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    /**
     * Checks if the specified coordinates are in a state of water, hit, or sunk.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the state is water, hit, or sunk, false otherwise
     */
    public boolean isWaterHitOrSunk(int x, int y) {
        State state = board.get(y).get(x);
        return state == State.WATER || state == State.HIT || state == State.SUNK;
    }
    /**
     * Checks if the specified coordinates are in a state of hit or sunk.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the state is hit or sunk, false otherwise
     */
    public boolean isHitOrSunk(int x, int y) {
        State state = board.get(y).get(x);
        return state == State.HIT || state == State.SUNK;
    }
    /**
     * Changes the state of the specified coordinates on the board.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param newState the new state to be set
     */
    public void changeState(int x, int y, State newState) {
        board.get(y).set(x, newState);
    }
    /**
     * Returns the state of the specified coordinates on the board.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the state of the specified coordinates
     */
    public State getState(int x, int y) {
        return board.get(y).get(x);
    }
    /**
     * Updates the state of the ship at the specified coordinates to hit.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void updateShipStateToHit(int x, int y) {
        for (Ship ship : ships) {
            int shipX = ship.getTailX();
            int shipY = ship.getTailY();
            int length = ship.getLength();
            Ship.Direction direction = ship.getDirection();

            for (int i = 0; i < length; i++) {
                int currentX = direction == Ship.Direction.HORIZONTAL ? shipX + i : shipX;
                int currentY = direction == Ship.Direction.VERTICAL ? shipY + i : shipY;

                if (currentX == x && currentY == y) {
                    changeState(x, y, State.HIT);
                    return;
                }
            }
        }
    }
    /**
     * Updates the state of the ship to sunk if all its parts are hit.
     */
    public void updateAndCheckShipStateToSunk() {
        for (Ship ship : ships) {
            int shipX = ship.getTailX();
            int shipY = ship.getTailY();
            int length = ship.getLength();
            Ship.Direction direction = ship.getDirection();
            boolean allHit = true;

            for (int i = 0; i < length; i++) {
                int currentX = direction == Ship.Direction.HORIZONTAL ? shipX + i : shipX;
                int currentY = direction == Ship.Direction.VERTICAL ? shipY + i : shipY;


                if (board.get(currentY).get(currentX) != State.HIT) {
                    allHit = false;
                }
            }

            if (allHit) {
                for (int i = 0; i < length; i++) {
                    int sunkX = direction == Ship.Direction.HORIZONTAL ? shipX + i : shipX;
                    int sunkY = direction == Ship.Direction.VERTICAL ? shipY + i : shipY;
                    changeState(sunkX, sunkY, State.SUNK);
                }
                setSunkShips();
            }
        }
    }
    /**
     * Checks if all ships on the board are sunk.
     *
     * @return true if all ships are sunk, false otherwise
     */
    public boolean allShipsSunk() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.get(i).get(j) == State.OCCUPIED) {
                    return false;
                }
            }
        }
        return true;
    }


}
