module org.beadando.beadando {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.xml;

    opens org.beadando.beadando to javafx.fxml;
    exports org.beadando.beadando;
}