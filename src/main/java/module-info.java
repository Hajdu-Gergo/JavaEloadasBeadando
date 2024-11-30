module org.beadando.beadando {
    requires javafx.controls;
    requires javafx.fxml;

    requires gson;
    requires httpcore;
    requires httpclient;

    requires org.kordamp.bootstrapfx.core;

    requires java.xml;
    requires java.sql;
    requires v20;


    opens org.beadando.beadando to javafx.fxml;





    exports org.beadando.beadando;

}