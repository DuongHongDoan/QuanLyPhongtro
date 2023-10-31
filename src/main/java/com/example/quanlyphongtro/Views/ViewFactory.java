package com.example.quanlyphongtro.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {
    //host view
    private AnchorPane dashboardView;
    private AnchorPane manageRoomView;
    private AnchorPane manageCustomerView;
    private AnchorPane profileView;


    //host, load cac view len lan dau, cac lan sau khong can load lai
    //tai view trang chu
    public AnchorPane getDashboardView() {
        if(dashboardView == null) {
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/FXMLs/Host/Dashboard.fxml")).load();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return dashboardView;
    }

    //tai view quan ly phong
    public AnchorPane getManageRoomView() {
        if(manageRoomView == null) {
            try {
                manageRoomView = new FXMLLoader(getClass().getResource("/FXMLs/Host/ManageRoom.fxml")).load();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return manageRoomView;
    }

    //tai view quan ly khach thue
    public AnchorPane getManageCustomerView() {
        if(manageCustomerView == null) {
            try {
                manageCustomerView = new FXMLLoader(getClass().getResource("/FXMLs/Host/ManageCustomer.fxml")).load();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return manageCustomerView;
    }

    //tai view xem profile
    public AnchorPane getProfileView() {
        if(profileView == null) {
            try {
                profileView = new FXMLLoader(getClass().getResource("/FXMLs/Host/Profile.fxml")).load();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return profileView;
    }


    //cac view login, signup, resetPass, checkIn
    //tao stage chung
    Stage stage;
    public void createStage(FXMLLoader loader, String title){
        Scene scene = null;

        try {
            scene = new Scene(loader.load());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        stage = new Stage();
        stage.setTitle(title);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setScene(scene);
        stage.show();
    }

    //dong 1 stage lai khi co 1 stage khac xuat hien
    public void closeStage(Stage stage) {
        stage.close();
    }

    //show cua so login
    public void showLoginWindow() {
        FXMLLoader login = new FXMLLoader(getClass().getResource("/FXMLs/Login.fxml"));
        createStage(login, "Đăng nhập");
    }

    //show cua so SignIn
    public void showSignupWindow() {
        FXMLLoader signUp = new FXMLLoader(getClass().getResource("/FXMLs/Signup.fxml"));
        createStage(signUp, "Đăng ký");
    }

    //show cua so quen mat khau
    public void showForgetPassWindow() {
        FXMLLoader forgetPass = new FXMLLoader(getClass().getResource("/FXMLs/ForgetPass.fxml"));
        createStage(forgetPass, "Quên mật khẩu");
    }

    //show cua so checkIn
    public void showCheckInWindow() {
        FXMLLoader checkIn = new FXMLLoader(getClass().getResource("/FXMLs/CheckIn.fxml"));
        createStage(checkIn, "Đăng ký phòng");
        stage.setMinHeight(550);
        stage.setMinWidth(680);
    }
}

