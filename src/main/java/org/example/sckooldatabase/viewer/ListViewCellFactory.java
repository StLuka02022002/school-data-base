package org.example.sckooldatabase.viewer;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.example.sckooldatabase.Main;
import org.example.sckooldatabase.controllers.CellViewController;
import org.example.sckooldatabase.data.Configuration;
import org.example.sckooldatabase.data.Role;
import org.example.sckooldatabase.object.Lesson;

import java.io.IOException;

public class ListViewCellFactory implements Callback<ListView<Lesson>, ListCell<Lesson>> {

    @Override
    public ListCell<Lesson> call(ListView<Lesson> listView) {
        return new ListCell<Lesson>() {
            private final HBox cellLayout;
            private final CellViewController cellController;
            {
                try {
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/cell-view.fxml"));
                    cellLayout = loader.load();
                    cellController = loader.getController();
                    listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                        boolean isSelected = newSelection == getItem();
                        cellController.setActiveButton(isSelected);
                    });
                    cellController.setListView(listView);
                    if(Configuration.getRole()!= Role.ADMIN){
                        cellLayout.setDisable(true);
                    }
                } catch (IOException e) {

                    throw new RuntimeException(e);
                }
            }

            @Override
            protected void updateItem(Lesson item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(cellLayout);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    if(item.getId() == null){
                        cellController.setNewLesson(item);
                    }else {
                        cellController.setLesson(item);
                    }
                }
            }
        };
    }
}

