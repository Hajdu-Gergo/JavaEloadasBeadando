package org.beadando.beadando;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class ParhuzamController {

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    private Thread task1;
    private Thread task2;
    private boolean isRunning = false;

    // Metódus, amely elindítja a párhuzamos feladatokat
    @FXML
    public void startParallelTasks() {
        if (isRunning) {
            return; // Ha már futnak a szálak, akkor ne indítsuk újra
        }

        isRunning = true;

        // Task 1: Módosítás 1 másodpercenként
        task1 = new Thread(() -> {
            int counter1 = 1;
            while (isRunning) {
                String message1 = "1. címke értéke: " + counter1;
                counter1++;
                // A UI szálon frissítjük a label-t
                Platform.runLater(() -> label1.setText(message1));

                try {
                    Thread.sleep(1000);  // 1 másodperces szünet
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Task 2: Módosítás 2 másodpercenként
        task2 = new Thread(() -> {
            int counter2 = 1;
            while (isRunning) {
                String message2 = "2. címke értéke: " + counter2;
                counter2++;
                // A UI szálon frissítjük a label-t
                Platform.runLater(() -> label2.setText(message2));

                try {
                    Thread.sleep(2000);  // 2 másodperces szünet
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Indítjuk a két szálat
        task1.start();
        task2.start();

        // Leállító gomb engedélyezése
        stopButton.setDisable(false);
        startButton.setDisable(true);
    }

    // Metódus a folyamat leállításához
    @FXML
    public void stopParallelTasks() {
        isRunning = false; // A szálaknak jelezzük, hogy álljanak le

        // A szálak leállítása
        task1.interrupt();
        task2.interrupt();

        // Nullázzuk le a label értékeit
        Platform.runLater(() -> {
            label1.setText("1. címke - 1 másodpercenként fog frissíteni");
            label2.setText("2. címke - 2 másodpercenként fog frissíteni");
        });

        // Gombok visszaállítása
        startButton.setDisable(false);
        stopButton.setDisable(true);
    }
}