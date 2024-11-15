package com.example.battleship.controllers;

import com.example.battleship.models.Matrix;
import com.example.battleship.models.utilities.serialization;
import com.example.battleship.views.GameView;
import com.example.battleship.views.PlacementView;
import com.example.battleship.views.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Controller class for handling information-related actions in the Battleship game.
 */
public class informationController {
    /**
     * Serialization utility for saving and loading game state.
     */
    private serialization serialization;
    /**
     * Matrix representing the machine's board.
     */
    private Matrix machineBoard;
    /**
     * Matrix representing the player's board.
     */
    private Matrix playerBoard;
    /**
     * Label for displaying welcome text.
     */
    @FXML
    private Label welcomeText;
    /**
     * Handles the action event for displaying game information.
     *
     * @param event the ActionEvent triggered by the user
     */
    @FXML
    void onInformationButton(ActionEvent event) {
        new AlertBox().showAlert(
                "Instrucciones",
                "Bienvenido al juego Battle ship",
                "El juego llamado Battle ship es un juego de estrategia donde dos jugadores (humano y máquina) compiten por hundir la flota del oponente. Cada jugador coloca sus barcos en un tablero de coordenadas y luego intenta adivinar la ubicación de los barcos del oponente para hundirlos, el primero que derribe la flota del oponente gana la partida.",
                Alert.AlertType.INFORMATION
        );

    }
    /**
     * Checks for a saved game and loads it if available.
     */
    public void checkAndLoadGame() {
        serialization = new serialization();
        String relativePath = serialization.getRelativePath();
        File saveFile = new File(relativePath);

        if (saveFile.exists() && saveFile.length() > 0) {
            AlertBox alertBox = new AlertBox();
            boolean confirmed = alertBox.showConfirmation("Confirmacion", "Se ha encontrado un juego guardado, Deseas continuar con el juego actual (si selecciona \"NO\" se iniciará un juego nuevo)");
            if (confirmed) {
                this.loadGame();
            }
        }
    }
    /**
     * Handles the action event for starting the game.
     *
     * @param event the ActionEvent triggered by the user
     */
    @FXML
    void onPlayButton(ActionEvent event) {
        AlertBox alertBox = new AlertBox();
        boolean confirmed = alertBox.showConfirmation("Confirmacion", "¿Estas seguro que quieres comenzar a jugar?");
        if (confirmed)
        {
            try
            {
                PlacementView placementView = new PlacementView();
                placementView.show();
                ((Stage) welcomeText.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * Loads the saved game state.
     */
    public void loadGame()
    {
        try
        {
            List<Object> objects = serialization.deserializeObjects(serialization.getRelativePath());
            if (objects.size() >= 2) {
                machineBoard = (Matrix) objects.get(0);
                playerBoard = (Matrix) objects.get(1);
                GameView gameView = new GameView(machineBoard, playerBoard);
                gameView.show();
                Stage stage = (Stage) welcomeText.getScene().getWindow();
                stage.close();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

}
