package com.example.quanlyphongtro.Controllers;

import com.example.quanlyphongtro.Models.Model;
import com.example.quanlyphongtro.Models.DatabaseDriver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.net.URL;
import java.security.MessageDigest;
import java.util.Random;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    public Button btn_back;

    @FXML
    public Button btn_signin;

    @FXML
    public ComboBox<String> combox_gender;

    @FXML
    public DatePicker date_birth;

    @FXML
    public PasswordField txt_enter_passwd;

    @FXML
    public TextField txt_fld_email;

    @FXML
    public TextField txt_fld_fullname;

    @FXML
    public TextField txt_fld_phone;

    @FXML
    public TextField txt_fld_username;

    @FXML
    public PasswordField txt_passwd;
    public ComboBox<String> combox_QA;
    public TextField txt_QA;

    //ham click su kien icon back
    private void onMoveToLogin() {
        Stage stage = (Stage) btn_back.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    //ham tao passwd md5
    public static String getMD5(String md5) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    private static final String key = "Doan2020Doan2021Doan2022Doan2023awdtgujolzsxdcfvgbhn";
    public static String getMD5Key(String pass) {
        return getMD5(getMD5(pass) + key);
    }

    //cac ham kiem tra nhap lieu cua user
    //kiem tra sđt
    private boolean checkPhone(String str) throws Exception {
        // Bieu thuc chinh quy mo ta dinh dang so dien thoai
        String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";

        // Kiem tra dinh dang
        boolean kt = str.matches(reg);

        return kt;
    }

    //kiem tra email
    private boolean checkEmail (String str) throws Exception {
        //bieu thuc chinh quy dinh dang 1 email
        String reg = "^[A-Za-z0-9]+[A-Za-z0-9]*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)$";

        boolean kt = str.matches(reg);
        return kt;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //them cac gia tri vao comboBox gioi tinh
        combox_gender.getItems().addAll("Nam", "Nữ");

        //them cac gia tri vao conboBox cau hoi bao mat
        combox_QA.getItems().addAll(
                "Tên thành phố hoặc thị trấn mà bạn sinh ra?",
                "Tên trường trung học của bạn là gì?",
                "Tên người bạn thân nhất là gì?",
                "Ngày sinh nhật của cha mẹ bạn là gì?",
                "Tên thú cưng đầu tiên của bạn là gì?",
                "Tên trường đại học hoặc cao đẳng mà bạn từng theo học là gì?"
        );

        //nhan nut quay lai trang login
        btn_back.setOnAction(e -> onMoveToLogin());

        //click nut dang ky
        btn_signin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!txt_fld_fullname.getText().trim().isEmpty() && !txt_fld_username.getText().trim().isEmpty() &&
                        !txt_fld_phone.getText().trim().isEmpty() && !txt_fld_email.getText().trim().isEmpty() &&
                        !txt_passwd.getText().trim().isEmpty() && !txt_enter_passwd.getText().trim().isEmpty()) {
                    //kiem tra sđt
                    try {
                        if(!checkPhone(txt_fld_phone.getText())) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Số điện thoại không đúng định dạng!");
                            alert.show();
                        }
                        else if (!checkEmail(txt_fld_email.getText())){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Email không đúng định dạng!");
                            alert.show();
                        } else if (!txt_passwd.getText().equals(txt_enter_passwd.getText())) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Mật khẩu không khớp!");
                            alert.show();
                        } else {
                            DatabaseDriver.signupUser(event, txt_fld_fullname.getText(), txt_fld_username.getText(),
                                    combox_gender.getValue(), txt_fld_phone.getText(), txt_fld_email.getText(),
                                    getMD5Key(txt_passwd.getText()), combox_QA.getValue(), txt_QA.getText());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Vui lòng điền đầy đủ thông tin!");
                    alert.show();
                }
            }
        });
    }
}

