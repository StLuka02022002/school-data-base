package org.example.sckooldatabase.viewer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.example.sckooldatabase.object.Subject;
import org.example.sckooldatabase.object.Teacher;
import org.example.sckooldatabase.service.SubjectService;

import java.util.ArrayList;
import java.util.List;

public class TeacherViewer {
    private SubjectService subjectService;
    private ObservableList<Node> teacherData;
    private ObservableList<Subject> allSubject;
    private TextField textFieldFirstName;
    private TextField textFieldLastName;
    private TextField textFieldThreeName;
    private TextField textFieldRank;
    private DatePicker datePickerBirthday;

    public TeacherViewer(ObservableList<Node> teacherData) {
        this.teacherData = teacherData;
        this.textFieldLastName = (TextField) teacherData.get(0);
        this.textFieldFirstName = (TextField) teacherData.get(1);
        this.textFieldThreeName = (TextField) teacherData.get(2);
        this.datePickerBirthday = (DatePicker) teacherData.get(3);
        this.textFieldRank = (TextField) teacherData.get(4);
        subjectService = new SubjectService();
        allSubject = FXCollections.observableList(subjectService.getAllSubjects());
    }

    public void setError(String error) {
        textFieldLastName.setText(null);
        textFieldLastName.setPromptText(error);
    }

    public Teacher getTeacher() {
        if (!isValid()) {
            return null;
        }
        Teacher teacher = new Teacher(
                textFieldFirstName.getText(),
                textFieldLastName.getText(),
                textFieldThreeName.getText(),
                datePickerBirthday.getValue()
        );
        teacher.setRank(Integer.parseInt(textFieldRank.getText()));
        teacher.setSubjects(getSubjects());
        return teacher;
    }

    public void teacherView(Teacher teacher) {
        if (teacher == null) {
            clear();
        }
        textFieldFirstName.setText(teacher.getFirstName());
        textFieldLastName.setText(teacher.getLastName());
        textFieldThreeName.setText(teacher.getThreeName());
        textFieldRank.setText(String.valueOf(teacher.getRank()));
        datePickerBirthday.setValue(teacher.getBirthday());
        setSubject(teacher.getSubjects());
    }

    private void setSubject(List<Subject> subjects) {
        deleteComboBox();
        for (Subject subject : subjects) {
            teacherData.add(teacherData.size() - 1, getNormolizeComboBox(subject));
        }
    }

    private List<Subject> getSubjects() {
        List<Subject> subjects = new ArrayList<>();
        for (int i = 5; i < teacherData.size() - 1; i++) {
            if (teacherData.get(i) instanceof ComboBox<?>) {
                ComboBox<Subject> subjectComboBox = (ComboBox<Subject>) teacherData.get(i);
                subjects.add(subjectComboBox.getValue());
            }
        }
        return subjects;
    }

    private void deleteComboBox() {
        teacherData.remove(5, teacherData.size() - 1);
    }

    public void clear() {
        textFieldFirstName.setText(null);
        textFieldLastName.setText(null);
        textFieldThreeName.setText(null);
        textFieldRank.setText(null);
        datePickerBirthday.setValue(null);
        textFieldLastName.setPromptText(null);
        deleteComboBox();
        teacherData.add(teacherData.size() - 1, getNormolizeComboBox());
    }

    public ComboBox<Subject> getNormolizeComboBox() {
        ComboBox<Subject> subjectComboBox = new ComboBox<>();
        subjectComboBox.setPrefWidth(350);
        subjectComboBox.setPrefHeight(42);
        subjectComboBox.setItems(allSubject);
        return subjectComboBox;
    }

    private ComboBox<Subject> getNormolizeComboBox(Subject subject) {
        ComboBox<Subject> subjectComboBox = getNormolizeComboBox();
        subjectComboBox.setValue(subject);
        return subjectComboBox;
    }

    public boolean isValid() {
        if (!isValid(textFieldFirstName.getText())) {
            return false;
        }
        if (!isValid(textFieldLastName.getText())) {
            return false;
        }
        if (!isValid(textFieldThreeName.getText())) {
            return false;
        }
        if (!isValid(textFieldRank.getText())) {
            return false;
        }
        try {
            Integer.parseInt(textFieldRank.getText());
        } catch (Exception e) {
            setError("Рейтинг должен быть числом");
            return false;
        }
        if (datePickerBirthday.getValue() == null) {
            return false;
        }
        for (int i = 5; i < teacherData.size() - 1; i++) {
            if (teacherData.get(i) instanceof ComboBox<?>) {
                ComboBox<Subject> subjectComboBox = (ComboBox<Subject>) teacherData.get(i);
                if (subjectComboBox.getValue() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(String s) {
        return s != null && !s.equals("");
    }
}
