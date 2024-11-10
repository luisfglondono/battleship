package com.example.battleship.views;

import com.example.battleship.controllers.GameController;
import com.example.battleship.controllers.PlacementController;
import com.example.battleship.models.Matrix;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class GameView extends Stage {
    private GameController gameController;

    public GameView(Matrix machineBoard, Matrix playerBoard) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/battleship/game-view.fxml")
        );
        Parent root = loader.load();
        this.gameController = loader.getController();
        this.gameController.setBoards(machineBoard, playerBoard);
        this.setTitle("Batalla Naval");
        Scene scene = new Scene(root);
        this.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/battleship/images/yate.png")));
        this.setScene(scene);
        this.show();
    }

    public GameController getGameController() {
        return this.gameController;
    }

    /**
     * Gets the singleton instance of informacionView.
     *
     * @return the singleton instance of informacionView
     * @throws IOException if the FXML file cannot be loaded
     */
    public static GameView getInstance(Matrix machineBoard, Matrix playerBoard) throws IOException {
        return GameViewHolder.INSTANCE = new GameView(machineBoard,playerBoard);
    }
    /**
     * Holder class for the singleton instance of informacionView.
     */
    private static class GameViewHolder {
        private static GameView INSTANCE;
    }
}
