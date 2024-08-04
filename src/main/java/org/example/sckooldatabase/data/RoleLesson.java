package org.example.sckooldatabase.data;

import java.util.Arrays;
import java.util.List;

public enum RoleLesson {
    EXAM("Экзамен"),
    CLASS("Занятие");

    private final String role;

    RoleLesson(String role) {
        this.role = role;
    }

    public static List<String> getRoles() {
        return Arrays.stream(values())
                .map(RoleLesson::getRole)
                .toList();
    }

    public static RoleLesson getRole(String role){
        return Arrays.stream(values()).filter(nuwRole -> nuwRole.getRole().equals(role)).toList().get(0);
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return role;
    }
}
