package org.beadando.beadando;

import org.beadando.database.DatabaseHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;

public class MainController {
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

        // Feltöltjük a ComboBox-ot
        tableSelectComboBox.getItems().addAll("gp", "pilota", "eredmeny");

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
        loadFilterOptions(); // Szűrőelemek feltöltése
        loadModifyOptions(); // Rekord azonosítók betöltése
        loadDeleteOptions(); // Rekordok törléséhez szükséges lehetőségek betöltése

        // ToggleGroup beállítása
        sortGroup = new ToggleGroup();
        sortByDate.setToggleGroup(sortGroup);
        sortByName.setToggleGroup(sortGroup);
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



    private void loadModifyOptions() {
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nev, datum FROM gp ORDER BY datum DESC")) {  // Csak azokat az elemeket töltjük be, amik elérhetőek

            ObservableList<String> records = FXCollections.observableArrayList();
            while (rs.next()) {
                records.add(rs.getString("nev") + " - " + rs.getString("datum"));
            }
            modifyComboBox.setItems(records);  // A módosításhoz használt ComboBox frissítése
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTableSelection() {
        String selectedTable = tableSelectComboBox.getValue();

        // Minden táblát rejtünk el először
        gpTableView.setVisible(false);
        pilotaTableView.setVisible(false);
        eredmenyTableView.setVisible(false);

        // A választott táblát jelenítjük meg
        if (selectedTable != null) {
            switch (selectedTable) {
                case "gp":
                    gpTableView.setVisible(true);
                    break;
                case "pilota":
                    pilotaTableView.setVisible(true);
                    break;
                case "eredmeny":
                    eredmenyTableView.setVisible(true);
                    break;
            }
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

    // Handle Olvas
    @FXML
    private void handleOlvas() {
        loadGPData();  // Újratöltjük az adatokat a táblázatba
    }

    // Handle Olvas2 - Szűrő lehetőség
    @FXML
    private void handleOlvas2() {
        System.out.println("Olvas2 szűrési lehetőség");
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

    // Handle Ír - Új adat hozzáadása
    @FXML
    private void handleIr() {
        String datum = newDatum.getText().trim();
        String nev = newNev.getText().trim();
        String helyszin = newHelyszin.getText().trim();

        if (datum.isEmpty() || nev.isEmpty() || helyszin.isEmpty()) {
            // Figyelmeztetés, ha valamelyik mező üres
            System.out.println("Minden mezőt ki kell tölteni!");
            return;
        }

        String query = "INSERT INTO gp (datum, nev, helyszin) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, datum);
            stmt.setString(2, nev);
            stmt.setString(3, helyszin);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Sikeresen hozzáadva az új rekord!");
                loadGPData();  // Frissítjük az adatokat a táblázatban
            } else {
                System.out.println("Hiba történt a rekord hozzáadása közben.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddRecord() {
        String datum = (newDatePicker.getValue() != null)
                ? newDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                : null;
        String nev = newNev.getText().trim();
        String helyszin = newHelyszin.getText().trim();

        if (datum == null || nev.isEmpty() || helyszin.isEmpty()) {
            // Figyelmeztetés, ha valamelyik mező üres
            System.out.println("Minden mezőt ki kell tölteni!");
            return;
        }

        String query = "INSERT INTO gp (datum, nev, helyszin) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, datum);
            stmt.setString(2, nev);
            stmt.setString(3, helyszin);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Sikeresen hozzáadva az új rekord!");
                loadGPData();  // Frissítjük az adatokat a táblázatban
                loadDeleteOptions();  // Frissítjük a törléshez használható opciókat
                loadModifyOptions();  // Frissítjük a módosításhoz használható opciókat
            } else {
                System.out.println("Hiba történt a rekord hozzáadása közben.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void handleModifyRecord() {
        String selectedRecord = modifyComboBox.getValue();
        if (selectedRecord == null) {
            System.out.println("Nincs rekord kiválasztva!");
            return;
        }

        // Az azonosító helyett most a 'nev' és 'datum' alapján dolgozunk
        String[] parts = selectedRecord.split(" - ");
        String nev = parts[0];
        String datum = parts[1];

        String modifiedDatum = (modifyDatePicker.getValue() != null)
                ? modifyDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                : null;
        String modifiedNev = modifyNev.getText().trim();
        String modifiedHelyszin = modifyHelyszin.getText().trim();

        if (modifiedDatum == null || modifiedNev.isEmpty() || modifiedHelyszin.isEmpty()) {
            System.out.println("Minden mezőt ki kell tölteni!");
            return;
        }

        String query = "UPDATE gp SET datum = ?, nev = ?, helyszin = ? WHERE nev = ? AND datum = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, modifiedDatum);
            stmt.setString(2, modifiedNev);
            stmt.setString(3, modifiedHelyszin);
            stmt.setString(4, nev);  // Kiválasztott rekord neve
            stmt.setString(5, datum);  // Kiválasztott rekord dátuma

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Sikeresen módosítva a rekord!");
                loadGPData();  // Frissítjük a GP táblázatot
                loadDeleteOptions();  // Frissítjük a törléshez használt ComboBox-ot
                loadModifyOptions();  // Frissítjük a módosításhoz használt ComboBox-ot
            } else {
                System.out.println("Hiba történt a rekord módosítása közben.");
            }

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
                loadModifyOptions();  // Frissítjük a módosításhoz használható opciókat
            } else {
                System.out.println("Hiba történt a rekord törlése közben.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFormula1CRUD() {
        // Itt hívhatod meg a már meglévő alkalmazásod, amely megjeleníti a Formula-1 CRUD felületet
        System.out.println("Formula-1 CRUD ablak megjelenítése");
        // A kívánt alkalmazás ablakának megnyitásához implementáld itt a szükséges logikát
    }

}