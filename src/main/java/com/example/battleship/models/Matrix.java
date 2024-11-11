package com.example.battleship.models;

import java.util.*;

public class Matrix {
    private final ArrayList<ArrayList<State>> board = new ArrayList<>();
    private final List<Ship> ships = new ArrayList<>();
    private final int BOARD_SIZE = 10;

    public enum State {
        EMPTY,
        OCCUPIED,
        WATER,
        HIT,
        SUNK
    }

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

    public Ship getShip(int i) {
        return ships.get(i);
    }

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

    public void getBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board.get(i).get(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public boolean isWaterHitOrSunk(int x, int y) {
        State state = board.get(x).get(y);
        return state == State.WATER || state == State.HIT || state == State.SUNK;
    }
    public void changeState(int x, int y, State newState) {
        board.get(y).set(x, newState);
    }
    public State getState(int x, int y) {
        return board.get(y).get(x);
    }
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
            }
        }
    }
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
