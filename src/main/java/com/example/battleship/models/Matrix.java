package com.example.battleship.models;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
    private ArrayList<ArrayList<Integer>> board = new ArrayList<>();
    private final int GRID_SIZE = 10;

    public Matrix() {
        for (int i = 0; i < GRID_SIZE; i++) {
            board.add(new ArrayList<>());

            for (int j = 0; j < GRID_SIZE; j++) {
                board.get(i).add(0);
            }
        }
    }

    public boolean putShip(int x, int y, boolean isHorizontal, int length) {
        int c, r;

        if (validatePosition(x, y, isHorizontal, length)) {
            for (int i = 0; i < length; i++) {
                c = isHorizontal ? x + i : x;
                r = isHorizontal ? y : y + i;

                board.get(r).set(c, 1);
            }
        } else {
            return false;
        }

        return true;
    }

    public boolean validatePosition(int x, int y, boolean isHorizontal, int length) {
        int c, r;

        for (int i = 0; i < length; i++) {
            r = isHorizontal ? y : y + i;
            c = isHorizontal ? x + i : x;

            if (c >= GRID_SIZE || r >= GRID_SIZE || board.get(r).get(c) != 0) {
                return false;
            }
        }

        return true;
    }

    public void removeShip(int x, int y, boolean isHorizontal, int length) {
        int c, r;

        for (int i = 0; i < length; i++) {
            r = isHorizontal ? y : y + i;
            c = isHorizontal ? x + i : x;

            board.get(r).set(c, 0);
        }
    }

    public void getBoard() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(board.get(i).get(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
