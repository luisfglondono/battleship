package com.example.battleship.controllers;

import com.example.battleship.models.Game;
import com.example.battleship.models.Matrix;
import com.example.battleship.models.Ship;
import com.example.battleship.models.utilities.serialization;
import com.example.battleship.views.PlacementView;
import com.example.battleship.views.alert.AlertBox;
import com.example.battleship.views.informationView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

import java.io.IOException;
import java.util.Random;

public class GameController {
    /**
     * Label for displaying the current turn in the game.
     */
    @FXML
    private Label labelTurnInGame;
    /**
     * Matrix representing the machine's board.
     */
    private Matrix machineBoard;
    /**
     * Matrix representing the player's board.
     */
    private Matrix playerBoard;
    /**
     * Serialization utility for saving and loading game state.
     */
    private serialization serialization;
    /**
     * AnchorPane for displaying the columns.
     */
    @FXML
    private AnchorPane columnsPane;
    /**
     * AnchorPane for positioning elements.
     */
    @FXML
    private AnchorPane panePosition;
    /**
     * AnchorPane for displaying the rows.
     */
    @FXML
    private AnchorPane rowsPane;
    /**
     * AnchorPane for displaying the machine's columns.
     */
    @FXML
    private AnchorPane columnsPaneMachine;
    /**
     * AnchorPane for positioning machine elements.
     */
    @FXML
    private AnchorPane panePositionMachine;
    /**
     * AnchorPane for displaying the machine's rows.
     */
    @FXML
    private AnchorPane rowsPaneMachine;
    /**
     * Label for displaying information.
     */
    @FXML
    private Label informationLabel;
    /**
     * Label for displaying username.
     */
    @FXML
    private Label nameLabel;
    /**
     * Game instance for managing game state.
     */
    private Game game;
    /**
     * Size of the grid in pixels.
     */
    private final int GRID_SIZE = 400;
    /**
     * Number of cells in the grid.
     */
    private final int NUMBERS_CELL = 10;
    /**
     * Size of each cell in the grid.
     */
    private final int CELL_SIZE = GRID_SIZE / NUMBERS_CELL;
    /**
     * Sets the boards for the machine and player, initializes the game, and sets up the event handler for mouse presses.
     * Also draws the grids and ships for both the player and machine boards.
     *
     * @param machineBoard the machine's board
     * @param playerBoard the player's board
     */

    public void setBoards(Matrix machineBoard, Matrix playerBoard) {
        this.machineBoard = machineBoard;
        this.playerBoard = playerBoard;
        game = new Game();
        panePositionMachine.setOnMousePressed(this::handleMousePressed);
        this.drawGrid();
        this.drawShips();
        this.drawGridMachine();
        this.drawHitsContinue();
        nameLabel.setText(playerBoard.getUsername());
    }
    /**
     * Handles the action event for starting a new game.
     * Displays a confirmation dialog to the user. If the user confirms,
     * it clears the current game state, opens the information view, and closes the current window.
     *
     * @param event the ActionEvent triggered by the user
     */
    @FXML
    void OnActionNewGame(ActionEvent event) {
        AlertBox alertBox = new AlertBox();
        boolean confirmed = alertBox.showConfirmation("Confirmacion", "¿Estas seguro que quieres comenzar un nuevo juego?, (se perderá el progreso actual)");
        if (confirmed)
        {
            try {
                serialization.clearFile(serialization.getRelativePath());
                informationView infoView = informationView.getInstance();
                infoView.show();
                ((Stage) nameLabel.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Handles the mouse pressed event on the machine's board.
     * Updates the game state based on the player's and machine's moves,
     * checks for win conditions, and saves the game state.
     *
     * @param event the MouseEvent triggered by the user
     */
    public void handleMousePressed(MouseEvent event) {
        int x = (int) event.getX() / 40;
        int y = (int) event.getY() / 40;
        informationLabel.setText("¡Has hundido " + machineBoard.getSunkShips() + " barcos!");
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
                labelTurnInGame.setText("TURNO DE: Maquina");
                System.out.println("TURNO DE: Maquina");
            }

            if (machineBoard.getState(x,y) == Matrix.State.OCCUPIED) {
                machineBoard.updateShipStateToHit(x,y);
                machineBoard.updateAndCheckShipStateToSunk();
                informationLabel.setText("¡Has hundido " + machineBoard.getSunkShips() + " barcos!");
                labelTurnInGame.setText("TURNO DE: " + playerBoard.getUsername());
                System.out.println("TURNO DE: " + playerBoard.getUsername());
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
        }
        if (game.getTurn() % 2 != 0)
        {
            int w,z;
            do{
                Random random = new Random();
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
                    labelTurnInGame.setText("TURNO DE: " + playerBoard.getUsername());
                    System.out.println("TURNO DE: " + playerBoard.getUsername());
                }
                if (playerBoard.getState(w,z) == Matrix.State.OCCUPIED) {
                    playerBoard.updateShipStateToHit(w,z);
                    playerBoard.updateAndCheckShipStateToSunk();
                    labelTurnInGame.setText("TURNO DE: Maquina");
                    System.out.println("TURNO DE: Maquina");
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
            }while(playerBoard.isHitOrSunk(w,z));
            serialization.serializeObjects("objectsSerialization.txt", machineBoard, playerBoard);


            }

        if (playerBoard.allShipsSunk())
        {
            try {
                serialization.clearFile(serialization.getRelativePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            labelTurnInGame.setText("TURNO DE: Maquina");
            new AlertBox().showAlert(
                    "¡PERDISTE!",
                    "¡La maquina ha hundido tu flota!",
                    "",
                    Alert.AlertType.INFORMATION
            );
            Platform.exit();
        }
        if (machineBoard.allShipsSunk())
        {
            try {
                serialization.clearFile(serialization.getRelativePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AlertBox().showAlert(
                    "¡GANASTE!",
                    "¡Felicidades has hundido la flota enemiga!",
                    "",
                    Alert.AlertType.INFORMATION
            );
            Platform.exit();
        }
    }
    /**
     * Draws the grid for the machine's board.
     * This includes drawing horizontal and vertical lines to form the grid,
     * and adding labels for the columns and rows.
     */
    public void drawGridMachine() {
        Line line;

        for (int i = 0; i <= NUMBERS_CELL; i++) {
            line = new Line(0, i * CELL_SIZE, GRID_SIZE, i * CELL_SIZE);
            line.setStroke(Color.web("#6ded49"));
            line.setStrokeWidth(1);
            panePositionMachine.getChildren().add(line);

            line = new Line(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE);
            line.setStrokeWidth(1);
            line.setStroke(Color.web("#6ded49"));

            panePositionMachine.getChildren().add(line);
        }

        Label label;

        for (int i = 0; i < NUMBERS_CELL; i++) {
            char letter = (char) (65 + i);

            label = new Label(String.valueOf(letter));
            label.setPrefSize(40, 40);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-size: 18px; -fx-text-fill: #6ded49");

            AnchorPane.setLeftAnchor(label, i * 40.0);
            AnchorPane.setTopAnchor(label, 0.0);

            columnsPaneMachine.getChildren().add(label);

            label = new Label(String.valueOf(i + 1));
            label.setPrefSize(40, 40);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-size: 18px; -fx-text-fill: #6ded49");

            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setTopAnchor(label, i * 40.0);

            rowsPaneMachine.getChildren().add(label);
        }
    }
    /**
     * Draws the grid for the player's board.
     * This includes drawing horizontal and vertical lines to form the grid,
     * and adding labels for the columns and rows.
     */
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
    /**
     * Draws the ships on the player's board.
     * This includes setting the position, stroke, and fill of each ship,
     * and adding them to the pane.
     */
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
    /**
     * Draws a path representing a sunk ship.
     * This includes setting the stroke and fill colors.
     *
     * @return the path representing a sunk ship
     */
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
    /**
     * Draws a path representing a water hit.
     * This includes setting the stroke and fill colors.
     *
     * @return the path representing a water hit
     */
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
    /**
     * Draws a path representing a ship hit.
     * This includes setting the stroke and fill colors.
     *
     * @return the path representing a ship hit
     */
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

    public void drawHitsContinue() {
        //Redibuja todos los hits, de agua, hit normal y hundido, para ambos tableros luego de continuar con el juego anterior
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
                if (machineBoard.getState(i,j) == Matrix.State.WATER) {
                    Path waterHit = drawWaterHit();
                    waterHit.setLayoutX(i * CELL_SIZE);
                    waterHit.setLayoutY(j * CELL_SIZE);
                    panePositionMachine.getChildren().add(waterHit);
                }
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
                if (playerBoard.getState(i,j) == Matrix.State.WATER) {
                    Path waterHit = drawWaterHit();
                    waterHit.setLayoutX(i * CELL_SIZE);
                    waterHit.setLayoutY(j * CELL_SIZE);
                    panePosition.getChildren().add(waterHit);
                }
            }
        }
    }
}
