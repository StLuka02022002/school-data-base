package org.example.sckooldatabase.data;

import org.example.sckooldatabase.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    public static int LENGTH_LOGIN_AND_PASSWORD = 6;
    public static String LOGIN_AND_PASSWORD_MESSAGE = "Длина логина и пароля должна быть не менее " +
            LENGTH_LOGIN_AND_PASSWORD + " символов";
    public static int SOLT_SIZE = 5;
    private static String CONFIGURATION_FILE = "configuration.properties";

    private static String url;
    private static String user;
    private static String name;
    private static String password;
    private static Role role;

    public static void init() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Main.class.getResourceAsStream(CONFIGURATION_FILE)) {
            properties.load(input);
        }

        url = properties.getProperty("db.url");
        name = properties.getProperty("db.name");
        user = properties.getProperty("db.user");
        password = properties.getProperty("db.password");
    }

    public static String getUrl() {
        return url;
    }

    public static String getName() {
        return name;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }

    public static Role getRole() {
        return role;
    }

    public static void setRole(Role role) {
        Configuration.role = role;
    }
}
