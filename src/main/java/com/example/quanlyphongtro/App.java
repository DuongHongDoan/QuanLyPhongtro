package com.example.quanlyphongtro;

import com.example.quanlyphongtro.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}
