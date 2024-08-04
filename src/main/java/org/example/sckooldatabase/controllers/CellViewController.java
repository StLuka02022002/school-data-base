package org.example.sckooldatabase.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.example.sckooldatabase.data.RoleLesson;
import org.example.sckooldatabase.object.*;
import org.example.sckooldatabase.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class CellViewController {

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonUpdate;

    @FXML
    private ComboBox<Classroom> comboBoxClassroom;

    @FXML
    private ComboBox<RoleLesson> comboBoxRoleLesson;

    @FXML
    private ComboBox<Subject> comboBoxSubject;

    @FXML
    private ComboBox<Teacher> comboBoxTeacher;

    @FXML
    private DatePicker dataPickerDate;

    @FXML
    private HBox hBoxEndTime;

    @FXML
    private HBox hBoxStartTime;

    private boolean save;

    private ClassroomService classroomService;
    private SubjectService subjectService;
    private TeacherService teacherService;
    private LessonService lessonService;
    private TeacherSubjectService teacherSubjectService;
    private ClassroomSubjectService classroomSubjectService;
    private ReplacementService replacementService;

    private Lesson nuwLesson;
    private ListView<Lesson> nuwListView;
    private Replacement replacement;

    @FXML
    public void initialize() {
        classroomService = new ClassroomService();
        subjectService = new SubjectService();
        teacherService = new TeacherService();
        lessonService = new LessonService();
        teacherSubjectService = new TeacherSubjectService();
        classroomSubjectService = new ClassroomSubjectService();
        replacementService = new ReplacementService();
        replacement = new Replacement();

        setComboBoxListener();
        comboBoxSubject.getItems().addAll(subjectService.getAllSubjects());
        comboBoxRoleLesson.getItems().addAll(RoleLesson.values());

        setComboBoxTime(hBoxStartTime);
        setComboBoxTime(hBoxEndTime);
    }

    public void setLesson(Lesson lesson) {
        nuwLesson = lesson;
        if (lesson == null) {
            return;
        }
        if (lesson.getId() == null) {
            return;
        }
        setObservableListFromSubject(lesson.getSubject());
        dataPickerDate.setValue(lesson.getDate());
        setTime(hBoxStartTime, lesson.getTimePeriod().getStartTime());
        setTime(hBoxEndTime, lesson.getTimePeriod().getEndTime());
        comboBoxClassroom.setValue(lesson.getClassroom());
        comboBoxTeacher.setValue(lesson.getTeacher());
        comboBoxRoleLesson.setValue(lesson.getRoleLesson());
        replacement.setLessonId(nuwLesson.getId());
        replacement.setTeacherFrom(lesson.getTeacher());
    }

    public void setListView(ListView listView) {
        nuwListView = listView;
    }

    private void setTime(HBox hBox, LocalTime time) {
        ComboBox<Integer> hour = (ComboBox<Integer>) hBox.getChildren().get(0);
        ComboBox<Integer> minute = (ComboBox<Integer>) hBox.getChildren().get(2);
        if (time == null) {
            hour.setValue(null);
            minute.setValue(null);
        } else {
            hour.setValue(time.getHour());
            minute.setValue(time.getMinute());
        }
    }

    private void setComboBoxTime(HBox hBox) {
        ComboBox<Integer> hour = (ComboBox<Integer>) hBox.getChildren().get(0);
        ComboBox<Integer> minute = (ComboBox<Integer>) hBox.getChildren().get(2);

        List<Integer> hours = IntStream.range(0, 24).boxed().toList();
        List<Integer> minutes = IntStream.range(0, 60).boxed().toList();

        hour.getItems().addAll(hours);
        minute.getItems().addAll(minutes);

        StringConverter<Integer> stringConverter = new StringConverter<>() {
            @Override
            public String toString(Integer object) {
                return String.format("%02d", object);
            }

            @Override
            public Integer fromString(String string) {
                if (string == null || string.equals(""))
                    return null;
                return Integer.parseInt(string);
            }
        };

        hour.setConverter(stringConverter);
        minute.setConverter(stringConverter);

        hour.setCellFactory(comboBox -> new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.format("%02d", item));
            }
        });
        minute.setCellFactory(comboBox -> new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.format("%02d", item));
            }
        });
        hour.setValue(null);
        minute.setValue(null);
    }

    private void setComboBoxListener() {
        comboBoxSubject.getSelectionModel().selectedItemProperty().addListener((observableValue, subject, s1) -> {
            setObservableListFromSubject(s1);
        });
        comboBoxTeacher.getSelectionModel().selectedItemProperty().addListener((observableValue, teacher, t1) -> {
            if (teacher == null || t1 == null)
                return;
            if (t1.equals(teacher)) {
                return;
            }
            replacement.setTeacherTo(t1);
        });
    }

    private void setReplacement() {
        if (replacement == null)
            return;
        if (replacement.getTeacherTo() == null)
            return;
        if (nuwLesson == null)
            return;
        if (nuwLesson.getId() != null) {
            Replacement replacementIn = replacementService.getReplacementByLesson(nuwLesson);
            if (replacementIn != null) {
                if (replacementIn.getTeacherFrom().equals(replacement.getTeacherTo())) {
                    replacementService.deleteReplacement(replacementIn.getId());
                    return;
                }
                replacementIn.setTeacherTo(replacement.getTeacherTo());
                replacementService.updateReplacement(replacementIn);
            } else {
                replacementService.addReplacement(replacement);
            }
        }
    }

    private void setObservableListFromSubject(Subject subject) {
        if (subject == null)
            return;
        List<Teacher> teachers = teacherSubjectService.getTeachersBySubjectId(subject.getId());
        List<Classroom> classrooms = classroomSubjectService.getClassroomsBySubjectId(subject.getId());
        teachers.sort(new Comparator<Teacher>() {
            @Override
            public int compare(Teacher o1, Teacher o2) {
                return o2.getRank() - o1.getRank();
            }
        });
        updateObservableList(comboBoxClassroom.getItems(), classrooms);
        updateObservableList(comboBoxTeacher.getItems(), teachers);
        comboBoxClassroom.setValue(null);
        comboBoxTeacher.setValue(null);
        comboBoxSubject.setValue(subject);
    }

    private void updateObservableList(ObservableList observableList, List list) {
        int size = observableList.size();
        observableList.remove(0, size);
        observableList.addAll(list);
    }

    public void setActiveButton(boolean active) {
        buttonUpdate.setDisable(!active);
        buttonDelete.setDisable(!active);
    }

    private void setSave(boolean save) {
        this.save = save;
        if (save) {
            buttonUpdate.getStyleClass().set(1, "button-green");
            buttonUpdate.setText("Сохранить");
        } else {
            buttonUpdate.getStyleClass().set(1, "button-blue");
            buttonUpdate.setText("Изменить");
        }
    }

    @FXML
    void deleteLesson(ActionEvent event) {
        lessonService.removeLesson(nuwLesson.getId());
        nuwListView.getItems().remove(nuwLesson);
        nuwListView.getSelectionModel().select(null);
    }

    @FXML
    void updateLesson(ActionEvent event) {
        if (save) {
            boolean isSave = saveLesson();
            setSave(!isSave);
        }
        if (nuwLesson == null) {
            return;
        }
        if (nuwLesson.getId() == null) {
            return;
        }
        if (checkChange()) {
            setLesson();
            setReplacement();
            lessonService.updateLesson(nuwLesson);
        }
    }

    private boolean saveLesson() {
        if (!checkEmpty()) {
            setLesson();
            nuwLesson = lessonService.addLesson(nuwLesson);
            if (nuwLesson != null) {
                replacement.setLessonId(nuwLesson.getId());
                replacement.setTeacherFrom(nuwLesson.getTeacher());
                return true;
            }
        }
        return false;
    }

    private void setLesson() {
        nuwLesson.setDate(dataPickerDate.getValue());
        nuwLesson.setTimePeriod(new TimePeriod(getLocalTime(hBoxStartTime), getLocalTime(hBoxEndTime)));
        nuwLesson.setClassroom(comboBoxClassroom.getValue());
        nuwLesson.setSubject(comboBoxSubject.getValue());
        nuwLesson.setTeacher(comboBoxTeacher.getValue());
        nuwLesson.setRoleLesson(comboBoxRoleLesson.getValue());
    }

    private boolean checkChange() {
        if (nuwLesson == null) {
            return true;
        }
        if (checkEmpty()) {
            return false;
        }
        LocalDate localDate = dataPickerDate.getValue();
        TimePeriod timePeriod = new TimePeriod(getLocalTime(hBoxStartTime), getLocalTime(hBoxEndTime));
        Classroom classroom = comboBoxClassroom.getValue();
        Subject subject = comboBoxSubject.getValue();
        Teacher teacher = comboBoxTeacher.getValue();
        RoleLesson roleLesson = comboBoxRoleLesson.getValue();
        return !(localDate.isEqual(nuwLesson.getDate()) &&
                timePeriod.equals(nuwLesson.getTimePeriod()) &&
                classroom.equals(nuwLesson.getClassroom()) &&
                subject.equals(nuwLesson.getSubject()) &&
                teacher.equals(nuwLesson.getTeacher()) &&
                roleLesson.equals(nuwLesson.getRoleLesson()));
    }

    private boolean checkEmpty() {
        return dataPickerDate.getValue() == null ||
                getLocalTime(hBoxStartTime) == null ||
                getLocalTime(hBoxEndTime) == null ||
                comboBoxClassroom.getValue() == null ||
                comboBoxSubject.getValue() == null ||
                comboBoxTeacher.getValue() == null ||
                comboBoxRoleLesson.getValue() == null;
    }


    private LocalTime getLocalTime(HBox hBox) {
        ComboBox<Integer> hour = (ComboBox<Integer>) hBox.getChildren().get(0);
        ComboBox<Integer> minute = (ComboBox<Integer>) hBox.getChildren().get(2);
        if (hour.getValue() == null || minute.getValue() == null)
            return null;
        LocalTime time = LocalTime.of(hour.getValue(), minute.getValue());
        return time;
    }

    public void setNewLesson(Lesson lesson) {
        nuwLesson = lesson;
        dataPickerDate.setValue(null);
        setTime(hBoxStartTime, null);
        setTime(hBoxEndTime, null);
        comboBoxClassroom.setValue(null);
        comboBoxSubject.setValue(null);
        comboBoxTeacher.setValue(null);
        comboBoxRoleLesson.setValue(null);
        setSave(true);
    }

}
