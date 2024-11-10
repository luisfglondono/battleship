package com.example.battleship.views.alert;

import javafx.scene.control.Alert;

public interface AlertBoxInterface {
    public void showAlert(String title, String header, String message, Alert.AlertType Type);
}
