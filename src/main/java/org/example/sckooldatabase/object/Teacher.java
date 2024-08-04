package org.example.sckooldatabase.object;

import org.example.sckooldatabase.utils.DateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Teacher {
    private Long id;
    private String firstName;
    private String lastName;
    private String threeName;
    private String shortName;
    private LocalDate birthday;
    private List<Subject> subjects;
    private int rank;

    public Teacher(String firstName, String lastName, String threeName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.threeName = threeName;
        this.shortName = lastName.concat(" ")
                .concat(firstName.substring(0, 1))
                .concat(". ")
                .concat(threeName == null ? "" : threeName.substring(0, 1));
    }

    public Teacher(String firstName, String lastName, String threeName, LocalDate birthday) {
        this(firstName, lastName, threeName);
        this.birthday = birthday;
    }

    public Teacher(String firstName, String lastName, String threeName, String birthday) {
        this(firstName, lastName, threeName);
        setBirthday(birthday);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getThreeName() {
        return threeName;
    }

    public void setThreeName(String threeName) {
        this.threeName = threeName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    private void setBirthday(String date) {
        this.birthday = DateUtil.parseDate(date);
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(firstName, teacher.firstName)
                && Objects.equals(lastName, teacher.lastName)
                && Objects.equals(threeName, teacher.threeName)
                && Objects.equals(birthday, teacher.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, threeName, birthday);
    }

    //    @Override
//    public String toString() {
//        return "Teacher{" +
//                "firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", threeName='" + threeName + '\'' +
//                ", subjects=" + subjects +
//                ", rank=" + rank +
//                '}';
//    }
}
