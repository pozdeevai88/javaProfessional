package ru.geekbrains;

import java.sql.*;

public class DBConnection {

    public DBConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("org.sqlite.JDBC");
//        Connection connection = DriverManager.getConnection("jdbc:sqlite:user_db.db");
//        statement = connection.createStatement();
//        statement.execute("INSERT INTO clients (name, login, password) VALUES (\"Pavel\", \"pavel1\", \"1234\")");
//        statement.execute("INSERT INTO clients (name, login, password) VALUES (\"Oleg\", \"oleg1\", \"1234\")");
//        statement.execute("INSERT INTO clients (name, login, password) VALUES (\"Nick\", \"nick1\", \"4321\")");
//        statement.close();
//        connection.close();
    }

    public Client isClientExist (String login, String password) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:user_db.db");
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT name, login, password FROM clients");
        while (result.next()) {
            String nameDB = result.getString("name");
            String loginDB = result.getString("login");
            String passwordDB = result.getString("password");
            if (login.equals(loginDB) && password.equals(passwordDB)) {
                result.close();
                statement.close();
                connection.close();
                return new Client(nameDB, login, password);
            }
        }
        result.close();
        statement.close();
        connection.close();
        return null;
    }

    public void changeNickName (String newNickName, String login) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:user_db.db");
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE clients SET name = '" + newNickName + "' WHERE login = '" + login + "'");
        statement.close();
        connection.close();
    }
}
