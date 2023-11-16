package com.example.quanlyphongtro.Controllers;

import com.example.quanlyphongtro.Models.ConnectDB;
import com.example.quanlyphongtro.Models.DatabaseDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BillController implements Initializable {

    @FXML
    public Label lbl_renderName;

    @FXML
    public Button btn_bill_confirm;

    @FXML
    public Button btn_bill_edit;

    @FXML
    public RadioButton rad_bill_notPay;

    @FXML
    public RadioButton rad_bill_paid;

    @FXML
    public TextField tf_bill_boss;

    @FXML
    public Label tf_bill_electricMoney;

    @FXML
    public TextField tf_bill_newElectricIndex;

    @FXML
    public TextField tf_bill_newWaterIndex;

    @FXML
    public TextField tf_bill_note;

    @FXML
    public TextField tf_bill_oldElectricIndex;

    @FXML
    public TextField tf_bill_oldWaterIndex;

    @FXML
    public DatePicker dp_date_create;

    @FXML
    public TextField tf_bill_roomName;

    @FXML
    public TextField tf_bill_roomPrice;

    @FXML
    public Label tf_bill_serviceOther;

    @FXML
    public Label tf_bill_sumMoney;

    @FXML
    public Label tf_bill_waterMoney;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_bill_confirm.setOnAction(this::onSubmit);
        btn_bill_edit.setOnAction(this::onEdit);

    //tinh toan tien thu
        //lang nghe su thay doi trong o textField chi so moi cua dien va nuoc, tien dich vu khac, tien phong
        tf_bill_newElectricIndex.textProperty().addListener((observable, oldVal, newVal) -> {
            if(!newVal.isEmpty()){
                calculatorElectricBill();
            }
            else {
                tf_bill_electricMoney.setText("");
            }
        });
        tf_bill_newWaterIndex.textProperty().addListener((observable, oldVal, newVal) -> {
            if(!newVal.isEmpty()) {
                calculatorWaterBill();
            }
            else {
                tf_bill_waterMoney.setText("");
            }
        });
        tf_bill_electricMoney.textProperty().addListener((observable, oldVal, newVal) -> {
            calculatorSumBill();
        });
        tf_bill_waterMoney.textProperty().addListener((observable, oldVal, newVal) -> {
            calculatorSumBill();
        });
        tf_bill_serviceOther.textProperty().addListener((observable, oldVal, newVal) -> {
            calculatorSumBill();
        });
        //hien thi tien dich vu khac
        try {
            int otherBill = DatabaseDriver.calculatorOtherBill();
            tf_bill_serviceOther.setText(String.valueOf(otherBill));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void calculatorSumBill() {
        if(!tf_bill_roomPrice.getText().isEmpty() && !tf_bill_serviceOther.getText().isEmpty() && !tf_bill_electricMoney.getText().isEmpty() && !tf_bill_waterMoney.getText().isEmpty()) {
            int roomPrice = Integer.parseInt(tf_bill_roomPrice.getText());
            int otherBill = Integer.parseInt(tf_bill_serviceOther.getText());
            int electricBill = Integer.parseInt(tf_bill_electricMoney.getText());
            int waterBill = Integer.parseInt(tf_bill_waterMoney.getText());
            int sumBill = roomPrice + otherBill + electricBill + waterBill;
            tf_bill_sumMoney.setText(String.valueOf(sumBill));
        }
    }

    private void calculatorWaterBill() {
        //lay ra chi so nuoc moi, cu
        int newWater = Integer.parseInt(tf_bill_newWaterIndex.getText());
        int oldWater = Integer.parseInt(tf_bill_oldWaterIndex.getText());

        Connection conn = null;
        PreparedStatement psSelectWaterPrice = null;
        ResultSet rsSelectWaterPrice = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;

            psSelectWaterPrice = conn.prepareStatement("SELECT servicePrice FROM tbl_serviceType WHERE serviceName LIKE '%nước%' OR serviceName LIKE '%nuoc%'");
            rsSelectWaterPrice = psSelectWaterPrice.executeQuery();

            if(!rsSelectWaterPrice.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Không tìm thấy dịch vụ điện hoặc nước!");
                alert.show();
            }else {
                while(rsSelectWaterPrice.next()) {
                    int rt_water_price = rsSelectWaterPrice.getInt("servicePrice");

                    //tinh tien nuoc
                    int waterBill = (newWater - oldWater) * rt_water_price;
                    tf_bill_waterMoney.setText(String.valueOf(waterBill));
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void calculatorElectricBill() {
        //lay ra chi so dien moi, cu
        int newElectric = Integer.parseInt(tf_bill_newElectricIndex.getText());
        int oldElectric = Integer.parseInt(tf_bill_oldElectricIndex.getText());

        Connection conn = null;
        PreparedStatement psSelectElectricPrice = null;
        ResultSet rsSelectElectricPrice = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;

            psSelectElectricPrice = conn.prepareStatement("SELECT servicePrice FROM tbl_serviceType WHERE serviceName LIKE '%điện%' OR serviceName LIKE '%dien%'");
            rsSelectElectricPrice = psSelectElectricPrice.executeQuery();

            if(!rsSelectElectricPrice.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Không tìm thấy dịch vụ điện hoặc nước!");
                alert.show();
            }else {
                while(rsSelectElectricPrice.next()) {
                    int rt_electric_price = rsSelectElectricPrice.getInt("servicePrice");

                    //tinh tien dien
                    int electricBill = (newElectric - oldElectric) * rt_electric_price;
                    tf_bill_electricMoney.setText(String.valueOf(electricBill));
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void onEdit(ActionEvent event) {
        if(tf_bill_roomName.getText().isEmpty() || tf_bill_roomPrice.getText().isEmpty() || tf_bill_oldElectricIndex.getText().isEmpty() ||
                tf_bill_newElectricIndex.getText().isEmpty() || tf_bill_oldWaterIndex.getText().isEmpty() || tf_bill_newWaterIndex.getText().isEmpty() ||
                dp_date_create.getValue() == null || tf_bill_boss.getText().isEmpty() || tf_bill_serviceOther.getText().isEmpty() ||
                tf_bill_electricMoney.getText().isEmpty() || tf_bill_waterMoney.getText().isEmpty() || tf_bill_sumMoney.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống!");
            alert.show();
        }
        else {
            if(rad_bill_notPay.isSelected()) {
                try {
                    if (dp_date_create.getValue().isAfter(LocalDate.now())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Ngày nhập không được lớn hơn ngày hiện tại!");
                        alert.show();
                    }else if (Integer.parseInt(tf_bill_newElectricIndex.getText()) < Integer.parseInt(tf_bill_oldElectricIndex.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Vui lòng nhập chỉ số điện mới lớn hơn chỉ số điện cũ!");
                        alert.show();
                    }else if (Integer.parseInt(tf_bill_newWaterIndex.getText()) < Integer.parseInt(tf_bill_oldWaterIndex.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Vui lòng nhập chỉ số nước mới lớn hơn chỉ số nước cũ!");
                        alert.show();
                    } else {
                        DatabaseDriver.editBill(event, tf_bill_roomName.getText(), Double.valueOf(tf_bill_roomPrice.getText()),
                                Date.valueOf(dp_date_create.getValue()), tf_bill_boss.getText(),
                                Double.valueOf(tf_bill_serviceOther.getText()), Double.valueOf(tf_bill_electricMoney.getText()),
                                Double.valueOf(tf_bill_waterMoney.getText()), Double.valueOf(tf_bill_sumMoney.getText()),
                                tf_bill_note.getText(), rad_bill_notPay.getText());
                    }
                } catch (SQLException e) {
                    System.out.println("Lỗi tạo hóa đơn " + e.getMessage());
                }
            }else {
                try {
                    if (dp_date_create.getValue().isAfter(LocalDate.now())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Ngày nhập không được lớn hơn ngày hiện tại!");
                        alert.show();
                    }else if (Integer.parseInt(tf_bill_newElectricIndex.getText()) < Integer.parseInt(tf_bill_oldElectricIndex.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Vui lòng nhập chỉ số điện mới lớn hơn chỉ số điện cũ!");
                        alert.show();
                    }else if (Integer.parseInt(tf_bill_newWaterIndex.getText()) < Integer.parseInt(tf_bill_oldWaterIndex.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Vui lòng nhập chỉ số nước mới lớn hơn chỉ số nước cũ!");
                        alert.show();
                    } else {
                        DatabaseDriver.editBill(event, tf_bill_roomName.getText(), Double.valueOf(tf_bill_roomPrice.getText()),
                                Date.valueOf(dp_date_create.getValue()), tf_bill_boss.getText(),
                                Double.valueOf(tf_bill_serviceOther.getText()), Double.valueOf(tf_bill_electricMoney.getText()),
                                Double.valueOf(tf_bill_waterMoney.getText()), Double.valueOf(tf_bill_sumMoney.getText()),
                                tf_bill_note.getText(), rad_bill_paid.getText());
                    }
                } catch (SQLException e) {
                    System.out.println("Lỗi tạo hóa đơn " + e.getMessage());
                }
            }
        }
    }

    private void onSubmit(ActionEvent event) {
        if(tf_bill_roomName.getText().isEmpty() || tf_bill_roomPrice.getText().isEmpty() || tf_bill_oldElectricIndex.getText().isEmpty() ||
            tf_bill_newElectricIndex.getText().isEmpty() || tf_bill_oldWaterIndex.getText().isEmpty() || tf_bill_newWaterIndex.getText().isEmpty() ||
            dp_date_create.getValue() == null || tf_bill_boss.getText().isEmpty() || tf_bill_serviceOther.getText().isEmpty() ||
            tf_bill_electricMoney.getText().isEmpty() || tf_bill_waterMoney.getText().isEmpty() || tf_bill_sumMoney.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống!");
            alert.show();
        }
        else {
            if(rad_bill_notPay.isSelected()) {
                try {
                    if (dp_date_create.getValue().isAfter(LocalDate.now())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Ngày nhập không được lớn hơn ngày hiện tại!");
                        alert.show();
                    }else if (Integer.parseInt(tf_bill_newElectricIndex.getText()) < Integer.parseInt(tf_bill_oldElectricIndex.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Vui lòng nhập chỉ số điện mới lớn hơn chỉ số điện cũ!");
                        alert.show();
                    }else if (Integer.parseInt(tf_bill_newWaterIndex.getText()) < Integer.parseInt(tf_bill_oldWaterIndex.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Vui lòng nhập chỉ số nước mới lớn hơn chỉ số nước cũ!");
                        alert.show();
                    } else {
                        DatabaseDriver.createBill(event, tf_bill_roomName.getText(), Double.valueOf(tf_bill_newElectricIndex.getText()),
                                Double.valueOf(tf_bill_newWaterIndex.getText()), Date.valueOf(dp_date_create.getValue()), tf_bill_boss.getText(),
                                Double.valueOf(tf_bill_serviceOther.getText()), Double.valueOf(tf_bill_electricMoney.getText()),
                                Double.valueOf(tf_bill_waterMoney.getText()), Double.valueOf(tf_bill_sumMoney.getText()),
                                tf_bill_note.getText(), rad_bill_notPay.getText(), lbl_renderName.getText());
                    }
                } catch (SQLException e) {
                    System.out.println("Lỗi tạo hóa đơn " + e.getMessage());
                }
            }else {
                try {
                    if (dp_date_create.getValue().isAfter(LocalDate.now())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Ngày nhập không được lớn hơn ngày hiện tại!");
                        alert.show();
                    }else if (Integer.parseInt(tf_bill_newElectricIndex.getText()) < Integer.parseInt(tf_bill_oldElectricIndex.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Vui lòng nhập chỉ số điện mới lớn hơn chỉ số điện cũ!");
                        alert.show();
                    }else if (Integer.parseInt(tf_bill_newWaterIndex.getText()) < Integer.parseInt(tf_bill_oldWaterIndex.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Vui lòng nhập chỉ số nước mới lớn hơn chỉ số nước cũ!");
                        alert.show();
                    } else {
                        DatabaseDriver.createBill(event, tf_bill_roomName.getText(), Double.valueOf(tf_bill_newElectricIndex.getText()),
                                Double.valueOf(tf_bill_newWaterIndex.getText()), Date.valueOf(dp_date_create.getValue()), tf_bill_boss.getText(),
                                Double.valueOf(tf_bill_serviceOther.getText()), Double.valueOf(tf_bill_electricMoney.getText()),
                                Double.valueOf(tf_bill_waterMoney.getText()), Double.valueOf(tf_bill_sumMoney.getText()),
                                tf_bill_note.getText(), rad_bill_paid.getText(), lbl_renderName.getText());
                    }
                } catch (SQLException e) {
                    System.out.println("Lỗi tạo hóa đơn " + e.getMessage());
                }
            }
        }
    }
    public void setRoomNamePrice(String roomName, BigDecimal roomPrice, int oldElectric, int oldWater, String renderName) {
        tf_bill_roomName.setText(roomName);
        tf_bill_roomPrice.setText(String.valueOf(roomPrice));
        tf_bill_oldElectricIndex.setText(String.valueOf(oldElectric));
        tf_bill_oldWaterIndex.setText(String.valueOf(oldWater));
        lbl_renderName.setText(renderName);
    }

    public void setBill (String roomName, BigDecimal roomPrice, int oldElectric, int newElectric, int oldWater, int newWater,
                         Date dateCreated, String billBoss, BigDecimal otherBill, BigDecimal electricBill, BigDecimal waterBill,
                         BigDecimal sumBill, String note, String paid, String fullName) {
        tf_bill_roomName.setText(roomName);
        tf_bill_roomPrice.setText(String.valueOf(roomPrice));
        tf_bill_oldElectricIndex.setText(String.valueOf(oldElectric));
        tf_bill_newElectricIndex.setText(String.valueOf(newElectric));
        tf_bill_oldWaterIndex.setText(String.valueOf(oldWater));
        tf_bill_newWaterIndex.setText(String.valueOf(newWater));
        dp_date_create.setValue(dateCreated.toLocalDate());
        tf_bill_boss.setText(billBoss);
        tf_bill_serviceOther.setText(String.valueOf(otherBill));
        tf_bill_electricMoney.setText(String.valueOf(electricBill));
        tf_bill_waterMoney.setText(String.valueOf(waterBill));
        tf_bill_sumMoney.setText(String.valueOf(sumBill));
        tf_bill_note.setText(note);
        if(paid.equals("Đã đóng")) {
            rad_bill_paid.setSelected(true);
        } else if (paid.equals("Chưa đóng")) {
            rad_bill_notPay.setSelected(true);
        }
        lbl_renderName.setText(fullName);
    }
}
