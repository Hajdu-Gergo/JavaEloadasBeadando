package org.beadando.beadando;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class Olvas2Controller
{
    @FXML
    private TableView<GP> tableView;
    @FXML
    private TableColumn<GP, String> colDatum;
    @FXML
    private TableColumn<GP, String> colNev;
    @FXML
    private TableColumn<GP, String> colHelyszin;
    @FXML
    private TableView<GP> gpTableView;

    @FXML
    private ComboBox<String> tableSelectComboBox;

    @FXML
    private TableColumn<GP, String> gpColDatum; // Hozzáadva a TableColumn
    @FXML
    private TableColumn<GP, String> gpColNev;   // Hozzáadva a TableColumn
    @FXML
    private TableColumn<GP, String> gpColHelyszin; // Hozzáadva a TableColumn

    @FXML
    private TableView<Pilota> pilotaTableView;
    @FXML
    private TableColumn<Pilota, String> pilotaColNev;
    @FXML
    private TableColumn<Pilota, String> pilotaColNem;
    @FXML
    private TableColumn<Pilota, String> pilotaColSzuldat;
    @FXML
    private TableColumn<Pilota, String> pilotaColNemzet;
    @FXML
    private TableView<Eredmeny> eredmenyTableView;

    @FXML
    private TableColumn<Eredmeny, String> eredmenyColDatum;
    @FXML
    private TableColumn<Eredmeny, Integer> eredmenyColPilotaaz;
    @FXML
    private TableColumn<Eredmeny, Integer> eredmenyColHelyezes;

    @FXML
    private TableColumn<Eredmeny, String> eredmenyColHiba;
    @FXML
    private TableColumn<Eredmeny, String> eredmenyColCsapat;
    @FXML
    private TableColumn<Eredmeny, String> eredmenyColTipus;
    @FXML
    private TableColumn<Eredmeny, String> eredmenyColMotor;


    @FXML
    private ComboBox<String> deleteComboBox;


    @FXML
    private DatePicker filterDate;  // Új DatePicker vezérlő

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

    private ObservableList<GP> gpList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // gp tábla oszlopainak beállítása
        gpColDatum.setCellValueFactory(new PropertyValueFactory<>("datum"));
        gpColNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        gpColHelyszin.setCellValueFactory(new PropertyValueFactory<>("helyszin"));

        loadGPData();
        loadFilterOptions(); // Szűrőelemek feltöltése

        // ToggleGroup beállítása
        sortGroup = new ToggleGroup();
        sortByDate.setToggleGroup(sortGroup);
        sortByName.setToggleGroup(sortGroup);
    }

    // Betöltjük a gp tábla adatait
    private void loadGPData() {
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT datum, nev, helyszin FROM gp")) {

            ObservableList<GP> gpList = FXCollections.observableArrayList();
            while (rs.next()) {
                gpList.add(new GP(rs.getString("datum"), rs.getString("nev"), rs.getString("helyszin")));
            }
            gpTableView.setItems(gpList);  // A GP táblázat frissítése
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Szűrőelemek feltöltése
    private void loadFilterOptions() {
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT helyszin FROM gp")) {

            ObservableList<String> locations = FXCollections.observableArrayList();
            while (rs.next()) {
                locations.add(rs.getString("helyszin"));
            }
            filterLocation.setItems(locations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Szűrési logika a GP táblára
    @FXML
    private void handleFilter() {
        String location = filterLocation.getValue(); // Szűrés helyszín alapján
        boolean recentOnly = filterRecent.isSelected(); // Csak az utolsó 10 adat megjelenítése
        boolean sortByDateSelected = sortByDate.isSelected(); // Dátum szerinti rendezés

        // Dátum formázása az adatbázis formátumához
        String datum = "";
        if (filterDate.getValue() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            datum = filterDate.getValue().format(formatter);
        }

        // Alap SQL lekérdezés
        String query = "SELECT datum, nev, helyszin FROM gp WHERE 1=1";

        // Szűrési feltételek hozzáadása
        if (!datum.isEmpty()) {
            query += " AND datum = '" + datum + "'";
        }
        if (location != null && !location.isEmpty()) {
            query += " AND helyszin = '" + location + "'";
        }
        if (recentOnly) {
            query += " ORDER BY datum DESC LIMIT 10";
        } else if (sortByDateSelected) {
            query += " ORDER BY datum";
        } else {
            query += " ORDER BY nev";
        }

        // GP lista inicializálása
        ObservableList<GP> filteredGPList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Adatok beolvasása a GP táblából
            while (rs.next()) {
                filteredGPList.add(new GP(
                        rs.getString("datum"),
                        rs.getString("nev"),
                        rs.getString("helyszin")
                ));
            }

            // Táblázat frissítése a szűrt adatokkal
            gpTableView.setItems(filteredGPList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
