package ru.geekbrains;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.Arrays;

public class DBConnection {
    static Connection connection;
    private static DBConnection instance;
    private static final Logger LOGGER = LogManager.getLogger("dbLog");

    static {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:user_db.db");
        } catch (SQLException e) {
            LOGGER.warn("Не удалось установить соединение с базой: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public DBConnection() throws ClassNotFoundException, SQLException {
        LOGGER.info("Соединение с базой данных установлено");
//        statement = connection.createStatement();
//        statement.execute("INSERT INTO clients (name, login, password) VALUES (\"Pavel\", \"pavel1\", \"1234\")");
//        statement.execute("INSERT INTO clients (name, login, password) VALUES (\"Oleg\", \"oleg1\", \"1234\")");
//        statement.execute("INSERT INTO clients (name, login, password) VALUES (\"Nick\", \"nick1\", \"4321\")");
//        statement.close();
    }

    public Client isClientExist(String login, String password) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT name, login, password FROM clients");
        while (result.next()) {
            String nameDB = result.getString("name");
            String loginDB = result.getString("login");
            String passwordDB = result.getString("password");
            if (login.equals(loginDB) && password.equals(passwordDB)) {
                result.close();
                statement.close();
                return new Client(nameDB, login, password);
            }
        }
        result.close();
        statement.close();
        return null;
    }

    public void changeNickName(String newNickName, String login) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE clients SET name = ? WHERE login = ?");
        statement.setString(1, newNickName);
        statement.setString(2, login);
        LOGGER.info("Изменён ник для " + login + ". Новый ник = " + newNickName);
        statement.executeUpdate();
        statement.close();
    }
}
