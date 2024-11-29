package org.beadando.beadando;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/main.fxml").toURI().toURL());

        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Java Eloadas Beadando");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
