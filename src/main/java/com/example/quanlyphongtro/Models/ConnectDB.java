package com.example.quanlyphongtro.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    public static Connection connectDB() {
        String url = "jdbc:mysql://localhost/nienluancoso";
        String user = "root";
        String passwd_db = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, passwd_db);
            System.out.println("Kết nối db thành công");
            return conn;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
