package com.example.quanlyphongtro.Controllers;

import com.example.quanlyphongtro.Models.DatabaseDriver;
import com.example.quanlyphongtro.Models.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public Button btn_create_acc;

    @FXML
    public Button btn_forget_pass;

    @FXML
    public Button btn_login;

    @FXML
    public TextField txt_fld_name;

    @FXML
    public PasswordField txt_fld_passwd;
    //la cac ham dung trong su ly su kien click
    //click tao acc moi
    private void onCreateNewAcc() {
        Stage stage = (Stage) btn_create_acc.getScene().getWindow(); //lay ra cua so hien tai
        Model.getInstance().getViewFactory().closeStage(stage); //dong cua so hien tai
        Model.getInstance().getViewFactory().showSignupWindow();
    }
    private void onForgetPass() {
        Stage stage = (Stage) btn_forget_pass.getScene().getWindow(); //lay ra cua so hien tai
        Model.getInstance().getViewFactory().closeStage(stage); //dong cua so hien tai
        Model.getInstance().getViewFactory().showForgetPassWindow();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //su kien click vao tao acc moi
        btn_create_acc.setOnAction(e -> onCreateNewAcc());

        btn_forget_pass.setOnAction(e -> onForgetPass());

        //su kien click login
        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String passwd = SignupController.getMD5Key(txt_fld_passwd.getText());
                DatabaseDriver.loginUser(event, txt_fld_name.getText(), passwd);
            }
        });
    }
}

