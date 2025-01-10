package com.company;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/ConferenceDB", "root", "12345678");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
