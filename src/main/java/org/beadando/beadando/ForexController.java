package org.beadando.beadando;

import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.instrument.Candlestick;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import com.oanda.v20.primitives.DateTime;
import com.oanda.v20.trade.Trade;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.lang.management.ClassLoadingMXBean;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class ForexController implements Initializable {

    @FXML
    TableView<Map.Entry<String, String>> tablazat;

    @FXML
    TableView<Trade> tabla3;

    @FXML
    ComboBox<String> devizapar;
    @FXML
    ComboBox<String> irany;

    @FXML
    Label kiir;

    @FXML
    DatePicker kezd;

    @FXML
    DatePicker veg;

    @FXML
    TableView<Map.Entry<String, String>> tablazat2;

    @FXML
    LineChart<String, Number> histgraf;

    @FXML
    ComboBox<Integer> menny;
    @FXML
    TextField pozaz;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            devizapar.getItems().addAll("EUR_HUF","USD_HUF","GBP_HUF","EUR_USD", "USD_JPY", "GBP_USD", "USD_CHF", "EUR_GBP", "EUR_JPY", "EUR_CHF", "AUD_USD", "USD_CAD", "NZD_USD");
            devizapar.setValue("EUR_HUF");
        } catch (Exception e) {
            System.out.println("devizapar nem került felöltésre!");
        }
        try {
            irany.getItems().addAll("Vásárlás","Eladás");
            irany.setValue("Vásárlás");
        } catch (Exception e) {
            System.out.println("irany nem került felöltés!");
        }
        try {
            menny.getItems().addAll(100,200,300,400,500,600,700,800,900,1000);
            menny.setValue(100);
        } catch (Exception e) {
            System.out.println("menny nem került felöltés!");
        }
        try {
            TableColumn<Trade, String> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));

            // Oszlop létrehozása az instrumenthez
            TableColumn<Trade, String> instrumentColumn = new TableColumn<>("Instrument");
            instrumentColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty());

            // Oszlop létrehozása az árhoz
            TableColumn<Trade, Double> priceColumn = new TableColumn<>("Price");
            priceColumn.setCellValueFactory(cellData ->
                    new SimpleDoubleProperty().asObject());

            // Oszlop létrehozása a nyitási időhöz
            TableColumn<Trade, String> openTimeColumn = new TableColumn<>("Open Time");
            openTimeColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getOpenTime().toString()));

            // Oszlopok hozzáadása a táblázathoz
            tabla3.getColumns().addAll(idColumn, instrumentColumn, priceColumn, openTimeColumn);
        } catch (Exception e) {
            System.out.println("Táblázat nem került feltöltésre!");
        }


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

        /*System.out.println("Base currency: " + baseCurrency);
        System.out.println("Quote currency: " + quoteCurrency);
        System.out.println("Price: " + price);*/
    }
    private static String extractValue(String data, String startToken, String endToken) {
        int startIndex = data.indexOf(startToken) + startToken.length();
        int endIndex = data.indexOf(endToken, startIndex);
        return data.substring(startIndex, endIndex).trim();
    }

    public void lekerdezHistorikus(ActionEvent actionEvent) {
        InstrumentCandlesResponse response = Oanda.getHistoricalData(devizapar.getValue(), new DateTime(kezd.getValue().toString()), new DateTime(veg.getValue().toString()));
        Map<String, String> map = new LinkedHashMap<>();
        for(Candlestick candle: response.getCandles())
            map.put(candle.getTime().toString(), candle.getMid().getC().toString());
        ObservableList<Map.Entry<String, String>> data = FXCollections.observableArrayList(map.entrySet());
        if (tablazat2 == null) {
            System.err.println("Táblázat nem létezik!");
            return;
        }
        if(tablazat2.getColumns().isEmpty()) {
            TableColumn<Map.Entry<String, String>, String> timeColumn = new TableColumn<>("time");
            timeColumn.setCellValueFactory(data2 -> new SimpleStringProperty(data2.getValue().getKey()));
            timeColumn.setText("Idő");

            TableColumn<Map.Entry<String, String>, String> priceColumn = new TableColumn<>("price");
            priceColumn.setCellValueFactory(data2 -> new SimpleStringProperty(data2.getValue().getValue()));
            priceColumn.setText("Árfolyam");
            tablazat2.getColumns().add(timeColumn);
            tablazat2.getColumns().add(priceColumn);
        }
        tablazat2.getItems().clear();
        tablazat2.getItems().addAll(data);
        tablazat2.refresh();
        grafkirajzol(data);
    }
    public void grafkirajzol(ObservableList<Map.Entry<String, String>>  map) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        System.out.println("Grafikon kirajzolása...");
        for (Map.Entry<String, String> entry : map) {
            String xValue = entry.getKey();  // X tengely értéke
            double yValue = Double.parseDouble(entry.getValue());  // Y tengely értéke
            series.getData().add(new XYChart.Data<>(xValue, yValue));
        }

        histgraf.getData().add(series);
    }

    public void poznyitas(ActionEvent actionEvent) throws ExecuteException, RequestException {
        Integer direction = irany.getValue().equals("Vásárlás") ? 1 : -1;
        Oanda.openTrade(devizapar.getValue(), menny.getValue() * direction);
    }

    public void pozzar(ActionEvent actionEvent) {
        System.out.println("Pozició zárása...");
        Oanda.closeTrade(pozaz.getText());
    }

    public void frissites(ActionEvent actionEvent) {
        ObservableList<Trade> tradeData = FXCollections.observableArrayList(Oanda.getTrades());
        tabla3.setItems(tradeData);
    }
}
