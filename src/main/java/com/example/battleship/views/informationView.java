package com.example.battleship.views;

import com.example.battleship.controllers.informationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The informationView class represents the view for displaying information.
 * It loads the FXML layout and initializes the corresponding controller.
 *
 * @version 1.0
 */
public class informationView extends Stage
{
    /**
     * The controller associated with this view.
     */
    private informationController informationController;

    /**
     * Constructor for informationView.
     * Loads the FXML layout and initializes the controller.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public informationView() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/battleship/information-view.fxml"));
        Parent root = loader.load();
        this.informationController = loader.getController();
        this.setTitle("Battle ship");
        this.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/battleship/images/yate.png")));
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.show();
        this.informationController.checkAndLoadGame();
    }
    /**
     * Gets the controller associated with this view.
     *
     * @return the informationController instance
     */
    public informationController getGameController() {
        return this.informationController;
    }
    /**
     * Gets the singleton instance of informationView.
     *
     * @return the singleton instance of informationView
     * @throws IOException if the FXML file cannot be loaded
     */
    public static informationView getInstance() throws IOException {
        return InformationViewHolder.INSTANCE = new informationView();
    }
    /**
     * Holder class for the singleton instance of informationView.
     */
    private static class InformationViewHolder {
        private static informationView INSTANCE;
    }
}

