package org.example.sckooldatabase.viewer;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.sckooldatabase.object.Subject;

public class SubjectViewer {
    private ObservableList<Node> subjectData;
    private TextField textFieldName;
    private TextArea textAreaDescription;

    public SubjectViewer(ObservableList<Node> subjectData) {
        this.subjectData = subjectData;
        this.textFieldName = (TextField) subjectData.get(0);
        this.textAreaDescription = (TextArea) subjectData.get(1);
    }

    public void setError(String error) {
        textFieldName.setText(null);
        textFieldName.setPromptText(error);
    }

    public Subject getSubject() {
        if (!isValid()) {
            return null;
        }
        Subject subject = new Subject(
                textFieldName.getText(),
                textAreaDescription.getText()
        );
        return subject;
    }

    public void subjectView(Subject subject) {
        if (subject == null) {
            clear();
        }
        textFieldName.setText(subject.getName());
        textAreaDescription.setText(subject.getDescription());
    }

    public void clear() {
        textFieldName.setText(null);
        textAreaDescription.setText(null);
    }

    private boolean isValid() {
        if (!isValid(textFieldName.getText())) {
            return false;
        }
        if (!isValid(textAreaDescription.getText())) {
            return false;
        }
        return true;
    }

    private boolean isValid(String s) {
        return s != null && !s.equals("");
    }
}
