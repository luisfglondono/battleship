package com.example.battleship.controllers;

import com.example.battleship.models.Matrix;
import javafx.fxml.FXML;

public class GameController {
    private Matrix machineBoard;
    private Matrix playerBoard;
    private PlacementController placementController;

    public void usePlacementControllerMethod() {
        placementController.drawShips();
    }

    public void setBoards(Matrix machineBoard, Matrix playerBoard) {
        this.machineBoard = machineBoard;
        this.playerBoard = playerBoard;
        usePlacementControllerMethod();
    }

    // Otros métodos y lógica del controlador
}
