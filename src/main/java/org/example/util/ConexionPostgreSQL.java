package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionPostgreSQL {

    private static final String URL = "jdbc:postgresql://localhost:5432/joyeria";
    private static final String USER = "postgres";
    private static final String PASSWORD = "9094";

    public static Connection getConnection()throws SQLException {
        try{
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el driver de PostreSQL", e);
        }
    }
}
