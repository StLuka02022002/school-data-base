package org.example.sckooldatabase.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.example.sckooldatabase.data.Configuration;
import org.example.sckooldatabase.data.Role;
import org.example.sckooldatabase.object.Classroom;
import org.example.sckooldatabase.object.Subject;
import org.example.sckooldatabase.service.ClassroomService;
import org.example.sckooldatabase.service.SubjectService;
import org.example.sckooldatabase.utils.WindowUtil;
import org.example.sckooldatabase.viewer.ClassroomViewer;

import java.util.List;

public class AdderClassroomController {

    @FXML
    private ListView<Classroom> listViewClassrooms;

    @FXML
    private ComboBox<Subject> comboBoxSubject;

    @FXML
    private VBox vBoxData;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Button buttonDelete;

    private boolean save;

    private ClassroomService classroomService;
    private SubjectService subjectService;
    private ClassroomViewer classroomViewer;

    public void initialize() {
        classroomService = new ClassroomService();
        subjectService = new SubjectService();
        classroomViewer = new ClassroomViewer(vBoxData.getChildren());

        List<Classroom> teachers = classroomService.getAllClassrooms();
        listViewClassrooms.setItems(FXCollections.observableList(teachers));
        List<Subject> subjects = subjectService.getAllSubjects();
        comboBoxSubject.setItems(FXCollections.observableList(subjects));

        listViewClassrooms.getSelectionModel().selectedItemProperty().addListener((observableValue, classroom, c1) -> {
            classroomViewer.classroomView(c1);
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
    public void addSubject(ActionEvent event) {
        List<Node> list = vBoxData.getChildren();
        list.add(list.size() - 1, classroomViewer.getNormolizeComboBox());
    }

    @FXML
    public void deleteSubject(ActionEvent event) {
        int size = vBoxData.getChildren().size();
        if (size > 6) {
            vBoxData.getChildren().remove(size - 2);
        }
    }

    @FXML
    public void deleteClassroom(ActionEvent event) {
        Classroom classroom = listViewClassrooms.getSelectionModel().getSelectedItem();
        listViewClassrooms.getItems().remove(classroom);
        classroomService.deleteClassroom(classroom.getId());
    }

    @FXML
    public void addClassroom(ActionEvent event) {
        if (save) {
            Classroom classroom = classroomViewer.getClassroom();
            if (classroom == null) {
                classroomViewer.setError("Все поля должны быть заполнены");
                return;
            }
            classroom = classroomService.addClassroom(classroom);
            if (classroom == null) {
                classroomViewer.setError("Ошибка при сохранении");
                return;
            }
            listViewClassrooms.getItems().add(classroom);
            classroomViewer.clear();
        } else {
            classroomViewer.clear();
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
    public void updateTeacher(ActionEvent event) {
        Classroom classroom = classroomViewer.getClassroom();
        Classroom classroomNuw = listViewClassrooms.getSelectionModel().getSelectedItem();
        classroom.setId(classroomNuw.getId());
        listViewClassrooms.getItems().set(listViewClassrooms.getSelectionModel().getSelectedIndex(), classroom);
        classroomService.updateClassroom(classroom);
    }

    @FXML
    void openAdderSubjectWindow(ActionEvent event) {
        WindowUtil.openWindow("fxml/add-subject.fxml", buttonAdd.getScene());
    }

    @FXML
    void openAdderTeacherWindow(ActionEvent event) {
        WindowUtil.openWindow("fxml/add-teacher.fxml", buttonAdd.getScene());
    }

    @FXML
    void openAdderClassroomWindow(ActionEvent event){

    }

    @FXML void openStatisticsWindow(ActionEvent event){
        WindowUtil.openWindow("fxml/statistics.fxml", buttonAdd.getScene());
    }

    @FXML
    void close(){
        buttonAdd.getScene().getWindow().hide();
    }
}
