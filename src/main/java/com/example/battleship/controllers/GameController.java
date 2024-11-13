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
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

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
                Path waterHit = drawWaterHit();
                waterHit.setLayoutX(x * CELL_SIZE);
                waterHit.setLayoutY(y * CELL_SIZE);
                panePositionMachine.getChildren().add(waterHit);
            }
            if (machineBoard.getState(x,y) == Matrix.State.OCCUPIED) {
                game.setTurn();
                machineBoard.updateShipStateToHit(x,y);
                machineBoard.updateAndCheckShipStateToSunk();

                panePositionMachine.getChildren().removeIf(node ->
                        node instanceof Path && ("shipHit".equals(node.getId()) || "shipSunk".equals(node.getId()))
                );
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (machineBoard.getState(i, j) == Matrix.State.SUNK) {
                            Path shipSunk = drawShipSunk();
                            shipSunk.setId("shipSunk");
                            shipSunk.setLayoutX(i * CELL_SIZE);
                            shipSunk.setLayoutY(j * CELL_SIZE);
                            panePositionMachine.getChildren().add(shipSunk);
                        } else if (machineBoard.getState(i, j) == Matrix.State.HIT) {
                            Path shipHit = drawShipHit();
                            shipHit.setId("shipHit");
                            shipHit.setLayoutX(i * CELL_SIZE);
                            shipHit.setLayoutY(j * CELL_SIZE);
                            panePositionMachine.getChildren().add(shipHit);
                        }
                    }
                }

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
                Path waterHit = drawWaterHit();
                waterHit.setLayoutX(w * CELL_SIZE);
                waterHit.setLayoutY(z * CELL_SIZE);
                panePosition.getChildren().add(waterHit);
            }
            if (playerBoard.getState(w,z) == Matrix.State.OCCUPIED) {
                game.setTurn();
                playerBoard.updateShipStateToHit(w,z);
                playerBoard.updateAndCheckShipStateToSunk();

                panePosition.getChildren().removeIf(node ->
                        node instanceof Path && ("shipHit".equals(node.getId()) || "shipSunk".equals(node.getId()))
                );
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (playerBoard.getState(i, j) == Matrix.State.SUNK) {
                            Path shipSunk = drawShipSunk();
                            shipSunk.setId("shipSunk");
                            shipSunk.setLayoutX(i * CELL_SIZE);
                            shipSunk.setLayoutY(j * CELL_SIZE);
                            panePosition.getChildren().add(shipSunk);
                        } else if (playerBoard.getState(i, j) == Matrix.State.HIT) {
                            Path shipHit = drawShipHit();
                            shipHit.setId("shipHit");
                            shipHit.setLayoutX(i * CELL_SIZE);
                            shipHit.setLayoutY(j * CELL_SIZE);
                            panePosition.getChildren().add(shipHit);
                        }
                    }
                }
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

    public static Path drawShipSunk(){
        Path figure = new Path(
                new MoveTo(23,3),
                new ArcTo(12,15,0,23,15,false,true),
                new ArcTo(4,5,0,26,22,false,false),
                new ArcTo(4,5,0,29,13,false,false),
                new QuadCurveTo(43,28,28,36),
                new QuadCurveTo(18,40,8,34),
                new QuadCurveTo(0,26,14,15),
                new QuadCurveTo(23,10,23,3)
        );
        figure.setStroke(Color.rgb(245, 71, 22));
        figure.setStrokeWidth(4);
        figure.setFill(Color.rgb(239, 160, 13, 0.95));

        return figure;
    }

    public static Path drawWaterHit(){
        Path figure = new Path(
                new MoveTo(8,3),
                new LineTo(20,17),
                new LineTo(32,3),
                new LineTo(37,3),
                new LineTo(23,20),
                new LineTo(37,37),
                new LineTo(32,37),
                new LineTo(20,23),
                new LineTo(8,37),
                new LineTo(3,37),
                new LineTo(17,20),
                new LineTo(3,3),
                new ClosePath()
        );

        figure.setStroke(Color.rgb(232, 34, 34));
        figure.setStrokeWidth(2);
        figure.setFill(Color.rgb(232, 34, 34));

        return figure;
    }

    public static Path drawShipHit(){
        Path figure = new Path(
                new MoveTo(18,38),
                new LineTo(15,26),
                new LineTo(9,32),
                new LineTo(10,22),
                new LineTo(2,18),
                new LineTo(10,14),
                new LineTo(),
                new LineTo(16, 11),
                new LineTo(20,0),
                new LineTo(24,11),
                new LineTo(32,6),
                new LineTo(30,14),
                new LineTo(40,20),
                new LineTo(30,24),
                new LineTo(40,40),
                new LineTo(24,28),
                new ClosePath()
        );
        figure.setStroke(Color.rgb(212, 173, 0));
        figure.setStrokeWidth(2);
        figure.setFill(Color.rgb(255, 208, 0,0.95));

        return figure;
    }
}
