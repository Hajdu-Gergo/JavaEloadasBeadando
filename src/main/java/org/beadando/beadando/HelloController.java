package org.beadando.beadando;

import com.oanda.v20.primitives.DateTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    @FXML
    public void TesztMetod(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MNB-graf.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setTitle("MNB Grafikon");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }






    public void olvas(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("olvas.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setTitle("Olvas");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void olvas2(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("olvas2.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setTitle("Olvas2");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void ir(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ir.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setTitle("Ir");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void modosit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("modosit.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setTitle("Modosit");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void torol(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("torol.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setTitle("Torol");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void parhuzam(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("parhuzam.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setTitle("Parhuzam");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void szamlainfo(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FOREX/Szamlainfo.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setTitle("Számlalekérdezés");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void aktarak(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FOREX/Aktualisarak.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setTitle("Aktuális árak");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void histarak(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FOREX/Historikusarak.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setTitle("Historikus árak");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void poznyit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FOREX/Pozicionyitas.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setTitle("Pozíció nyitás");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void pozzar(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FOREX/Poziciozaras.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setTitle("Pozíció zárás");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void nyittpoz(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FOREX/Nyitottpoziciok.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setTitle("Nyitott pozíciók");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}