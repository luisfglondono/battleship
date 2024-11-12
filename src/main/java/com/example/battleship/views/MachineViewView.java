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

public class MachineViewView extends Stage
{
        private MachineViewController machineViewController;

        public MachineViewView(Matrix machineBoard, Matrix playerBoard) throws IOException {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/battleship/machineView-view.fxml")
            );
            Parent root = loader.load();
            this.machineViewController = loader.getController();
            this.machineViewController.setBoards(machineBoard, playerBoard);
            this.setTitle("Battle ship");
            Scene scene = new Scene(root);
            this.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/battleship/images/yate.png")));
            this.setScene(scene);
            this.show();
        }

        public MachineViewController getMachineViewController() {
            return this.machineViewController;
        }

        public static MachineViewView getInstance(Matrix machineBoard, Matrix playerBoard) throws IOException {
            return MachineViewHolder.INSTANCE = new MachineViewView(machineBoard,playerBoard);
        }
        /**
         * Holder class for the singleton instance of informacionView.
         */
        private static class MachineViewHolder {
            private static MachineViewView INSTANCE;
        }
}


