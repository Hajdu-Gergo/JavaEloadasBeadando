package org.beadando.beadando;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.LinkedHashMap;
import java.util.Map;




public class ForexController {

    @FXML
    TableView<Map.Entry<String, String>> tablazat;


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
}
