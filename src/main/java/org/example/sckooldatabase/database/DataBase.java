package org.example.sckooldatabase.database;

import org.example.sckooldatabase.data.Configuration;

import java.sql.*;

public class DataBase {

    private static Connection connection;

    public DataBase() {
    }

    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Connection connect() throws SQLException {
        if (connection != null)
            return connection;
        String url = Configuration.getUrl() + Configuration.getName();
        String user = Configuration.getUser();
        String password = Configuration.getPassword();
        return DriverManager.getConnection(url, user, password);
    }


    public static void initialTable() {
        initialUsersTable();
        initialSubjectsTable();
        initialTeacherTable();
        initialTeacherSubjectTable();
        initialClassroomTable();
        initialClassroomSubjectTable();
        initialLessonTable();
        initialReplacementTable();
    }

    private static void initialUsersTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Users ("
                + "id BIGINT AUTO_INCREMENT, "
                + "login VARCHAR(255) NOT NULL, "
                + "password VARCHAR(255) NOT NULL, "
                + "solt VARCHAR(255) NOT NULL, "
                + "role ENUM('ADMIN', 'USER') NOT NULL, "
                + "PRIMARY KEY (id)"
                + ");";

        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Таблица 'Users' успешно создана или уже существует.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initialSubjectsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS subjects ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL UNIQUE, "
                + "description TEXT"
                + ");";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица 'subjects' успешно создана или уже существует.");
            addSubjects(); // Вызываем метод для добавления предметов
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initialTeacherTable() {
        String sql = "CREATE TABLE IF NOT EXISTS teachers ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "first_name VARCHAR(255) NOT NULL, "
                + "last_name VARCHAR(255) NOT NULL, "
                + "three_name VARCHAR(255), "
                + "short_name VARCHAR(255), "
                + "birthday DATE, "
                + "teacher_rank INT"
                + ");";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица 'teachers' успешно создана или уже существует.");
            addTeachers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initialTeacherSubjectTable() {
        String sql = "CREATE TABLE IF NOT EXISTS teacher_subject ("
                + "teacher_id BIGINT NOT NULL, "
                + "subject_id BIGINT NOT NULL, "
                + "PRIMARY KEY (teacher_id, subject_id), "
                + "FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE, "
                + "FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE"
                + ");";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.execute(sql);
            addTeacherSubject();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initialClassroomTable() {
        String sql = "CREATE TABLE IF NOT EXISTS classrooms ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "number INT NOT NULL, "
                + "floor INT NOT NULL, "
                + "number_seat INT NOT NULL"
                + ");";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица 'classrooms' успешно создана или уже существует.");
            addClassrooms();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initialClassroomSubjectTable() {
        String sql = "CREATE TABLE IF NOT EXISTS classroom_subject ("
                + "classroom_id BIGINT NOT NULL, "
                + "subject_id BIGINT NOT NULL, "
                + "PRIMARY KEY (classroom_id, subject_id), "
                + "FOREIGN KEY (classroom_id) REFERENCES classrooms(id) ON DELETE CASCADE, "
                + "FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE"
                + ");";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица 'classroom_subject' успешно создана или уже существует.");
            addClassroomSubjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initialLessonTable() {
        String sql = "CREATE TABLE IF NOT EXISTS lessons ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "date DATE NOT NULL, "
                + "start_time TIME NOT NULL, "
                + "end_time TIME NOT NULL, "
                + "classroom_id BIGINT, "
                + "subject_id BIGINT, "
                + "teacher_id BIGINT, "
                + "role ENUM('EXAM', 'CLASS') NOT NULL, "
                + "FOREIGN KEY (classroom_id) REFERENCES classrooms(id), "
                + "FOREIGN KEY (subject_id) REFERENCES subjects(id), "
                + "FOREIGN KEY (teacher_id) REFERENCES teachers(id)"
                + ");";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица 'lessons' успешно создана или уже существует.");
            addLessons();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void initialReplacementTable() {
        String sql = "CREATE TABLE IF NOT EXISTS replacements ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "lesson_id BIGINT NOT NULL, "
                + "teacher_from BIGINT NOT NULL, "
                + "teacher_to BIGINT NOT NULL, "
                + "FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE, "
                + "FOREIGN KEY (teacher_from) REFERENCES teachers(id), "
                + "FOREIGN KEY (teacher_to) REFERENCES teachers(id)"
                + ");";

        try (Connection connection = new DataBase().connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);
            System.out.println("Таблица 'replacements' успешно создана или уже существует.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private static void addSubjects() {
        if (checkTable("subjects")) {
            return;
        }
        String sql = "INSERT INTO subjects (name, description) VALUES "
                + "('Математика', 'Основы алгебры и геометрии.'), "
                + "('Физика', 'Введение в классическую механику.'), "
                + "('Химия', 'Основные принципы химических реакций и лабораторных техник.'), "
                + "('Биология', 'Изучение живых организмов и их процессов.'), "
                + "('История', 'Обзор исторических событий и их значимости.'), "
                + "('География', 'Изучение Земли и её природных особенностей.'), "
                + "('Литература', 'Исследование классических и современных литературных произведений.'), "
                + "('Информатика', 'Введение в программирование и компьютерные системы.'), "
                + "('Физическая культура', 'Физические упражнения и спортивные активности.'), "
                + "('Изобразительное искусство', 'Основы визуального искусства и творческое самовыражение.'), "
                + "('Музыка', 'Изучение музыкальной теории и практики.'), "
                + "('Экономика', 'Основные принципы экономики и финансовой грамотности.'), "
                + "('Философия', 'Введение в философское мышление и концепции.'), "
                + "('Психология', 'Изучение человеческого поведения и ментальных процессов.');";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Предметы успешно добавлены в таблицу 'subjects'.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addTeachers() {
        if (checkTable("teachers")) {
            return;
        }
        String sql = "INSERT INTO teachers (last_name, first_name, three_name, short_name, birthday, teacher_rank) VALUES "
                + "('Иванова', 'Ольга', 'Петровна', 'Иванова О. П.', '1983-04-12', 80), "
                + "('Смирнов', 'Дмитрий', 'Андреевич', 'Смирнов Д. А.', '1978-11-30', 90), "
                + "('Кузнецова', 'Евгения', 'Владимировна', 'Кузнецова Е. В.', '1986-09-05', 70), "
                + "('Зайцев', 'Алексей', 'Алексеевич', 'Зайцев А. А.', '1992-02-18', 80), "
                + "('Попова', 'Светлана', 'Дмитриевна', 'Попова С. Д.', '1980-07-22', 70), "
                + "('Семенов', 'Игорь', 'Игоревич', 'Семенов И. И.', '1976-06-14', 90), "
                + "('Петрова', 'Татьяна', 'Сергеевна', 'Петрова Т. С.', '1984-03-08', 80), "
                + "('Морозов', 'Владимир', 'Владимирович', 'Морозов В. В.', '1981-10-25', 70), "
                + "('Волкова', 'Наталья', 'Александровна', 'Волкова Н. А.', '1979-08-17', 90), "
                + "('Борисов', 'Роман', 'Николаевич', 'Борисов Р. Н.', '1987-12-03', 80);";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Учителя успешно добавлены в таблицу 'teachers'.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addTeacherSubject() {
        if (checkTable("teacher_subject")) {
            return;
        }
        String sql = "INSERT INTO teacher_subject (teacher_id, subject_id) VALUES "
                + "(1, 1), "
                + "(1, 2), "
                + "(2, 3), "
                + "(2, 4), "
                + "(3, 5), "
                + "(4, 6), "
                + "(4, 7), "
                + "(5, 8), "
                + "(5, 9), "
                + "(6, 10), "
                + "(6, 11), "
                + "(7, 12), "
                + "(7, 13), "
                + "(8, 14), "
                + "(8, 13);";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Связи между учителями и предметами установлены");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void addClassrooms() {
        if (checkTable("classrooms")) {
            return;
        }
        String sql = "INSERT INTO classrooms (number, floor, number_seat) VALUES "
                + "(101, 1, 30), "
                + "(102, 1, 25), "
                + "(201, 2, 20), "
                + "(202, 2, 35), "
                + "(301, 3, 40), "
                + "(302, 3, 45);";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Классы успешно добавлены в таблицу 'classrooms'.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addClassroomSubjects() {
        if (checkTable("classroom_subject")) {
            return;
        }
        String sql = "INSERT INTO classroom_subject (classroom_id, subject_id) VALUES "
                + "(1, 1), "
                + "(1, 3), "
                + "(2, 2), "
                + "(2, 4), "
                + "(3, 5), "
                + "(4, 6), "
                + "(4, 7), "
                + "(5, 8), "
                + "(6, 9);";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Связи между классами и предметами установлены.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addLessons() {
        if (checkTable("lessons")) {
            return;
        }
        String sql = "INSERT INTO lessons (date, start_time, end_time, classroom_id, subject_id, teacher_id, role) VALUES "
                + "('2024-09-01', '09:00:00', '10:30:00', 1, 1, 1, 'CLASS'), "
                + "('2024-09-01', '11:00:00', '12:30:00', 2, 2, 2, 'CLASS'), "
                + "('2024-09-01', '13:00:00', '14:30:00', 3, 3, 3, 'EXAM'), "
                + "('2024-09-02', '09:00:00', '10:30:00', 1, 1, 4, 'CLASS'), "
                + "('2024-09-02', '11:00:00', '12:30:00', 2, 2, 5, 'CLASS'), "
                + "('2024-09-02', '13:00:00', '14:30:00', 3, 3, 6, 'EXAM');";
        try (Statement statement = new DataBase().connect().createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Уроки успешно добавлены в таблицу 'lessons'.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkTable(String table) {
        try (Statement stmt = new DataBase().connect().createStatement()) {
            String sql = "SELECT COUNT(*) AS count FROM " + table;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int rowCount = rs.getInt("count");
                return rowCount > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
