package org.beadando.beadando;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class OlvasController
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

        // pilota tábla oszlopainak beállítása
        pilotaColNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        pilotaColNem.setCellValueFactory(new PropertyValueFactory<>("nem"));
        pilotaColSzuldat.setCellValueFactory(new PropertyValueFactory<>("szuldat"));
        pilotaColNemzet.setCellValueFactory(new PropertyValueFactory<>("nemzet"));

        // eredmeny tábla oszlopainak beállítása
        eredmenyColDatum.setCellValueFactory(new PropertyValueFactory<>("datum"));
        eredmenyColPilotaaz.setCellValueFactory(new PropertyValueFactory<>("pilotaaz"));
        eredmenyColHelyezes.setCellValueFactory(new PropertyValueFactory<>("helyezes"));
        eredmenyColHiba.setCellValueFactory(new PropertyValueFactory<>("hiba"));
        eredmenyColCsapat.setCellValueFactory(new PropertyValueFactory<>("csapat"));
        eredmenyColTipus.setCellValueFactory(new PropertyValueFactory<>("tipus"));
        eredmenyColMotor.setCellValueFactory(new PropertyValueFactory<>("motor"));


        loadGPData();
        loadPilotaData();
        loadEredmenyData();
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


    // Betöltjük a pilota tábla adatait
    private void loadPilotaData() {
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nev, nem, szuldat, nemzet FROM pilota")) {

            ObservableList<Pilota> pilotaList = FXCollections.observableArrayList();
            while (rs.next()) {
                pilotaList.add(new Pilota(rs.getString("nev"), rs.getString("nem"), rs.getString("szuldat"), rs.getString("nemzet")));
            }
            pilotaTableView.setItems(pilotaList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Betöltjük az eredmeny tábla adatait
    private void loadEredmenyData() {
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             // Lekérdezzük az összes oszlopot
             ResultSet rs = stmt.executeQuery("SELECT datum, pilotaaz, helyezes, hiba, csapat, tipus, motor FROM eredmeny")) {

            ObservableList<Eredmeny> eredmenyList = FXCollections.observableArrayList();
            while (rs.next()) {
                // Az összes mezőt beolvassuk és hozzárendeljük az Eredmeny objektumhoz
                eredmenyList.add(new Eredmeny(
                        rs.getString("datum"),
                        rs.getInt("pilotaaz"),
                        rs.getInt("helyezes"),
                        rs.getString("hiba"),
                        rs.getString("csapat"),
                        rs.getString("tipus"),
                        rs.getString("motor")
                ));
            }
            eredmenyTableView.setItems(eredmenyList);  // Frissítjük a TableView-t
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
