package ru.geekbrains;

import java.sql.*;

class AuthService {

    synchronized Client auth(String login, String password) throws SQLException, ClassNotFoundException {

        DBConnection db = new DBConnection();

        return db.isClientExist(login, password);
    }
}