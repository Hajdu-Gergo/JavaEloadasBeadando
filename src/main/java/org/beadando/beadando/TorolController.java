package org.beadando.beadando;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class TorolController
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
        loadDeleteOptions(); // Rekordok törléséhez szükséges lehetőségek betöltése
    }

    private void loadDeleteOptions() {
        ObservableList<String> options = FXCollections.observableArrayList();
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nev, datum FROM gp ORDER BY datum DESC")) {  // Csak azokat az elemeket töltjük be, amik elérhetőek

            while (rs.next()) {
                String record = rs.getString("nev") + " - " + rs.getString("datum");
                options.add(record);  // A törléshez szükséges rekordok betöltése
            }
            deleteComboBox.setItems(options);  // ComboBox frissítése
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @FXML
    private void handleDeleteRecord() {
        String selectedRecord = deleteComboBox.getValue();
        if (selectedRecord == null || !selectedRecord.contains(" - ")) {
            System.out.println("Nincs megfelelő rekord kiválasztva!");
            return;
        }

        // Az azonosító helyett a 'nev' és 'datum' mezőkre alapozunk
        String[] parts = selectedRecord.split(" - ");
        if (parts.length < 2) {
            System.out.println("Érvénytelen formátumú rekord!");
            return;
        }

        String nev = parts[0].trim();
        String datum = parts[1].trim();

        String query = "DELETE FROM gp WHERE nev = ? AND datum = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nev);
            stmt.setString(2, datum);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Sikeresen törölve a rekord!");
                loadGPData();  // Frissítjük az adatokat a táblázatban
                loadDeleteOptions();  // Frissítjük a törléshez használható opciókat
            } else {
                System.out.println("Hiba történt a rekord törlése közben.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
