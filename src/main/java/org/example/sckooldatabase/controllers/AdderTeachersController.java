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
import org.example.sckooldatabase.data.Configuration;
import org.example.sckooldatabase.data.Role;
import org.example.sckooldatabase.object.Subject;
import org.example.sckooldatabase.object.Teacher;
import org.example.sckooldatabase.service.SubjectService;
import org.example.sckooldatabase.service.TeacherService;
import org.example.sckooldatabase.utils.WindowUtil;
import org.example.sckooldatabase.viewer.TeacherViewer;

import java.util.List;

public class AdderTeachersController {

    @FXML
    private ListView<Teacher> listViewTeachers;

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

    private TeacherService teacherService;
    private SubjectService subjectService;
    private TeacherViewer teacherViewer;

    public void initialize() {
        teacherService = new TeacherService();
        subjectService = new SubjectService();
        teacherViewer = new TeacherViewer(vBoxData.getChildren());

        List<Teacher> teachers = teacherService.getAllTeachers();
        listViewTeachers.setItems(FXCollections.observableList(teachers));
        List<Subject> subjects = subjectService.getAllSubjects();
        comboBoxSubject.setItems(FXCollections.observableList(subjects));

        listViewTeachers.getSelectionModel().selectedItemProperty().addListener((observableValue, teacher, t1) -> {
            teacherViewer.teacherView(t1);
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
        list.add(list.size() - 1, teacherViewer.getNormolizeComboBox());
    }

    @FXML
    public void deleteSubject(ActionEvent event) {
        int size = vBoxData.getChildren().size();
        if (size > 6) {
            vBoxData.getChildren().remove(size - 2);
        }
    }

    @FXML
    public void deleteTeacher(ActionEvent event) {
        Teacher teacher = listViewTeachers.getSelectionModel().getSelectedItem();
        listViewTeachers.getItems().remove(teacher);
        teacherService.deleteTeacher(teacher.getId());
    }

    @FXML
    public void addTeacher(ActionEvent event) {
        if (save) {
            Teacher teacher = teacherViewer.getTeacher();
            if (teacher == null) {
                teacherViewer.setError("Все поля должны быть заполнены");
                return;
            }
            teacher = teacherService.addTeacher(teacher);
            if (teacher == null) {
                teacherViewer.setError("Ошибка при сохранении");
                return;
            }
            listViewTeachers.getItems().add(teacher);
            teacherViewer.clear();
        } else {
            teacherViewer.clear();
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
        Teacher teacher = teacherViewer.getTeacher();
        Teacher teacherNuw = listViewTeachers.getSelectionModel().getSelectedItem();
        teacher.setId(teacherNuw.getId());
        listViewTeachers.getItems().set(listViewTeachers.getSelectionModel().getSelectedIndex(), teacher);
        teacherService.updateTeacher(teacher);
    }

    @FXML
    void openAdderSubjectWindow(ActionEvent event) {
        WindowUtil.openWindow("fxml/add-subject.fxml", buttonAdd.getScene());
    }

    @FXML
    void openAdderTeacherWindow(ActionEvent event) {

    }

    @FXML
    void openAdderClassroomWindow(ActionEvent event) {
        WindowUtil.openWindow("fxml/add-classroom.fxml", buttonAdd.getScene());
    }

    @FXML
    void openStatisticsWindow(ActionEvent event){
        WindowUtil.openWindow("fxml/statistics.fxml", buttonAdd.getScene());
    }

    @FXML
    void close() {
        buttonAdd.getScene().getWindow().hide();
    }

}
