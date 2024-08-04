package org.example.sckooldatabase.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.sckooldatabase.Main;
import org.example.sckooldatabase.database.DataBase;

import java.io.IOException;

public class WindowUtil {

    public static void openWindow(String fxml, Scene scene) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
            Parent homePage = loader.load();
            Stage stage = (Stage) scene.getWindow();
            stage.setScene(new Scene(homePage));
            stage.centerOnScreen();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openNewWindow(String fxml, Scene scene){
        try {
            FXMLLoader loader =new FXMLLoader(Main.class.getResource(fxml));
            Parent homePage = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(homePage));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void close(){
        DataBase.disconnect();
        Platform.exit();
        System.exit(0);
    }
}
