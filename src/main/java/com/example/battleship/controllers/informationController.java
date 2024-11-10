package com.example.battleship.controllers;

import com.example.battleship.views.PlacementView;
import com.example.battleship.views.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class informationController {

    @FXML
    private Label welcomeText;

    @FXML
    void onInformationButton(ActionEvent event) {
        new AlertBox().showAlert(
                "Instrucciones",
                "Bienvenido al juego batalla naval",
                "El juego llamado Batalla Naval es un juego de estrategia donde dos jugadores (humano y máquina) compiten por hundir la flota del oponente. Cada jugador coloca sus barcos en un tablero de coordenadas y luego intenta adivinar la ubicación de los barcos del oponente para hundirlos, el primero que derribe la flota del oponente gana la partida.",
                Alert.AlertType.INFORMATION
        );

    }

    @FXML
    void onNoButton(ActionEvent event) {
        AlertBox alertBox = new AlertBox();
        boolean confirmed = alertBox.showConfirmation("Confirmacion", "¿Estas seguro que quieres comenzar un nuevo juego?, (Si ya has empezado un juego se borrará tu progreso actual)");
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

    @FXML
    void onYesButton(ActionEvent event) {
        AlertBox alertBox = new AlertBox();
        boolean confirmed = alertBox.showConfirmation("Confirmacion", "¿Estas seguro que quieres continuar con el juego anterior?");
        if (confirmed)
        {
//            try
//            {
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

    }

}
