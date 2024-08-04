package org.example.sckooldatabase.viewer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.sckooldatabase.object.Subject;
import org.example.sckooldatabase.object.Classroom;
import org.example.sckooldatabase.service.SubjectService;

import java.util.ArrayList;
import java.util.List;

public class ClassroomViewer {
    private SubjectService subjectService;
    private ObservableList<Node> classroomData;
    private ObservableList<Subject> allSubject;
    private TextField textFieldNumber;
    private TextField textFieldFloor;
    private TextField textFieldNumberSeat;

    public ClassroomViewer(ObservableList<Node> classroomData) {
        this.classroomData = classroomData;
        this.textFieldNumber = (TextField) classroomData.get(0);
        this.textFieldFloor = (TextField) classroomData.get(1);
        this.textFieldNumberSeat = (TextField) classroomData.get(2);
        subjectService = new SubjectService();
        allSubject = FXCollections.observableList(subjectService.getAllSubjects());
    }

    public void setError(String error) {
        textFieldNumber.setText(null);
        textFieldNumber.setPromptText(error);
    }

    public Classroom getClassroom() {
        if (!isValid()) {
            return null;
        }
        Classroom classroom = new Classroom(
                Integer.parseInt(textFieldNumber.getText()),
                Integer.parseInt(textFieldFloor.getText()),
                Integer.parseInt(textFieldNumberSeat.getText())
        );
        classroom.setSubjects(getSubjects());
        return classroom;
    }

    public void classroomView(Classroom classroom) {
        if (classroom == null) {
            clear();
        }
        textFieldNumber.setText(String.valueOf(classroom.getNumber()));
        textFieldFloor.setText(String.valueOf(classroom.getFloor()));
        textFieldNumberSeat.setText(String.valueOf(classroom.getNumberSeat()));
        setSubject(classroom.getSubjects());
    }

    private void setSubject(List<Subject> subjects) {
        deleteComboBox();
        for (Subject subject : subjects) {
            classroomData.add(classroomData.size() - 1, getNormolizeComboBox(subject));
        }
    }

    private List<Subject> getSubjects() {
        List<Subject> subjects = new ArrayList<>();
        for (int i = 3; i < classroomData.size() - 1; i++) {
            if (classroomData.get(i) instanceof ComboBox<?>) {
                ComboBox<Subject> subjectComboBox = (ComboBox<Subject>) classroomData.get(i);
                subjects.add(subjectComboBox.getValue());
            }
        }
        return subjects;
    }

    private void deleteComboBox() {
        classroomData.remove(3, classroomData.size() - 1);
    }

    public void clear() {
        textFieldNumber.setText(null);
        textFieldFloor.setText(null);
        textFieldNumberSeat.setText(null);
        textFieldNumber.setPromptText(null);
        deleteComboBox();
        classroomData.add(classroomData.size() - 1, getNormolizeComboBox());
    }

    public ComboBox<Subject> getNormolizeComboBox() {
        ComboBox<Subject> subjectComboBox = new ComboBox<Subject>();
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
        if (!isValid(textFieldNumber.getText())) {
            return false;
        }
        if (!isValid(textFieldFloor.getText())) {
            return false;
        }
        if (!isValid(textFieldNumberSeat.getText())) {
            return false;
        }
        try {
            Integer.parseInt(textFieldNumber.getText());
            Integer.parseInt(textFieldFloor.getText());
            Integer.parseInt(textFieldNumberSeat.getText());
        } catch (NumberFormatException e) {
            setError("Все поля должны быть числами");
            return false;
        }
        for (int i = 3; i < classroomData.size() - 1; i++) {
            if (classroomData.get(i) instanceof ComboBox<?>) {
                ComboBox<Subject> subjectComboBox = (ComboBox<Subject>) classroomData.get(i);
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
