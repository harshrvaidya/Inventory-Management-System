package com.tcet.ivm2.modal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    private static final String URL = "jdbc:mysql://localhost:3307/inventory";
    private static final String USER = "root";
    private static final String PASSWORD = "66818359";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // TEST CONNECTION
    public static void main(String[] args) {
        try (Connection con = connect()) {
            if (con != null) {
                System.out.println("✅ Connection Successful!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Connection Failed!");
            e.printStackTrace();
        }
    }
}
