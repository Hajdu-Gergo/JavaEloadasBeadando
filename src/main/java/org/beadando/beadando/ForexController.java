package org.beadando.beadando;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.lang.management.ClassLoadingMXBean;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class ForexController implements Initializable {

    @FXML
    TableView<Map.Entry<String, String>> tablazat;


    @FXML
    ComboBox<String> devizapar;

    @FXML
    Label kiir;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        devizapar.getItems().addAll("EUR_HUF","USD_HUF","GBP_HUF","EUR_USD", "USD_JPY", "GBP_USD", "USD_CHF", "EUR_GBP", "EUR_JPY", "EUR_CHF", "AUD_USD", "USD_CAD", "NZD_USD");
        devizapar.setValue("EUR_HUF");

    }

    public void lekerdezTablaba() {
        String accountSummary=Oanda.getAccountSummary();


        if (tablazat == null) {
            System.err.println("Táblázat nem létezik!");
            return;
        }

        // Ha nincsenek oszlopok a táblázatban, hozzuk létre őket
        if (tablazat.getColumns().isEmpty()) {
            TableColumn<Map.Entry<String, String>, String> infocellaColumn = new TableColumn<>("infocella");
            infocellaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
            infocellaColumn.setText("Információk");

            TableColumn<Map.Entry<String, String>, String> ertekcellaColumn = new TableColumn<>("ertekcella");
            ertekcellaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getValue()));
            ertekcellaColumn.setText("Értékek");
            tablazat.getColumns().add(infocellaColumn);
            tablazat.getColumns().add(ertekcellaColumn);
        }

        // Adatok szétbontása kulcs-érték párokra
        accountSummary = accountSummary.replace("AccountSummary(", "").replace(")", "");
        String[] pairs = accountSummary.split(", ");
        Map<String, String> map = new LinkedHashMap<>();

        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            } else {
                map.put(keyValue[0], "null");
            }
            //System.out.println(keyValue[0] + " " + keyValue[1]);
        }

        //System.out.println(map);
        // Az adatok ObservableList formába konvertálása
        ObservableList<Map.Entry<String, String>> data = FXCollections.observableArrayList(map.entrySet());

        //System.out.println("ObservableList OK!");
        // A táblázat adatainak frissítése
        tablazat.getItems().clear(); // Meglévő adatok törlése
        //System.out.println("Törlés OK!");
        tablazat.getItems().addAll(data); // Új adatok hozzáadása
        tablazat.refresh(); // Táblázat frissítése
    }

    public void lekerdezAktar(ActionEvent actionEvent) {
        System.out.println(Oanda.getPricing(devizapar.getValue()));
        String instrument = extractValue(Oanda.getPricing(devizapar.getValue()), "instrument=", ", time=");
        String[] currencies = instrument.split("_");
        String baseCurrency = currencies[0];
        String quoteCurrency = currencies[1];
        String price = extractValue(Oanda.getPricing(devizapar.getValue()), "closeoutAsk=", ",");

        kiir.setText("1 " + baseCurrency + " = " + price + " "+quoteCurrency );

        System.out.println("Base currency: " + baseCurrency);
        System.out.println("Quote currency: " + quoteCurrency);
        System.out.println("Price: " + price);
    }
    private static String extractValue(String data, String startToken, String endToken) {
        int startIndex = data.indexOf(startToken) + startToken.length();
        int endIndex = data.indexOf(endToken, startIndex);
        return data.substring(startIndex, endIndex).trim();
    }
}
