package com.example.battleship.controllers;

import com.example.battleship.models.Matrix;
import com.example.battleship.models.Ship;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.Random;

public class PlacementController {
    private Matrix playerBoard;

    @FXML
    private AnchorPane panePosition;
    Random rand = new Random();

    private final int GRID_SIZE = 400;
    private final int NUMBERS_CELL = 10;
    private final int CELL_SIZE = GRID_SIZE / NUMBERS_CELL;

    private boolean movementValid;

    private int positionInitialX;
    private int positionInitialY;

    private boolean isDragging;

    private Path pathTarget;

    public void initialize() {
        Path path = new Path();

        movementValid = false;

        path.getElements().add(new MoveTo(0, 0));
        path.getElements().add(new LineTo(0, 40));
        path.getElements().add(new LineTo(80, 40));
        path.getElements().add(new LineTo(80, 0));
        path.getElements().add(new LineTo(0, 0));

        playerBoard = new Matrix();

        path.setStrokeWidth(3);
        path.setStroke(Color.BLACK);
        path.setFill(Color.TRANSPARENT);

        this.drawShips();
        playerBoard.getBoard();

        drawGrid();

        panePosition.setOnMouseMoved(this::handleMouseMoved);
        panePosition.setOnMousePressed(this::handleMousePressed);
        panePosition.setOnMouseDragged(this::handleMouseDragged);
        panePosition.setOnMouseReleased(this::handleMouseReleased);
        panePosition.setOnMouseClicked(this::handleMouseClicked);
    }

    public void drawShips() {
        Ship ship;
        for (int i = 0; i < 10; i++) {
            ship = playerBoard.getShip(i);

            Path path = new Path();

            System.out.println("ship.getWidth() = " + ship.getWidth());
            System.out.println("ship.getHeight() = " + ship.getHeight());

            path.getElements().add(new MoveTo(0, 0));
            path.getElements().add(new LineTo(0, ship.getHeight() * CELL_SIZE));
            path.getElements().add(new LineTo(ship.getWidth() * CELL_SIZE, ship.getHeight() * CELL_SIZE));
            path.getElements().add(new LineTo(ship.getWidth() * CELL_SIZE, 0));
            path.getElements().add(new LineTo(0, 0));

            path.setLayoutX(ship.getTailX() * CELL_SIZE);
            path.setLayoutY(ship.getTailY() * CELL_SIZE);

            path.setStrokeWidth(3);
            path.setStroke(Color.BLACK);
            path.setFill(Color.TRANSPARENT);

            path.setUserData(ship);

            panePosition.getChildren().add(path);
        }
    }

    private void handleMouseMoved(MouseEvent event) {
        try {
            pathTarget = (Path) event.getTarget();

            panePosition.setCursor(Cursor.MOVE);
        } catch (ClassCastException e) {
            panePosition.setCursor(Cursor.DEFAULT);
        }
    }

    public void handleMousePressed(MouseEvent event) {
        try {
            pathTarget = (Path) event.getTarget();

            Ship shipTarget = (Ship) pathTarget.getUserData();

            positionInitialX = (int) (pathTarget.getLayoutX() / CELL_SIZE);
            positionInitialY = (int) (pathTarget.getLayoutY() / CELL_SIZE);

            playerBoard.removeShip(positionInitialX, positionInitialY, shipTarget);

            pathTarget.setStroke(Color.GREEN);
        } catch (Exception ignored) {}
    }

    public void handleMouseClicked(MouseEvent event) {
        if (isDragging) {
            return;
        }

        try {
            pathTarget = (Path) event.getTarget();
            Ship ship = (Ship) pathTarget.getUserData();

            playerBoard.removeShip(positionInitialX, positionInitialY, ship);

            ship.rotate();

            movementValid = playerBoard.validatePosition(ship.getTailX(), ship.getTailY(), ship);

            if (movementValid) {
                pathTarget.getElements().clear();

                pathTarget.getElements().add(new MoveTo(0, 0));
                pathTarget.getElements().add(new LineTo(0, ship.getHeight() * CELL_SIZE));
                pathTarget.getElements().add(new LineTo(ship.getWidth() * CELL_SIZE, ship.getHeight() * CELL_SIZE));
                pathTarget.getElements().add(new LineTo(ship.getWidth() * CELL_SIZE, 0));
                pathTarget.getElements().add(new LineTo(0, 0));

                pathTarget.setLayoutX(ship.getTailX() * CELL_SIZE);
                pathTarget.setLayoutY(ship.getTailY() * CELL_SIZE);
            } else {
                ship.rotate();
                System.out.println("Entra");
            }

            playerBoard.putShip(positionInitialX, positionInitialY, ship);

        } catch (Exception ignored) {}
    }

    public void handleMouseDragged(MouseEvent event) {
        isDragging = true;

        try {
            pathTarget = (Path) event.getTarget();

            int positionX = (int) event.getX();
            int positionY = (int) event.getY();

            Ship shipTarget = (Ship) pathTarget.getUserData();

            int positionFixX = (positionX / CELL_SIZE) * CELL_SIZE;
            int positionFixY = (positionY / CELL_SIZE) * CELL_SIZE;

            movementValid = playerBoard.validatePosition(positionX / CELL_SIZE, positionY / CELL_SIZE, shipTarget);

            if (movementValid) {
                pathTarget.setLayoutX(positionFixX);
                pathTarget.setLayoutY(positionFixY);
            } else {
                pathTarget.setLayoutX(event.getX());
                pathTarget.setLayoutY(event.getY());
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void handleMouseReleased(MouseEvent event) {
        pathTarget.setStroke(Color.BLACK);

        Ship shipTarget = (Ship) pathTarget.getUserData();

        int positionX = (int) (pathTarget.getLayoutX() / CELL_SIZE);
        int positionY = (int) (pathTarget.getLayoutY() / CELL_SIZE);

        if (movementValid) {
            shipTarget.setTailX(positionX);
            shipTarget.setTailY(positionY);
        } else {
            pathTarget.setLayoutX(positionInitialX * CELL_SIZE);
            pathTarget.setLayoutY(positionInitialY * CELL_SIZE);
        }

        playerBoard.putShip(shipTarget.getTailX(), shipTarget.getTailY(), shipTarget);

        pathTarget = null;

        playerBoard.getBoard();

        if (isDragging) {
            isDragging = false;
        }
    }

    public void drawGrid() {
        Line line;

        for (int i = 0; i <= NUMBERS_CELL; i++) {
            line = new Line(0, i * CELL_SIZE, GRID_SIZE, i * CELL_SIZE);
            line.setStroke(Color.BLUE);
            line.setStrokeWidth(1);
            panePosition.getChildren().add(line);

            line = new Line(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE);
            line.setStrokeWidth(1);
            line.setStroke(Color.BLUE);

            panePosition.getChildren().add(line);
        }
    }

}
