package org.beadando.beadando;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.initModality(Modality.NONE);
        stage.show();
    }

    public void TesztMetod(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MNB-graf.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setTitle("MNB Grafikon");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}