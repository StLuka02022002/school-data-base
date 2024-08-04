module org.example.sckooldatabase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens org.example.sckooldatabase to javafx.fxml;
    exports org.example.sckooldatabase;
    exports org.example.sckooldatabase.controllers;
    opens org.example.sckooldatabase.controllers to javafx.fxml;
    exports org.example.sckooldatabase.database;
    opens org.example.sckooldatabase.database to javafx.fxml;
}