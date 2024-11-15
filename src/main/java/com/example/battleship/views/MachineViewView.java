package com.example.battleship.views;

import com.example.battleship.controllers.GameController;
import com.example.battleship.controllers.MachineViewController;
import com.example.battleship.models.Matrix;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * The MachineViewView class represents the view for the machine's perspective in the Battleship game.
 * It loads the FXML layout and initializes the corresponding controller.
 */
public class MachineViewView extends Stage
{
    /**
     * The controller associated with this view.
     */
    private MachineViewController machineViewController;
    /**
     * Constructor for MachineViewView.
     * Loads the FXML layout and initializes the controller.
     *
     * @param machineBoard the game board for the machine
     * @param playerBoard the game board for the player
     * @throws IOException if the FXML file cannot be loaded
     */
    public MachineViewView(Matrix machineBoard, Matrix playerBoard) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/battleship/machineView-view.fxml"));
        Parent root = loader.load();
        this.machineViewController = loader.getController();
        this.machineViewController.setBoards(machineBoard, playerBoard);
        this.setTitle("Battle ship");
        Scene scene = new Scene(root);
        this.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/battleship/images/yate.png")));
        this.setScene(scene);
        this.show();
    }
    /**
     * Gets the controller associated with this view.
     *
     * @return the MachineViewController instance
     */
    public MachineViewController getMachineViewController() {
        return this.machineViewController;
    }
    /**
     * Gets the singleton instance of MachineViewView.
     *
     * @param machineBoard the game board for the machine
     * @param playerBoard the game board for the player
     * @return the singleton instance of MachineViewView
     * @throws IOException if the FXML file cannot be loaded
     */
    public static MachineViewView getInstance(Matrix machineBoard, Matrix playerBoard) throws IOException {
        return MachineViewHolder.INSTANCE = new MachineViewView(machineBoard,playerBoard);
    }
    /**
     * Holder class for the singleton instance of MachineViewView.
     */
    private static class MachineViewHolder {
        private static MachineViewView INSTANCE;
    }
}


