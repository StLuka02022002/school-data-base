package org.example.sckooldatabase.object;

import org.example.sckooldatabase.data.Role;

public class User {
    private Long id;
    private String login;
    private String password;
    private String solt;
    private Role role;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, String solt, Role role) {
        this.login = login;
        this.password = password;
        this.solt = solt;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSolt() {
        return solt;
    }

    public void setSolt(String solt) {
        this.solt = solt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
