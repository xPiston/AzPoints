package com.azuriom.azpoints.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Connection conn;

    public void connect(String host, String port, String database, String user, String password) {
        if (!isConnected()) {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
                System.out.println("[AzPoints] Connection established with the database");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("[AzPoints] Unable to connect to the database");
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return this.conn != null;
    }

    public Connection getConnection() {
        return conn;
    }

}

