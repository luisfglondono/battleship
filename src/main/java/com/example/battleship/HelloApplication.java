package com.example.battleship;

import com.example.battleship.views.informationView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        informationView.getInstance();
    }
    
    public static void main(String[] args) {
        launch();
    }
}