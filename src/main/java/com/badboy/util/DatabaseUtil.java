package com.badboy.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://localhost:3306/aaa?characterEncoding=utf8_general_ci";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";



    public static Connection getConnection() {
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return conn;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
