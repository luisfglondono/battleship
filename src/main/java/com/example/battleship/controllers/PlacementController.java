package com.example.battleship.controllers;

import com.example.battleship.models.Matrix;
import com.example.battleship.models.Ship;
import com.example.battleship.views.GameView;
import com.example.battleship.views.PlacementView;
import com.example.battleship.views.alert.AlertBox;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class PlacementController {
    @FXML
    private AnchorPane columnsPane;

    @FXML
    private AnchorPane panePosition;

    @FXML
    private AnchorPane rowsPane;

    private final int GRID_SIZE = 400;
    private final int NUMBERS_CELL = 10;
    private final int CELL_SIZE = GRID_SIZE / NUMBERS_CELL;

    private Matrix playerBoard;
    private Matrix machineBoard;

    private boolean movementValid;
    private int positionInitialX;
    private int positionInitialY;
    private boolean isDragging;
    private Path targetPath;
    private Ship targetShip;

    public void initialize() {
        playerBoard = new Matrix();
        movementValid = false;

        this.drawGrid();
        this.drawShips();

        panePosition.setOnMouseMoved(this::handleMouseMoved);
        panePosition.setOnMousePressed(this::handleMousePressed);
        panePosition.setOnMouseDragged(this::handleMouseDragged);
        panePosition.setOnMouseReleased(this::handleMouseReleased);
    }

    private void handleMouseMoved(MouseEvent event) {
        try {
            this.targetPath = (Path) event.getTarget();

            this.panePosition.setCursor(Cursor.MOVE);
        } catch (Exception e) {
            panePosition.setCursor(Cursor.DEFAULT);
        }
    }

    public void handleMousePressed(MouseEvent event) {
        try {
            this.targetPath = (Path) event.getTarget();

            this.targetShip = (Ship) targetPath.getUserData();

            positionInitialX = (int) (this.targetPath.getLayoutX() / CELL_SIZE);
            positionInitialY = (int) (this.targetPath.getLayoutY() / CELL_SIZE);

            playerBoard.removeShip(positionInitialX, positionInitialY, this.targetShip);

            this.targetPath.setStroke(Color.GREEN);
        } catch (Exception ignored) {}
    }

    public void handleMouseDragged(MouseEvent event) {
        try {

            int positionX = (int) event.getX();
            int positionY = (int) event.getY();

            isDragging = true;

            int positionFixX = (positionX / CELL_SIZE) * CELL_SIZE;
            int positionFixY = (positionY / CELL_SIZE) * CELL_SIZE;

            movementValid = playerBoard.validatePosition(positionX / CELL_SIZE, positionY / CELL_SIZE, targetShip);

            if (movementValid) {
                this.targetPath.setLayoutX(positionFixX);
                this.targetPath.setLayoutY(positionFixY);
                this.targetPath.setStroke(Color.web("#40bf44"));
                this.targetPath.setFill(Color.rgb(64,191,68,.05));
            } else {
                this.targetPath.setLayoutX(event.getX());
                this.targetPath.setLayoutY(event.getY());
                this.targetPath.setStroke(Color.web("#00f"));
                this.targetPath.setFill(Color.rgb(0, 0, 255, 0.05));
            }

        } catch (Exception ignored) {}
    }

    public void handleMouseReleased(MouseEvent event) {
        try {
            this.targetPath.setStroke(Color.web("#00f"));
            this.targetPath.setFill(Color.rgb(0, 0, 255, 0.05));

            if (isDragging) {
                int positionX = (int) (this.targetPath.getLayoutX() / CELL_SIZE);
                int positionY = (int) (this.targetPath.getLayoutY() / CELL_SIZE);

                if (movementValid) {
                    this.targetShip.setTailX(positionX);
                    this.targetShip.setTailY(positionY);
                } else {
                    this.targetPath.setLayoutX(positionInitialX * CELL_SIZE);
                    this.targetPath.setLayoutY(positionInitialY * CELL_SIZE);
                }
            } else {
                playerBoard.removeShip(this.targetShip.getTailX(), this.targetShip.getTailY(), this.targetShip);

                this.targetShip.rotate();

                movementValid = playerBoard.validatePosition(this.targetShip.getTailX(), this.targetShip.getTailY(), this.targetShip);

                if (movementValid) {
                    this.targetPath.getElements().clear();

                    this.targetPath.getElements().add(new MoveTo(0, 0));
                    this.targetPath.getElements().add(new LineTo(0, this.targetShip.getHeight() * CELL_SIZE));
                    this.targetPath.getElements().add(new LineTo(this.targetShip.getWidth() * CELL_SIZE, this.targetShip.getHeight() * CELL_SIZE));
                    this.targetPath.getElements().add(new LineTo(this.targetShip.getWidth() * CELL_SIZE, 0));
                    this.targetPath.getElements().add(new LineTo(0, 0));

                    this.targetPath.setLayoutX(this.targetShip.getTailX() * CELL_SIZE);
                    this.targetPath.setLayoutY(this.targetShip.getTailY() * CELL_SIZE);

                } else {
                    this.targetShip.rotate();
                }
            }

            playerBoard.putShip(this.targetShip.getTailX(), this.targetShip.getTailY(), this.targetShip);

            this.targetPath = null;
            this.targetShip = null;
            this.isDragging = false;

        } catch (Exception ignored) {}
    }

    public void drawGrid() {
        Line line;

        for (int i = 0; i <= NUMBERS_CELL; i++) {
            line = new Line(0, i * CELL_SIZE, GRID_SIZE, i * CELL_SIZE);
            line.setStroke(Color.web("#b4b4ff"));
            line.setStrokeWidth(1);
            panePosition.getChildren().add(line);

            line = new Line(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE);
            line.setStrokeWidth(1);
            line.setStroke(Color.web("#b4b4ff"));

            panePosition.getChildren().add(line);
        }

        Label label;

        for (int i = 0; i < NUMBERS_CELL; i++) {
            char letter = (char) (65 + i);

            label = new Label(String.valueOf(letter));
            label.setPrefSize(40, 40);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-size: 18px;");

            AnchorPane.setLeftAnchor(label, i * 40.0);
            AnchorPane.setTopAnchor(label, 0.0);

            columnsPane.getChildren().add(label);

            label = new Label(String.valueOf(i + 1));
            label.setPrefSize(40, 40);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-size: 18px;");

            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setTopAnchor(label, i * 40.0);

            rowsPane.getChildren().add(label);
        }
    }

    public void drawShips() {
        Ship ship;
        for (int i = 0; i < 10; i++) {
            ship = playerBoard.getShip(i);

            Path path = new Path();

            path.getElements().add(new MoveTo(0, 0));
            path.getElements().add(new LineTo(0, ship.getHeight() * CELL_SIZE));
            path.getElements().add(new LineTo(ship.getWidth() * CELL_SIZE, ship.getHeight() * CELL_SIZE));
            path.getElements().add(new LineTo(ship.getWidth() * CELL_SIZE, 0));
            path.getElements().add(new LineTo(0, 0));
            path.getElements().add(new ArcTo());

            path.setLayoutX(ship.getTailX() * CELL_SIZE);
            path.setLayoutY(ship.getTailY() * CELL_SIZE);

            path.setStrokeWidth(3);
            path.setStroke(Color.web("#00f"));
            path.setFill(Color.rgb(0, 0, 255, 0.05));

            path.setUserData(ship);

            panePosition.getChildren().add(path);
        }
    }
    @FXML
    void onActionMachineView(ActionEvent event) {


    }

    @FXML
    void onActionPlayButton(ActionEvent event) {
        AlertBox alertBox = new AlertBox();
        boolean confirmed = alertBox.showConfirmation("Confirmacion", "¿Estas seguro que quieres comenzar una partida?, (Los barcos no se podrán mover una vez iniciada la partida)");
        if (confirmed)
        {
            try
            {
                GameView gameView = new GameView(machineBoard, playerBoard);
                gameView.show();
                ((Stage) panePosition.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
