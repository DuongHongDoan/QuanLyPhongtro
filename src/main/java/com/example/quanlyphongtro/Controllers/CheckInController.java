package com.example.quanlyphongtro.Controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import com.example.quanlyphongtro.Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CheckInController implements Initializable {

    @FXML
    public Button btn_check_in;

    @FXML
    public ComboBox<String> cb_gender;

    @FXML
    public ComboBox<roomData> cb_room_name;

    @FXML
    public DatePicker dp_rend_date;

    @FXML
    public Label lbl_room_type;

    @FXML
    public TextField tf_cccd;

    @FXML
    public TextField tf_deposit;

    @FXML
    public TextField tf_home_town;

    @FXML
    public TextField tf_phone;

    @FXML
    public TextField tf_render_name;

    @FXML
    public TextField tf_init_electric;

    @FXML
    public TextField tf_init_water;


    ObservableList<roomData> emptyRoomList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //đưa ra danh sách phòng trống vào comboBox để chọn
        try {
            emptyRoomList = DatabaseDriver.getEmptyRoomList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cb_room_name.setItems(emptyRoomList);
        cb_room_name.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if(newVal != null){
                cb_room_name.setPromptText(newVal.toString());
                lbl_room_type.setText(newVal.getRoomTypeName());
            }
        });

        cb_gender.getItems().addAll("Nam", "Nữ");

        btn_check_in.setOnAction(this::onCheckIn);
    }

    private void onCheckIn(ActionEvent event) {
        if(tf_render_name.getText().isEmpty() || cb_gender.getSelectionModel().isEmpty() || tf_cccd.getText().isEmpty() ||
                tf_phone.getText().isEmpty() || tf_home_town.getText().isEmpty() || cb_room_name.getSelectionModel().isEmpty() ||
                lbl_room_type.getText().isEmpty() || dp_rend_date.getValue() == null || tf_init_electric.getText().isEmpty() ||
                tf_init_water.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng điền đầy đủ thông tin!");
            alert.show();
        }
        else {
            try {
                if(!checkCCCD(tf_cccd.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Số căn cước công dân không hợp lệ!");
                    alert.show();
                } else if (!checkPhone(tf_phone.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Số điện thoại không hợp lệ!");
                    alert.show();
                } else if (dp_rend_date.getValue().isAfter(LocalDate.now())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Ngày nhập không được lớn hơn ngày hiện tại!");
                    alert.show();
                } else if (!Pattern.matches("[0-9]+", tf_deposit.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Vui lòng nhập giá tiền (nếu không có tiền cọc thì nhập 0)!");
                    alert.show();
                } else {
                    String roomName = cb_room_name.getSelectionModel().getSelectedItem().getRoomName();
                    DatabaseDriver.checkIn(event, tf_render_name.getText(), cb_gender.getValue(), tf_cccd.getText(),
                            tf_phone.getText(), tf_home_town.getText(), roomName,
                            Double.valueOf(tf_deposit.getText()), Date.valueOf(dp_rend_date.getValue()),
                            Integer.parseInt(tf_init_electric.getText()),Integer.parseInt(tf_init_water.getText()));
                    MainViewController.reloadRenderList();
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean checkPhone(String str) {
        // Bieu thuc chinh quy mo ta dinh dang so dien thoai
        String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";

        // Kiem tra dinh dang

        return str.matches(reg);
    }

    private boolean checkCCCD(String str) {
        // Bieu thuc chinh quy mo ta dinh dang so dien thoai
        String reg = "^(0)((0[12468])|(1[0124579])|(2[024567])|(3[013-8])|(4[0245689])|(5[12468])|(6[024678])|(7[024579])|(8[0234679])|(9[1-6]))([0-3])((0[0-9])|(1[0-9])|(2[0-9])|(3[0-9])|(4[0-9])|(5[0-9])|(6[0-9])|(7[0-9])|(8[0-9])|(9[0-9]))(\\d{6})$";

        // Kiem tra dinh dang

        return str.matches(reg);
    }
}
