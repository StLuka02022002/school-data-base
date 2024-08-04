package org.example.sckooldatabase.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.example.sckooldatabase.data.Configuration;
import org.example.sckooldatabase.data.Role;
import org.example.sckooldatabase.data.RoleLesson;
import org.example.sckooldatabase.object.Classroom;
import org.example.sckooldatabase.object.Lesson;
import org.example.sckooldatabase.object.Subject;
import org.example.sckooldatabase.object.Teacher;
import org.example.sckooldatabase.service.ClassroomService;
import org.example.sckooldatabase.service.LessonService;
import org.example.sckooldatabase.service.SubjectService;
import org.example.sckooldatabase.service.TeacherService;
import org.example.sckooldatabase.utils.WindowUtil;
import org.example.sckooldatabase.viewer.ListViewCellFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class MainController {

    @FXML
    private ComboBox<Classroom> comboBoxClassroom;

    @FXML
    private ComboBox<RoleLesson> comboBoxRoleLesson;

    @FXML
    private VBox vBoxClassroom;

    @FXML
    private VBox vBoxRoleLesson;

    @FXML
    private VBox vBoxSubject;

    @FXML
    private VBox vBoxTeacher;

    @FXML
    private ComboBox<Subject> comboBoxSubject;

    @FXML
    private ComboBox<Teacher> comboBoxTeacher;

    @FXML
    private HBox hBoxEndTime;

    @FXML
    private HBox hBoxStartTime;

    @FXML
    private ListView<Lesson> listView;

    @FXML
    private VBox vBox;

    @FXML
    private VBox vBoxDate;

    @FXML
    private Button buttonAdd;

    private ClassroomService classroomService;
    private SubjectService subjectService;
    private TeacherService teacherService;
    private LessonService lessonService;
    private List<Lesson> allLessons;

    private boolean save;

    @FXML
    public void initialize() {
        classroomService = new ClassroomService();
        subjectService = new SubjectService();
        teacherService = new TeacherService();
        lessonService = new LessonService();

        update();
        setComboBoxTime(hBoxStartTime);
        setComboBoxTime(hBoxEndTime);
        setDataPicker(vBoxDate);
        listView.setCellFactory(new ListViewCellFactory());
        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, lesson, l1) -> {
            setSave(false);
        });
        Configuration.setRole(Role.ADMIN);
        if (Configuration.getRole() != Role.ADMIN) {
            buttonAdd.setDisable(true);
        }

    }

    private void updateLessons() {
        allLessons = lessonService.getAllLessons();
    }

    private void updateObservableList(ObservableList observableList, List list){
        int size = observableList.size();
        observableList.remove(0,size);
        observableList.addAll(list);
    }

    private void update() {
        updateLessons();
        updateObservableList(comboBoxClassroom.getItems(), classroomService.getAllClassrooms());
        updateObservableList(comboBoxSubject.getItems(), subjectService.getAllSubjects());
        updateObservableList(comboBoxTeacher.getItems(), teacherService.getAllTeachers());
        updateObservableList(comboBoxRoleLesson.getItems(), Arrays.stream(RoleLesson.values()).toList());
        updateObservableList(listView.getItems(), allLessons);
    }



    private void setSave(boolean save) {
        this.save = save;
        if (save) {
            buttonAdd.setText("Сохранить");
        } else {
            buttonAdd.setText("Добавить");
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
                if (object == null)
                    return "";
                return String.format("%02d", object);
            }

            @Override
            public Integer fromString(String string) {
                if (string == null || string.equals(""))
                    return 0;
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

    private void setDataPicker(VBox vBox) {
        DatePicker datePicker1 = (DatePicker) vBox.getChildren().get(1);
        DatePicker datePicker2 = (DatePicker) vBox.getChildren().get(2);

        datePicker1.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                datePicker2.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(empty || date.isBefore(newValue));
                    }
                });
            }
        });
    }

    @FXML
    void addLesson(ActionEvent event) {
            listView.getItems().add(new Lesson());
            listView.scrollTo(listView.getItems().size() - 1);
            setSave(true);
    }

    @FXML
    void applyFilter(ActionEvent event) {
        updateLessons();
        allLessons = allLessons.stream()
                .filter(this::getDateFilter)
                .filter(this::getTimeFilter)
                .filter(this::getClassroomFilter)
                .filter(this::getSubjectFilter)
                .filter(this::getTeacherFilter)
                .filter(this::getRoleLessonFilter)
                .toList();
        updateObservableList(listView.getItems(),allLessons);
    }

    private boolean getDateFilter(Lesson lesson) {
        DatePicker datePicker1 = (DatePicker) vBoxDate.getChildren().get(1);
        DatePicker datePicker2 = (DatePicker) vBoxDate.getChildren().get(2);
        LocalDate localDate1 = datePicker1.getValue();
        LocalDate localDate2 = datePicker2.getValue();
        if (localDate1 == null & localDate2 == null) {
            return true;
        }
        if (localDate1 == null) {
            return localDate2.isAfter(lesson.getDate())
                    || localDate2.isEqual(lesson.getDate());
        } else if (localDate2 == null) {
            return localDate1.isBefore(lesson.getDate())
                    || localDate1.isEqual(lesson.getDate());
        } else {
            return (localDate1.isBefore(lesson.getDate()) ||
                    localDate1.isEqual(lesson.getDate())) &&
                    (localDate2.isAfter(lesson.getDate()) ||
                            localDate2.isEqual(lesson.getDate()));
        }
    }

    private boolean getTimeFilter(Lesson lesson) {
        LocalTime start = getLocalTime(hBoxStartTime);
        LocalTime end = getLocalTime(hBoxEndTime);
        LocalTime startLesson = lesson.getTimePeriod().getStartTime();
        LocalTime endLesson = lesson.getTimePeriod().getEndTime();
        if (start == null && end == null) {
            return true;
        }
        if (start == null) {
            return end.isAfter(endLesson) || end.equals(endLesson);
        } else if (end == null) {
            return start.isBefore(startLesson) || start.equals(startLesson);
        } else {
            return (start.isBefore(startLesson) || start.equals(startLesson)) &&
                    (end.isAfter(endLesson) || end.equals(endLesson));
        }
    }

    private LocalTime getLocalTime(HBox hBox) {
        ComboBox<Integer> hour = (ComboBox<Integer>) hBox.getChildren().get(0);
        ComboBox<Integer> minute = (ComboBox<Integer>) hBox.getChildren().get(2);
        if (hour.getValue() == null || minute.getValue() == null)
            return null;
        LocalTime time = LocalTime.of(hour.getValue(), minute.getValue());
        return time;
    }

    private boolean getClassroomFilter(Lesson lesson) {
        if (comboBoxClassroom.getValue() == null) {
            return true;
        }
        return comboBoxClassroom.getValue().equals(lesson.getClassroom());
    }

    private boolean getSubjectFilter(Lesson lesson) {
        if (comboBoxSubject.getValue() == null) {
            return true;
        }
        return comboBoxSubject.getValue().equals(lesson.getSubject());
    }

    private boolean getTeacherFilter(Lesson lesson) {
        if (comboBoxTeacher.getValue() == null) {
            return true;
        }
        return comboBoxTeacher.getValue().equals(lesson.getTeacher());
    }

    private boolean getRoleLessonFilter(Lesson lesson) {
        if (comboBoxRoleLesson.getValue() == null) {
            return true;
        }
        return comboBoxRoleLesson.getValue().equals(lesson.getRoleLesson());
    }

    @FXML
    void clearFilter(ActionEvent event) {
        clearComboBoxTime(hBoxStartTime);
        clearComboBoxTime(hBoxEndTime);
        clearDatePicker(vBoxDate);
        clearComboBox(comboBoxClassroom);
        clearComboBox(comboBoxSubject);
        clearComboBox(comboBoxTeacher);
        clearComboBox(comboBoxRoleLesson);
    }

    private void clearComboBoxTime(HBox hBox) {
        ComboBox<Integer> hour = (ComboBox<Integer>) hBox.getChildren().get(0);
        ComboBox<Integer> minute = (ComboBox<Integer>) hBox.getChildren().get(2);
        hour.setValue(null);
        minute.setValue(null);
    }

    private void clearDatePicker(VBox vBox) {
        DatePicker datePicker1 = (DatePicker) vBox.getChildren().get(1);
        DatePicker datePicker2 = (DatePicker) vBox.getChildren().get(2);
        datePicker1.setValue(null);
        datePicker2.setValue(null);
    }

    private void clearComboBox(ComboBox comboBox) {
        comboBox.setValue(null);
    }

    @FXML
    void close(ActionEvent event) {
        WindowUtil.close();
    }


    @FXML
    void openAdderSubjectWindow(ActionEvent event) {
        WindowUtil.openNewWindow("fxml/add-subject.fxml", vBox.getScene());
        update();
    }

    @FXML
    void openAdderTeacherWindow(ActionEvent event) {
        WindowUtil.openNewWindow("fxml/add-teacher.fxml", vBox.getScene());
        update();
    }

    @FXML
    void openAdderClassroomWindow(ActionEvent event) {
        WindowUtil.openNewWindow("fxml/add-classroom.fxml", vBox.getScene());
        update();
    }

    @FXML
    void openStatisticsWindow(ActionEvent event){
        WindowUtil.openNewWindow("fxml/statistics.fxml", buttonAdd.getScene());
        update();
    }

    @FXML
    void close() {
        WindowUtil.close();
    }
}
