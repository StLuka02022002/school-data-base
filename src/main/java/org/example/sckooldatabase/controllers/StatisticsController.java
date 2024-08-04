package org.example.sckooldatabase.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.sckooldatabase.object.Subject;
import org.example.sckooldatabase.object.Teacher;
import org.example.sckooldatabase.service.LessonDetailsService;
import org.example.sckooldatabase.service.ReplacementService;
import org.example.sckooldatabase.service.SubjectService;
import org.example.sckooldatabase.service.TeacherService;
import org.example.sckooldatabase.utils.WindowUtil;

public class StatisticsController {
    @FXML
    private ListView<Subject> listViewSubject;

    @FXML
    private ListView<Teacher> listViewTeachers;

    @FXML
    private VBox vBoxDataSubjects;

    @FXML
    private VBox vBoxDataTeachers;

    private TeacherService teacherService;
    private SubjectService subjectService;
    private LessonDetailsService lessonDetailsService;
    private ReplacementService replacementService;

    public void initialize() {
        teacherService = new TeacherService();
        subjectService = new SubjectService();
        lessonDetailsService = new LessonDetailsService();
        replacementService = new ReplacementService();
        listViewTeachers.getItems().addAll(teacherService.getAllTeachers());
        listViewSubject.getItems().addAll(subjectService.getAllSubjects());
        listViewTeachers.getSelectionModel().selectedItemProperty().addListener((observableValue, teacher, t1) -> {
            setDataTeacher(vBoxDataTeachers.getChildren(), t1);
        });
        listViewSubject.getSelectionModel().selectedItemProperty().addListener((observableValue, subject, s1) -> {
            setDataSubject(vBoxDataSubjects.getChildren(), s1);
        });
    }

    private void setDataTeacher(ObservableList<Node> teacherData, Teacher teacher) {
        TextField textFieldLastName = (TextField) teacherData.get(0);
        TextField textFieldFirstName = (TextField) teacherData.get(1);
        TextField textFieldThreeName = (TextField) teacherData.get(2);
        TextField textFieldCountClass = (TextField) teacherData.get(3);
        TextField textFieldCountReplacement = (TextField) teacherData.get(4);
        textFieldLastName.setText(teacher.getLastName());
        textFieldFirstName.setText(teacher.getFirstName());
        textFieldThreeName.setText(teacher.getThreeName());
        textFieldCountClass.setText(String.valueOf(lessonDetailsService.getLessonCountByTeacher(teacher)));
        textFieldCountReplacement.setText(String.valueOf(replacementService.getReplacementCountByTeacherTo(teacher)));
    }

    private void setDataSubject(ObservableList<Node> teacherData, Subject subject) {
        TextField textFieldName = (TextField) teacherData.get(0);
        TextField textFieldCountClass = (TextField) teacherData.get(1);
        TextField textFieldExam = (TextField) teacherData.get(2);
        textFieldName.setText(subject.getName());
        textFieldCountClass.setText(String.valueOf(lessonDetailsService.getClassCountBySubject(subject)));
        textFieldExam.setText(String.valueOf(lessonDetailsService.getExamCountBySubject(subject)));
    }

    @FXML
    void close(ActionEvent event) {
        listViewSubject.getScene().getWindow().hide();
    }

    @FXML
    void openAdderClassroomWindow(ActionEvent event) {
        WindowUtil.openWindow("fxml/add-classroom.fxml", listViewSubject.getScene());
    }

    @FXML
    void openAdderSubjectWindow(ActionEvent event) {
        WindowUtil.openWindow("fxml/add-subject.fxml", listViewSubject.getScene());
    }

    @FXML
    void openAdderTeacherWindow(ActionEvent event) {
        WindowUtil.openWindow("fxml/add-teacher.fxml", listViewTeachers.getScene());
    }

    @FXML
    void openStatisticsWindow(ActionEvent event){

    }

}
