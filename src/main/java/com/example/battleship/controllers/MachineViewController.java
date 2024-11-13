package com.example.battleship.controllers;

import com.example.battleship.models.Game;
import com.example.battleship.models.Matrix;
import com.example.battleship.models.Ship;
import com.example.battleship.views.GameView;
import com.example.battleship.views.MachineViewView;
import com.example.battleship.views.PlacementView;
import com.example.battleship.views.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;

public class MachineViewController {
    private Matrix machineBoard;
    private Matrix playerBoard;
    @FXML
    private AnchorPane columnsPane;

    @FXML
    private AnchorPane panePosition;

    @FXML
    private AnchorPane rowsPane;

    @FXML
    private AnchorPane columnsPaneMachine;


    @FXML
    private AnchorPane panePositionMachine;


    @FXML
    private AnchorPane rowsPaneMachine;


    private final int GRID_SIZE = 400;
    private final int NUMBERS_CELL = 10;
    private final int CELL_SIZE = GRID_SIZE / NUMBERS_CELL;

    public void setBoards(Matrix machineBoard, Matrix playerBoard) {
        this.machineBoard = machineBoard;
        this.playerBoard = playerBoard;

        this.drawGrid();
        this.drawShips();
        this.drawGridMachine();
        this.drawShipsMachine();

    }
    public void drawGridMachine() {
        Line line;

        for (int i = 0; i <= NUMBERS_CELL; i++) {
            line = new Line(0, i * CELL_SIZE, GRID_SIZE, i * CELL_SIZE);
            line.setStroke(Color.web("#b4b4ff"));
            line.setStrokeWidth(1);
            panePositionMachine.getChildren().add(line);


            line = new Line(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE);
            line.setStrokeWidth(1);
            line.setStroke(Color.web("#b4b4ff"));

            panePositionMachine.getChildren().add(line);

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

            columnsPaneMachine.getChildren().add(label);

            label = new Label(String.valueOf(i + 1));
            label.setPrefSize(40, 40);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-size: 18px;");

            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setTopAnchor(label, i * 40.0);



            rowsPaneMachine.getChildren().add(label);
        }
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
        Path path;
        for (int i = 0; i < 10; i++) {
            ship = playerBoard.getShip(i);

            path = ship.getDraw();

            path.setLayoutX(ship.getTailX() * CELL_SIZE);
            path.setLayoutY(ship.getTailY() * CELL_SIZE);

            path.setStrokeWidth(2);
            path.setStroke(Color.web("#00f"));
            path.setFill(Color.rgb(0, 0, 255, 0.05));

            if (ship.getDirection() == Ship.Direction.VERTICAL) {
                Rotate rotate = new Rotate(90, 20, 20);

                path.getTransforms().add(rotate);
            }

            path.setUserData(ship);

            panePosition.getChildren().add(path);
        }
    }
    public void drawShipsMachine() {
        Ship ship;
        Path path;
        for (int i = 0; i < 10; i++) {
            ship = machineBoard.getShip(i);

            path = ship.getDraw();

            path.setLayoutX(ship.getTailX() * CELL_SIZE);
            path.setLayoutY(ship.getTailY() * CELL_SIZE);

            path.setStrokeWidth(2);
            path.setStroke(Color.web("#00f"));
            path.setFill(Color.rgb(0, 0, 255, 0.05));

            if (ship.getDirection() == Ship.Direction.VERTICAL) {
                Rotate rotate = new Rotate(90, 20, 20);

                path.getTransforms().add(rotate);
            }

            path.setUserData(ship);

            panePositionMachine.getChildren().add(path);
        }
    }
    @FXML
    void onActionLastWindow(ActionEvent event) {
        AlertBox alertBox = new AlertBox();
        boolean confirmed = alertBox.showConfirmation("Confirmacion", "¿Estas seguro que quieres volver a la ventana anterior? (Puedes editar el tablero de nuevo).");
        if (confirmed)
        {

            Stage stage = (Stage) panePosition.getScene().getWindow();
            stage.close();
            PlacementController.getInstance().show();

        }

    }

    @FXML
    void onActionNextWindow(ActionEvent event) {
        AlertBox alertBox = new AlertBox();
        boolean confirmed = alertBox.showConfirmation("Confirmacion", "¿Estas seguro que quieres comenzar una partida?");
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
