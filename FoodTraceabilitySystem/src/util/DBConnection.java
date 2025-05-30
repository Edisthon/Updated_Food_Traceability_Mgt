/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.*;

/**
 *
 * @author USER
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/food_traceability_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Nzulikikuyu123";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
}
