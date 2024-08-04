package org.example.sckooldatabase.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.example.sckooldatabase.data.Configuration;
import org.example.sckooldatabase.data.Role;
import org.example.sckooldatabase.object.Subject;
import org.example.sckooldatabase.service.SubjectService;
import org.example.sckooldatabase.utils.WindowUtil;
import org.example.sckooldatabase.viewer.SubjectViewer;

import java.util.List;

public class AdderSubjectController {
    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Button buttonDelete;

    @FXML
    private ListView<Subject> listViewSubjects;

    @FXML
    private VBox vBoxData;

    private SubjectService subjectService;
    private SubjectViewer subjectViewer;

    private boolean save;

    @FXML
    public void initialize() {
        subjectService = new SubjectService();
        subjectViewer = new SubjectViewer(vBoxData.getChildren());
        List<Subject> subjects = subjectService.getAllSubjects();
        listViewSubjects.setItems(FXCollections.observableList(subjects));
        listViewSubjects.getSelectionModel().selectedItemProperty().addListener((observableValue, subject, s1) -> {
            subjectViewer.subjectView(s1);
            setSave(false);
        });
        setSave(true);
        if (Configuration.getRole() != Role.ADMIN) {
            vBoxData.setDisable(true);
            buttonAdd.setDisable(true);
            buttonUpdate.setDisable(true);
            buttonDelete.setDisable(true);
            setSave(false);
        }
    }

    @FXML
    void addSubject(ActionEvent event) {
        if (save) {
            Subject subject = subjectViewer.getSubject();
            if (subject == null) {
                subjectViewer.setError("Все поля должны быть заполнены");
                return;
            }
            subject = subjectService.addSubject(subject);
            if (subject == null) {
                subjectViewer.setError("Ошибка при сохранении");
                return;
            }
            listViewSubjects.getItems().add(subject);
            subjectViewer.clear();
        } else {
            subjectViewer.clear();
            setSave(true);
        }
    }

    private void setSave(boolean save) {
        this.save = save;
        if (save) {
            buttonAdd.setText("Сохранить");
        } else {
            buttonAdd.setText("Добавить");
        }
    }

    @FXML
    void deleteSubject(ActionEvent event) {
        Subject subject = listViewSubjects.getSelectionModel().getSelectedItem();
        listViewSubjects.getItems().remove(subject);
        subjectService.deleteSubject(subject.getId());
    }

    @FXML
    void updateSubject(ActionEvent event) {
        Subject subject = subjectViewer.getSubject();
        Subject subjectNuw = listViewSubjects.getSelectionModel().getSelectedItem();
        subject.setId(subjectNuw.getId());
        listViewSubjects.getItems().set(listViewSubjects.getSelectionModel().getSelectedIndex(), subject);
        subjectService.updateSubject(subject);
    }

    @FXML
    void openAdderSubjectWindow(ActionEvent event) {

    }

    @FXML
    void openAdderTeacherWindow(ActionEvent event) {
        WindowUtil.openWindow("fxml/add-teacher.fxml", buttonAdd.getScene());
    }

    @FXML
    void openAdderClassroomWindow(ActionEvent event) {
        WindowUtil.openWindow("fxml/add-classroom.fxml", buttonAdd.getScene());
    }

    @FXML
    private void openStatisticsWindow(ActionEvent event) {
        WindowUtil.openWindow("fxml/statistics.fxml", buttonAdd.getScene());
    }

    @FXML
    void close() {
        buttonAdd.getScene().getWindow().hide();
    }
}
