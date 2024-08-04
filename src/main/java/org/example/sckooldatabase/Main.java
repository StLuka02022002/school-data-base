package org.example.sckooldatabase;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.sckooldatabase.data.Configuration;
import org.example.sckooldatabase.database.DataBase;
import org.example.sckooldatabase.utils.WindowUtil;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Configuration.init();
        DataBase.initialTable();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/login-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("SchoolDataBase!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnHidden((event) -> {
            WindowUtil.close();
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}