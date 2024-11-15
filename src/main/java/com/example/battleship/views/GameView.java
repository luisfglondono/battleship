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

/**
 * The GameView class represents the view for the Battleship game.
 * It loads the FXML layout and initializes the corresponding controller.
 */
public class GameView extends Stage {
    /**
     * The controller associated with this view.
     */
    private GameController gameController;
    /**
     * Constructor for GameView.
     * Loads the FXML layout and initializes the controller.
     *
     * @param machineBoard the game board for the machine
     * @param playerBoard the game board for the player
     * @throws IOException if the FXML file cannot be loaded
     */
    public GameView(Matrix machineBoard, Matrix playerBoard) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/battleship/game-view.fxml"));
        Parent root = loader.load();
        this.gameController = loader.getController();
        this.gameController.setBoards(machineBoard, playerBoard);
        this.setTitle("Battle Ship");
        Scene scene = new Scene(root);
        this.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/battleship/images/yate.png")));
        this.setScene(scene);
        this.show();
    }
    /**
     * Gets the controller associated with this view.
     *
     * @return the GameController instance
     */
    public GameController getGameController() {
        return this.gameController;
    }

    /**
     * Gets the singleton instance of GameView.
     *
     * @param machineBoard the game board for the machine
     * @param playerBoard the game board for the player
     * @return the singleton instance of GameView
     * @throws IOException if the FXML file cannot be loaded
     */
    public static GameView getInstance(Matrix machineBoard, Matrix playerBoard) throws IOException {
        return GameViewHolder.INSTANCE = new GameView(machineBoard,playerBoard);
    }
    /**
     * Holder class for the singleton instance of GameView.
     */
    private static class GameViewHolder {
        private static GameView INSTANCE;
    }
}
