module com.example.quanlyphongtro {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.rabbitmq.client;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.quanlyphongtro to javafx.fxml;
    exports com.example.quanlyphongtro;
    exports com.example.quanlyphongtro.Controllers;
    exports com.example.quanlyphongtro.Models;
    exports com.example.quanlyphongtro.Views;
}