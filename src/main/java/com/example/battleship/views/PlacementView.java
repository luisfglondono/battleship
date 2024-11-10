package com.example.battleship.views;

import com.example.battleship.controllers.PlacementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The informacionView class represents the view for displaying information.
 * It loads the FXML layout and initializes the corresponding controller.
 *
 * @version 1.0
 */
public class PlacementView extends Stage
{
    /**
     * The controller associated with this view.
     */
    private PlacementController placementController;

    /**
     * Constructor for informacionView.
     * Loads the FXML layout and initializes the controller.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public PlacementView() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/battleship/placement-view.fxml")
        );
        Parent root = loader.load();
        // se usa para conectar la vista de Game con el controlador de game
        this.placementController = loader.getController();
        this.setTitle("Batalla Naval");
        Scene scene = new Scene(root);
        this.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/battleship/yate.png")));
        this.setScene(scene);
        this.show();
    }
    /**
     * Gets the controller associated with this view.
     *
     * @return the informacionController instance
     */
    public PlacementController getPlacementController() {
        return this.placementController;
    }
    /**
     * Gets the singleton instance of informacionView.
     *
     * @return the singleton instance of informacionView
     * @throws IOException if the FXML file cannot be loaded
     */
    public static PlacementView getInstance() throws IOException {
        return PlacementViewHolder.INSTANCE = new PlacementView();
    }
    /**
     * Holder class for the singleton instance of informacionView.
     */
    private static class PlacementViewHolder {
        private static PlacementView INSTANCE;
    }
}























