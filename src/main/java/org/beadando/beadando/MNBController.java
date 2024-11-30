package org.beadando.beadando;

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

public class MNBController {



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
    @FXML
    LineChart<String, Number> grafikon;

    @FXML
    public void Beolvas() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT fájlok", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            GrafBeolvas(grafikon, selectedFile);
        }
    }

    @FXML
    public void GrafBeolvas(LineChart<String, Number> grafikon, File fajl) {

        Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fajl);

            NodeList days = doc.getElementsByTagName("Day");
            for (int i = 0; i < days.getLength(); i++) {
                Element day = (Element) days.item(i);
                String date = day.getAttribute("date");

                NodeList rates = day.getElementsByTagName("Rate");
                for (int j = 0; j < rates.getLength(); j++) {
                    Element rateElement = (Element) rates.item(j);
                    String currency = rateElement.getAttribute("curr");
                    String rateStr = rateElement.getTextContent().replace(",", ".");
                    double rate = Double.parseDouble(rateStr);

                    // Series lekérése vagy létrehozása valutánként
                    XYChart.Series<String, Number> series = seriesMap.computeIfAbsent(currency, curr -> {
                        XYChart.Series<String, Number> newSeries = new XYChart.Series<>();
                        newSeries.setName(curr + " árfolyam");
                        return newSeries;
                    });

                    // Adatok hozzáadása a sorozathoz
                    series.getData().add(new XYChart.Data<>(date, rate));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Minden series hozzáadása a chart-hoz
        for (XYChart.Series<String, Number> series : seriesMap.values()) {
            grafikon.getData().add(series);
        }

    }

    public void adatbazismegnyit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setTitle("Crud");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}