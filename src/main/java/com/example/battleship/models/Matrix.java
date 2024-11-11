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

    public List<Ship> getShips() {
        return ships;
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
}
