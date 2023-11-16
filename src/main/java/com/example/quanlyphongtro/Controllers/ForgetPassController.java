package com.example.quanlyphongtro.Controllers;

import com.example.quanlyphongtro.Models.DatabaseDriver;
import com.example.quanlyphongtro.Models.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ForgetPassController implements Initializable {

    @FXML
    public Button btn_back;

    @FXML
    public Button btn_update;

    @FXML
    public ComboBox<String> combo_QA;

    @FXML
    public FontAwesomeIconView icon_back;

    @FXML
    public TextField tf_QA;

    @FXML
    public TextField tf_enter_new_pass;

    @FXML
    public TextField tf_new_pass;

    @FXML
    public TextField tf_username;

    private void onMoveToLogin() {
        Stage stage = (Stage) btn_back.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_back.setOnAction(e -> onMoveToLogin());

        combo_QA.getItems().addAll(
                "Tên thành phố hoặc thị trấn mà bạn sinh ra?",
                "Tên trường trung học của bạn là gì?",
                "Tên người bạn thân nhất là gì?",
                "Ngày sinh nhật của cha mẹ bạn là gì?",
                "Tên thú cưng đầu tiên của bạn là gì?",
                "Tên trường đại học hoặc cao đẳng mà bạn từng theo học là gì?"
        );

        btn_update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_enter_new_pass.getText().equals(tf_new_pass.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Mật khẩu nhập lại không khớp!");
                    alert.show();
                } else {
                    String passwd = SignupController.getMD5Key(tf_new_pass.getText());
                    DatabaseDriver.updatePass(event, tf_username.getText(), combo_QA.getValue(), tf_QA.getText(), passwd);
                }
            }
        });
    }
}
