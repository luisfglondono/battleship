package com.example.battleship.controllers;

import com.example.battleship.models.Game;
import com.example.battleship.models.Matrix;
import com.example.battleship.models.Ship;
import com.example.battleship.views.alert.AlertBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.Random;

public class GameController {
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

    private Game game;

    private final int GRID_SIZE = 400;
    private final int NUMBERS_CELL = 10;
    private final int CELL_SIZE = GRID_SIZE / NUMBERS_CELL;


    public void setBoards(Matrix machineBoard, Matrix playerBoard) {
        this.machineBoard = machineBoard;
        this.playerBoard = playerBoard;
        game = new Game();
        panePositionMachine.setOnMousePressed(this::handleMousePressed);
        this.drawGrid();
        this.drawShips();
        this.drawGridMachine();

    }
    public void handleMousePressed(MouseEvent event) {

        System.out.println("INTENTO " + game.getTurn());
        int x = (int) event.getX()/40;
        int y = (int) event.getY()/40;
        System.out.println("Turno del jugador");
        System.out.println("coordenada en Y " + x);
        System.out.println("coordenada en X " + y);
        System.out.println(game.getTurn());
        if (game.getTurn() % 2 == 0)
        {
            if (machineBoard.isWaterHitOrSunk(x,y))
            {
                new AlertBox().showAlert(
                   "Error",
                    "Disparo invalido",
                   "No puedes disparar 2 veces en el mismo lugar",
                   Alert.AlertType.ERROR);
            }
            if (machineBoard.getState(x,y) == Matrix.State.EMPTY) {
                game.setTurn();
                machineBoard.changeState(x, y, Matrix.State.WATER);
            }
            if (machineBoard.getState(x,y) == Matrix.State.OCCUPIED) {
                game.setTurn();
                machineBoard.updateShipStateToHit(x,y);
                machineBoard.updateAndCheckShipStateToSunk();
            }
            machineBoard.getBoard();
        }
        if (game.getTurn() % 2 != 0)
        {
            Random random = new Random();
            int w,z;
            int BOARD_SIZE = 10;
            do {
                w = random.nextInt(BOARD_SIZE);
                z = random.nextInt(BOARD_SIZE);
            } while (playerBoard.isWaterHitOrSunk(w, z));
            if (playerBoard.getState(w,z) == Matrix.State.EMPTY) {
                game.setTurn();
                playerBoard.changeState(w,z, Matrix.State.WATER);
            }
            if (playerBoard.getState(w,z) == Matrix.State.OCCUPIED) {
                game.setTurn();
                playerBoard.updateShipStateToHit(w,z);
                playerBoard.updateAndCheckShipStateToSunk();
            }
        }
        if(playerBoard.allShipsSunk())
        {
            new AlertBox().showAlert(
                    "¡PERDISTE!",
                    "¡La maquina ha hundido tu flota!",
                    "",
                    Alert.AlertType.INFORMATION
            );
            Platform.exit();
        }
        if(machineBoard.allShipsSunk())
        {
            new AlertBox().showAlert(
                    "¡GANASTE!",
                    "¡Felicidades has hundido la flota enemiga!",
                    "",
                    Alert.AlertType.INFORMATION
            );
            Platform.exit();
        }
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
}
