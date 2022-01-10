module com.example.proiect {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;
    requires org.junit.jupiter.api;
    requires junit;


    opens com.proiect to javafx.fxml;
    exports com.proiect;
}