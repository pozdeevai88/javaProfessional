package ru.geekbrains;

import java.sql.*;

class AuthService {

    synchronized Client auth(String login, String password) throws SQLException, ClassNotFoundException {

        DBConnection db = DBConnection.getInstance();

        return db.isClientExist(login, password);

    }
}