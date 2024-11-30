package org.beadando.beadando;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class OlvasController {

    @FXML
    private TableView<CombinedRecord> tableView;  // Kombinált rekordok táblázata

    @FXML
    private TableColumn<CombinedRecord, String> colDatum;
    @FXML
    private TableColumn<CombinedRecord, String> colNev;
    @FXML
    private TableColumn<CombinedRecord, String> colHelyszin;
    @FXML
    private TableColumn<CombinedRecord, Integer> colPilotaaz;
    @FXML
    private TableColumn<CombinedRecord, Integer> colHelyezes;
    @FXML
    private TableColumn<CombinedRecord, String> colHiba;
    @FXML
    private TableColumn<CombinedRecord, String> colCsapat;
    @FXML
    private TableColumn<CombinedRecord, String> colTipus;
    @FXML
    private TableColumn<CombinedRecord, String> colMotor;

    @FXML
    private TableColumn<CombinedRecord, String> colPilotaNev;
    @FXML
    private TableColumn<CombinedRecord, String> colPilotaNem;
    @FXML
    private TableColumn<CombinedRecord, String> colPilotaSzuldat;
    @FXML
    private TableColumn<CombinedRecord, String> colPilotaNemzet;

    @FXML
    private ComboBox<String> deleteComboBox;

    @FXML
    private DatePicker filterDate;  // Szűrés dátumra

    @FXML
    private DatePicker newDatePicker; // Új rekordhoz
    @FXML
    private DatePicker modifyDatePicker; // Rekord módosításához

    @FXML
    private TextField filterNev;

    @FXML
    private ComboBox<String> filterLocation;

    @FXML
    private RadioButton sortByDate;

    @FXML
    private RadioButton sortByName;

    @FXML
    private CheckBox filterRecent;

    @FXML
    private TextField newDatum;

    @FXML
    private TextField newNev;

    @FXML
    private TextField newHelyszin;

    @FXML
    private ComboBox<String> modifyComboBox;

    @FXML
    private TextField modifyDatum;

    @FXML
    private TextField modifyNev;

    @FXML
    private TextField modifyHelyszin;

    private ToggleGroup sortGroup; // ToggleGroup létrehozása

    @FXML
    public void initialize() {

        // Kombinált rekordok oszlopainak beállítása
        colDatum.setCellValueFactory(new PropertyValueFactory<>("datum"));
        colNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        colHelyszin.setCellValueFactory(new PropertyValueFactory<>("helyszin"));
        colPilotaaz.setCellValueFactory(new PropertyValueFactory<>("pilotaaz"));
        colHelyezes.setCellValueFactory(new PropertyValueFactory<>("helyezes"));
        colHiba.setCellValueFactory(new PropertyValueFactory<>("hiba"));
        colCsapat.setCellValueFactory(new PropertyValueFactory<>("csapat"));
        colTipus.setCellValueFactory(new PropertyValueFactory<>("tipus"));
        colMotor.setCellValueFactory(new PropertyValueFactory<>("motor"));

        // Pilóta adatok oszlopainak beállítása
        colPilotaNev.setCellValueFactory(new PropertyValueFactory<>("pilotaNev"));
        colPilotaNem.setCellValueFactory(new PropertyValueFactory<>("pilotaNem"));
        colPilotaSzuldat.setCellValueFactory(new PropertyValueFactory<>("pilotaSzuldat"));
        colPilotaNemzet.setCellValueFactory(new PropertyValueFactory<>("pilotaNemzet"));

        // Az adatok betöltése
        loadCombinedData();
    }

    // Betöltjük a kombinált adatokat
    private void loadCombinedData() {
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement()) {

            // Az SQL lekérdezés módosítása a megfelelő kulcsoszlop használatával
            String query = "SELECT gp.datum, gp.nev, gp.helyszin, eredmeny.pilotaaz, eredmeny.helyezes, eredmeny.hiba, " +
                    "eredmeny.csapat, eredmeny.tipus, eredmeny.motor, pilota.nev AS pilotaNev, pilota.nem AS pilotaNem, " +
                    "pilota.szuldat AS pilotaSzuldat, pilota.nemzet AS pilotaNemzet " +
                    "FROM gp " +
                    "LEFT JOIN eredmeny ON gp.datum = eredmeny.datum " +
                    "LEFT JOIN pilota ON eredmeny.pilotaaz = pilota.az";  // Itt az 'az' oszlop szerepel

            ResultSet rs = stmt.executeQuery(query);

            ObservableList<CombinedRecord> combinedList = FXCollections.observableArrayList();

            while (rs.next()) {
                combinedList.add(new CombinedRecord(
                        rs.getString("datum"),
                        rs.getString("nev"),
                        rs.getString("helyszin"),
                        rs.getInt("pilotaaz"),
                        rs.getInt("helyezes"),
                        rs.getString("hiba"),
                        rs.getString("csapat"),
                        rs.getString("tipus"),
                        rs.getString("motor"),
                        rs.getString("pilotaNev"),
                        rs.getString("pilotaNem"),
                        rs.getString("pilotaSzuldat"),
                        rs.getString("pilotaNemzet")
                ));
            }

            tableView.setItems(combinedList);  // A táblázat frissítése
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}