package com.example.quanlyphongtro.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class BillController implements Initializable {

    @FXML
    public Button btn_bill_confirm;

    @FXML
    public RadioButton rad_bill_notPay;

    @FXML
    public RadioButton rad_bill_paid;

    @FXML
    public TextField tf_bill_boss;

    @FXML
    public TextField tf_bill_electricMoney;

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
    public TextField tf_bill_paidDate;

    @FXML
    public TextField tf_bill_roomName;

    @FXML
    public TextField tf_bill_roomPrice;

    @FXML
    public TextField tf_bill_serviceOther;

    @FXML
    public TextField tf_bill_sumMoney;

    @FXML
    public TextField tf_bill_waterMoney;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
