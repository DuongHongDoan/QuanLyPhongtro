package com.example.quanlyphongtro.Models;

import com.example.quanlyphongtro.Controllers.BillController;
import com.example.quanlyphongtro.Controllers.MainViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Objects;

public class DatabaseDriver {
    //chuyen scene khi nguoi dung login vao trang quan ly
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String gender, String email, String phone) {
        Parent root = null;

        if(username != null){
            try {
                FXMLLoader loader = new FXMLLoader(DatabaseDriver.class.getResource(fxmlFile));
                root = loader.load();
                MainViewController mainViewController = loader.getController();
                mainViewController.setUserLabel(username, gender, email, phone);
                mainViewController.btn_home.setStyle("-fx-background-color: linear-gradient(to bottom right, #17EA9D, #6078EA);" +
                        "-fx-effect: dropshadow(three-pass-box, #D3D3D3, 0, 0, 0, 0);");
                mainViewController.btn_profile.setStyle("-fx-background-color: transparent;");
                mainViewController.btn_manage_renter.setStyle("-fx-background-color: transparent;");
                mainViewController.btn_manage_room.setStyle("-fx-background-color: transparent;");
                mainViewController.btn_logout.setStyle("-fx-background-color: transparent;");
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(DatabaseDriver.class.getResource(fxmlFile)));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    // dang ky nguoi dung -> luu vao co so du lieu
    public static void signupUser(ActionEvent event, String fullname, String username, String gender,
                                  String phone, String email, String passwd, String security_question, String security_answer) {
        Connection conn = null;
        PreparedStatement psCheckUserExist = null;
        PreparedStatement psInsert = null;
        ResultSet resultSet = null;


        try {
            //ket noi database
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheckUserExist = conn.prepareStatement("SELECT * FROM tbl_accounts WHERE username = ?");
            psCheckUserExist.setString(1, username);
            //dua dl da truy van vao bo kq ResultSet
            resultSet = psCheckUserExist.executeQuery();

            if(resultSet.isBeforeFirst()) {//kiem tra resultSet co du lieu khong, mac dinh la true
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Người dùng đã tồn tại!");
                alert.show();
            }
            else {
                psInsert = conn.prepareStatement("INSERT INTO tbl_accounts (fullname, username, gender, phone, email, passwd, security_question, security_answer) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, fullname);
                psInsert.setString(2, username);
                psInsert.setString(3, gender);
                psInsert.setString(4, phone);
                psInsert.setString(5, email);
                psInsert.setString(6, passwd);
                psInsert.setString(7, security_question);
                psInsert.setString(8, security_answer);
                psInsert.executeUpdate();

                changeScene(event, "/FXMLs/Login.fxml", "Đăng nhập", username, gender, email, phone);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Đăng ký thành công!");
                alert.show();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psInsert != null) {
                try{
                    psInsert.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheckUserExist != null) {
                try{
                    psCheckUserExist.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //login nguoi dung vao chat room, co kiem tra username va passwd
    public static void loginUser (ActionEvent event, String username, String passwd) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //ket noi database
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            preparedStatement = conn.prepareStatement("SELECT username, gender, phone, email, passwd FROM tbl_accounts WHERE username = ?");
            preparedStatement.setString(1, username);
            //dua dl da truy van vao bo kq ResultSet
            resultSet = preparedStatement.executeQuery();

            if(username.isEmpty() || passwd.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Vui lòng điền thông tin!");
                alert.show();
            }
            else{
                if(!resultSet.isBeforeFirst()) {//kiem tra resultSet co du lieu khong
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Người dùng không tồn tại!");
                    alert.show();
                }
                else {
                    while (resultSet.next()){//lap qua all du lieu co trong tap du lieu da tim thay
                        String retrievePasswd = resultSet.getString("passwd");
                        String rt_username = resultSet.getString("username");
                        String rt_gender = resultSet.getString("gender");
                        String rt_phone = resultSet.getString("phone");
                        String rt_email = resultSet.getString("email");

                        if(retrievePasswd.equals(passwd)){
                            changeScene(event, "/FXMLs/MainView.fxml", "Quản lý phòng trọ", rt_username, rt_gender, rt_email, rt_phone);
//                            showInfoUser("/FXMLs/Host/Profile.fxml", rt_username, rt_gender, rt_email, rt_phone);
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Mật khẩu không khớp!");
                            alert.show();
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //cap nhat lai thong tin nguoi dung
    public static void updateUserInfo (ActionEvent event, String old_user, String username, String gender, String email, String phone) {
        Connection conn = null;
        PreparedStatement psCheckUserExist = null;
        PreparedStatement psInsert = null;
        ResultSet resultSet = null;

        try {
            //ket noi database
            conn = ConnectDB.connectDB();
            assert conn != null;
            //chuan bi cau lenh truy van SQL
            psCheckUserExist = conn.prepareStatement("SELECT * FROM tbl_accounts WHERE username = ?");
            psCheckUserExist.setString(1, old_user);
            //dua dl da truy van vao bo kq ResultSet
            resultSet = psCheckUserExist.executeQuery();

            while (resultSet.next()) {//lap qua all du lieu co trong tap du lieu da tim thay
                String rt_username = resultSet.getString("username");
                String rt_gender = resultSet.getString("gender");
                String rt_phone = resultSet.getString("phone");
                String rt_email = resultSet.getString("email");
                if(username.equals(rt_username) && phone.equals(rt_phone) && email.equals(rt_email) && gender.equals(rt_gender)) {//kiem tra resultSet co du lieu khong, mac dinh la true
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Nội dung không thay đổi!");
                    alert.show();
                }
                else {
                    psInsert = conn.prepareStatement("UPDATE tbl_accounts SET username = ?, gender = ?, email = ?, phone = ? WHERE username = ?");
                    psInsert.setString(1, username);
                    psInsert.setString(2, gender);
                    psInsert.setString(3, email);
                    psInsert.setString(4, phone);
                    psInsert.setString(5, old_user);
                    psInsert.executeUpdate();

                    changeScene(event, "/FXMLs/MainView.fxml", "Quản lý phòng trọ", username, gender, email, phone);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Đã cập nhật thành công!");
                    alert.show();
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psInsert != null) {
                try{
                    psInsert.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheckUserExist != null) {
                try{
                    psCheckUserExist.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //xoa tai khoan
    public static void delUser (ActionEvent event, String username) {
        Connection conn = null;
        PreparedStatement psCheckUserExist = null;

        try {
            //ket noi database
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheckUserExist = conn.prepareStatement("DELETE FROM tbl_accounts WHERE username = ?");
            psCheckUserExist.setString(1, username);
            psCheckUserExist.executeUpdate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(psCheckUserExist != null) {
                try{
                    psCheckUserExist.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }


    //thay doi pass khi quen
    public static void updatePass(ActionEvent event, String username, String security_question, String security_answer,
                                  String passwd) {
        Connection conn = null;
        PreparedStatement psCheckUser = null;
        PreparedStatement psInsert = null;
        ResultSet resultSet = null;

        try {
            //ket noi database
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheckUser = conn.prepareStatement("SELECT * FROM tbl_accounts WHERE username = ?");
            psCheckUser.setString(1, username);
            //dua dl da truy van vao bo kq ResultSet
            resultSet = psCheckUser.executeQuery();

            if(username.isEmpty() || security_question.isEmpty() || security_answer.isEmpty() || passwd.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Vui lòng điền thông tin!");
                alert.show();
            }
            else {
                if(!resultSet.isBeforeFirst()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Người dùng không tồn tại!");
                    alert.show();
                }
                else {
                    while (resultSet.next()) {//lap qua all du lieu co trong tap du lieu da tim thay
                        String rt_username = resultSet.getString("username");
                        String rt_security_question = resultSet.getString("security_question");
                        String rt_security_answer = resultSet.getString("security_answer");
                        String rt_passwd = resultSet.getString("passwd");

                        if(!username.equals(rt_username) || !security_question.equals(rt_security_question) || !security_answer.equals(rt_security_answer)){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Thông tin không đúng!");
                            alert.show();
                        } else if (passwd.equals(rt_passwd)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Mật khẩu không thay đổi!");
                            alert.show();
                        } else {
                            psInsert = conn.prepareStatement("UPDATE tbl_accounts SET passwd = ? WHERE username = ?");
                            psInsert.setString(1, passwd);
                            psInsert.setString(2, username);
                            psInsert.executeUpdate();

                            changeScene(event, "/FXMLs/Login.fxml", "Đăng nhập", username, "", "", "");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Cập nhật mật khẩu thành công!");
                            alert.show();
                        }
                    }
                }
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psInsert != null) {
                try{
                    psInsert.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheckUser != null) {
                try{
                    psCheckUser.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //thong ke so phong da thue
    public static String getTotalRendRoom () throws SQLException {
        Connection conn = null;
        PreparedStatement psSelect = null;
        PreparedStatement psSelectTotal = null;
        ResultSet rsSelect = null;
        ResultSet rsSelectTotal = null;
        int cnt = 0, cntTotal = 0;
        String kq = "";

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psSelect = conn.prepareStatement("SELECT COUNT(*) AS total1 FROM tbl_room WHERE roomStatus = 'Đã thuê'");
            rsSelect = psSelect.executeQuery();
            psSelectTotal = conn.prepareStatement("SELECT COUNT(*) AS total2 FROM tbl_room");
            rsSelectTotal = psSelectTotal.executeQuery();

            while(rsSelect.next() && rsSelectTotal.next()) {
                cnt = rsSelect.getInt("total1");
                cntTotal = rsSelectTotal.getInt("total2");
                kq = String.format("%d/%d", cnt, cntTotal);
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelectTotal != null){
                rsSelectTotal.close();
            }
            if(rsSelect != null){
                rsSelect.close();
            }
            if(psSelectTotal != null) {
                psSelectTotal.close();
            }
            if(psSelect != null) {
                psSelect.close();
            }
            if(conn != null) {
                conn.close();
            }
        }

        return kq;
    }

    //thong ke so nguoi thue tro
    public static int getTotalRender () throws SQLException {
        Connection conn = null;
        PreparedStatement psSelect = null;
        ResultSet rsSelect = null;
        int cnt = 0;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psSelect = conn.prepareStatement("SELECT COUNT(*) AS total FROM tbl_infoRender JOIN tbl_rendRoom ON tbl_infoRender.id_render = tbl_rendRoom.id_render JOIN tbl_room ON tbl_rendRoom.id_room = tbl_room.id_room");
            rsSelect = psSelect.executeQuery();

            while(rsSelect.next()) {
                cnt = rsSelect.getInt("total");
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelect != null){
                rsSelect.close();
            }
            if(psSelect != null) {
                psSelect.close();
            }
            if(conn != null) {
                conn.close();
            }
        }

        return cnt;
    }

    //nut "them", "sua", "xoa" du lieu cho tbl_roomType o ManageRoom
    public static void addRoomType(ActionEvent event, String roomTypeName, Double roomPrice) {
        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheck = null;
        ResultSet resultSet = null;

        try {
            //ket noi database
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheck = conn.prepareStatement("SELECT * FROM tbl_roomType WHERE roomTypeName = ?");
            psCheck.setString(1, roomTypeName);
            resultSet = psCheck.executeQuery();

            if(resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Loại phòng đã tồn tại!");
                alert.show();
            }
            else {
                psInsert = conn.prepareStatement("INSERT INTO tbl_roomType (roomTypeName, roomPrice) VALUES (?, ?)");
                psInsert.setString(1, roomTypeName);
                psInsert.setDouble(2, roomPrice);
                psInsert.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Thêm thành công!");
                alert.show();
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(psInsert != null) {
                try{
                    psInsert.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheck != null) {
                try{
                    psCheck.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void editRoomType(ActionEvent event, String roomTypeName, String roomPrice) {
        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheck = null;
        ResultSet resultSet = null;

        try {
            //ket noi database
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheck = conn.prepareStatement("SELECT * FROM tbl_roomType WHERE roomTypeName = ?");
            psCheck.setString(1, roomTypeName);
            resultSet = psCheck.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Loại phòng không tồn tại!");
                alert.show();
            }
            else {
                while(resultSet.next()){
                    String rt_roomPrice = resultSet.getString("roomPrice");
                    String rt_roomTypeName = resultSet.getString("roomTypeName");

                    if(roomTypeName.equals(rt_roomTypeName)){
                        if(roomPrice.equals(rt_roomPrice)){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Nội dung không thay đổi!");
                            alert.show();
                        }
                        else {
                            psInsert = conn.prepareStatement("UPDATE tbl_roomType SET roomPrice = ? WHERE roomTypeName = ?");
                            psInsert.setString(1, roomPrice);
                            psInsert.setString(2, roomTypeName);
                            psInsert.executeUpdate();

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Sửa giá thành công!");
                            alert.show();
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(psInsert != null) {
                try{
                    psInsert.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheck != null) {
                try{
                    psCheck.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void delRoomType(ActionEvent event, String roomTypeName) {
        Connection conn = null;
        PreparedStatement psCheckRoom = null;
        PreparedStatement psCheck = null;
        PreparedStatement psSelect = null;
        ResultSet resultSet = null;

        try {
            //ket noi database
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheck = conn.prepareStatement("SELECT id_roomType, roomTypeName FROM tbl_roomType WHERE roomTypeName = ?");
            psCheck.setString(1, roomTypeName);
            resultSet = psCheck.executeQuery();


            if(resultSet.isBeforeFirst()){
                while(resultSet.next()){
                    psCheckRoom = conn.prepareStatement("DELETE FROM tbl_roomType WHERE roomTypeName = ?");
                    psCheckRoom.setString(1, roomTypeName);
                    psCheckRoom.executeUpdate();
                    String rt_id = resultSet.getString("id_roomType");
                    psSelect = conn.prepareStatement("DELETE FROM tbl_room WHERE id_roomType = ?");
                    psSelect.setString(1, rt_id);
                    psSelect.executeUpdate();


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Xóa loại phòng thành công!");
                    alert.show();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Loại phòng không tồn tại!");
                alert.show();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(psSelect != null) {
                try{
                    psSelect.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheckRoom != null) {
                try{
                    psCheckRoom.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheck != null) {
                try{
                    psCheck.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //lay du lieu loai phong de gan vao comboBox
    public static ObservableList<roomTypeData> getListLoaiPhong() throws SQLException {
        // Tạo một kết nối đến database
        Connection conn = null;
        PreparedStatement psCheck = null;
        ResultSet resultSet = null;
        ObservableList<roomTypeData> listLoaiPhong = FXCollections.observableArrayList();
        try {
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheck = conn.prepareStatement("SELECT roomTypeName, roomPrice FROM tbl_roomType");
            resultSet = psCheck.executeQuery();
            while(resultSet.next()) {
                String rt_roomTypeName = resultSet.getString("roomTypeName");
                Double rt_roomPrice = resultSet.getDouble("roomPrice");
                listLoaiPhong.add(new roomTypeData(rt_roomTypeName, rt_roomPrice));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheck != null) {
                try{
                    psCheck.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return listLoaiPhong;
    }

    //nut them, sua va xoa du lieu Phong
    //nut them phong
    public static void addRoom(ActionEvent event, String roomName, String roomStatus, String roomTypeName) {
        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheck = null;
        PreparedStatement psCheckLP = null;
        ResultSet resultSet = null;
        ResultSet resultSetLP = null;

        try {
            //ket noi database
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheckLP = conn.prepareStatement("SELECT id_roomType FROM tbl_roomType WHERE roomTypeName = ?");
            psCheckLP.setString(1, roomTypeName);
            resultSetLP = psCheckLP.executeQuery();

            psCheck = conn.prepareStatement("SELECT * FROM tbl_room WHERE roomName = ?");
            psCheck.setString(1, roomName);
            resultSet = psCheck.executeQuery();

            if(resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Tên phòng đã tồn tại!");
                alert.show();
            }
            else {
                while(resultSetLP.next()){
                    int rt_id_roomType = resultSetLP.getInt("id_roomType");
                    psInsert = conn.prepareStatement("INSERT INTO tbl_room (roomName, roomStatus, id_roomType) VALUES (?, ?, ?)");
                    psInsert.setString(1, roomName);
                    psInsert.setString(2, roomStatus);
                    psInsert.setInt(3, rt_id_roomType);
                    psInsert.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Thêm phòng thành công!");
                    alert.show();
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(psInsert != null) {
                try{
                    psInsert.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(resultSetLP != null) {
                try{
                    resultSetLP.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheck != null) {
                try{
                    psCheck.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheckLP != null) {
                try{
                    psCheckLP.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //nut sua Phong
    public static void editRoom(ActionEvent event, String roomName, String roomTypeName, String roomStatus) {
        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheck = null;
        PreparedStatement psCheckRoom = null;
        PreparedStatement psCheckLP = null;
        ResultSet resultSet = null;
        ResultSet resultSetRoom = null;
        ResultSet resultSetLP = null;

        try {
            //ket noi database
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheckLP = conn.prepareStatement("SELECT id_roomType FROM tbl_roomType WHERE roomTypeName = ?");
            psCheckLP.setString(1, roomTypeName);
            resultSetLP = psCheckLP.executeQuery();

            psCheckRoom = conn.prepareStatement("SELECT * FROM tbl_room WHERE roomName = ?");
            psCheckRoom.setString(1, roomName);
            resultSetRoom = psCheckRoom.executeQuery();

            psCheck = conn.prepareStatement("SELECT roomTypeName, roomStatus FROM tbl_roomType JOIN tbl_room ON tbl_roomType.id_roomType = tbl_room.id_roomType AND roomName = ?");
            psCheck.setString(1, roomName);
            resultSet = psCheck.executeQuery();

            if(!resultSetRoom.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Phòng không tồn tại!");
                alert.show();
            }
            else {
                while(resultSet.next()){
                    String rt_roomStatus = resultSet.getString("roomStatus");
                    String rt_roomTypeName = resultSet.getString("roomTypeName");

                    if(roomTypeName.equals(rt_roomTypeName) && roomStatus.equals(rt_roomStatus)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Nội dung không thay đổi!");
                        alert.show();
                    }
                    else {
                        while(resultSetLP.next()){
                            String rt_id_roomTypeName = resultSetLP.getString("id_roomType");
                            psInsert = conn.prepareStatement("UPDATE tbl_room SET roomStatus = ?, id_roomType = ? WHERE roomName = ?");
                            psInsert.setString(1, roomStatus);
                            psInsert.setString(2, rt_id_roomTypeName);
                            psInsert.setString(3, roomName);
                            psInsert.executeUpdate();

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Sửa phòng thành công!");
                            alert.show();
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(psInsert != null) {
                try{
                    psInsert.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(resultSetLP != null) {
                try{
                    resultSetLP.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(resultSetRoom != null) {
                try{
                    resultSetRoom.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheck != null) {
                try{
                    psCheck.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheckLP != null) {
                try{
                    psCheckLP.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheckRoom != null) {
                try{
                    psCheckRoom.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //nut xoa Phong
    public static void delRoom(ActionEvent event, String roomName) {
        Connection conn = null;
        PreparedStatement psCheckRoom = null;
        PreparedStatement psCheckRoomName = null;
        ResultSet resultSet = null;

        try {
            //ket noi database
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheckRoomName = conn.prepareStatement("SELECT roomName FROM tbl_room WHERE roomName = ?");
            psCheckRoomName.setString(1, roomName);
            resultSet = psCheckRoomName.executeQuery();

            if(resultSet.isBeforeFirst()){
                psCheckRoom = conn.prepareStatement("DELETE FROM tbl_room WHERE roomName = ?");
                psCheckRoom.setString(1, roomName);
                psCheckRoom.executeUpdate();


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Xóa phòng thành công!");
                alert.show();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Phòng không tồn tại!");
                alert.show();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(psCheckRoom != null) {
                try{
                    psCheckRoom.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //hien thi du lieu ra tableView
    public static ObservableList<roomData> getRoomList() throws SQLException {
        Connection conn = null;
        PreparedStatement psCheck = null;
        ResultSet resultSet = null;
        ObservableList<roomData> roomList = FXCollections.observableArrayList();

        try {
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheck = conn.prepareStatement("SELECT roomName, roomTypeName, roomStatus, roomPrice FROM tbl_roomType JOIN tbl_room ON tbl_roomType.id_roomType = tbl_room.id_roomType");
            resultSet = psCheck.executeQuery();

            while(resultSet.next()) {
                String rt_roomName = resultSet.getString("roomName");
                String rt_roomTypeName = resultSet.getString("roomTypeName");
                String rt_roomStatus = resultSet.getString("roomStatus");
                BigDecimal rt_roomPrice = resultSet.getBigDecimal("roomPrice");
                roomList.add(new roomData(rt_roomName, rt_roomTypeName, rt_roomStatus, rt_roomPrice));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheck != null) {
                try{
                    psCheck.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return roomList;
    }

    //form checkIn
    public static void checkIn(ActionEvent event, String renderName, String gender, String CCCD, String phone,
                               String homeTown, String roomName, Double deposit, Date rendDate,
                               int initElectric, int initWater) throws SQLException {
        Connection conn = null;
        PreparedStatement psInsertRender = null, psInsertElectricService = null, psInsertWaterService = null;
        PreparedStatement psSelectIDRoom = null, psSelectIdElectric = null, psSelectIdWater = null;
        PreparedStatement psSelectIDRender = null;
        PreparedStatement psEditStatus = null;
        PreparedStatement psInsertRendRoom = null;
        ResultSet rsIDRoom = null, rsSelectIdElectric = null, rsSelectIdWater = null;
        ResultSet rsIDRender = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psInsertRender = conn.prepareStatement("INSERT INTO tbl_infoRender (fullname, gender, CCCD, phone, homeTown) VALUES (?, ?, ?, ?, ?)");
            psInsertRender.setString(1, renderName);
            psInsertRender.setString(2, gender);
            psInsertRender.setString(3, CCCD);
            psInsertRender.setString(4, phone);
            psInsertRender.setString(5, homeTown);
            psInsertRender.executeUpdate();

            psSelectIDRoom = conn.prepareStatement("SELECT id_room FROM tbl_room WHERE roomName = ?");
            psSelectIDRoom.setString(1, roomName);
            rsIDRoom = psSelectIDRoom.executeQuery();

            psSelectIDRender = conn.prepareStatement("SELECT id_render FROM tbl_infoRender WHERE fullname = ?");
            psSelectIDRender.setString(1, renderName);
            rsIDRender = psSelectIDRender.executeQuery();

            //lay id loai dich vu dien, nuoc
            psSelectIdElectric = conn.prepareStatement("SELECT id_serviceType FROM tbl_serviceType WHERE serviceName LIKE '%điện%' OR serviceName LIKE '%dien%'");
            rsSelectIdElectric = psSelectIdElectric.executeQuery();
            psSelectIdWater = conn.prepareStatement("SELECT id_serviceType FROM tbl_serviceType WHERE serviceName LIKE '%nước%' OR serviceName LIKE '%nuoc%'");
            rsSelectIdWater = psSelectIdWater.executeQuery();

            while(rsIDRoom.next() && rsIDRender.next() && rsSelectIdElectric.next() && rsSelectIdWater.next()) {
                int rt_id_room = rsIDRoom.getInt("id_room");
                int rt_id_render = rsIDRender.getInt("id_render");
                int rt_id_electric = rsSelectIdElectric.getInt("id_serviceType");
                int rt_id_water = rsSelectIdWater.getInt("id_serviceType");

                psInsertRendRoom = conn.prepareStatement("INSERT INTO tbl_rendRoom (check_in_date, deposit, init_electric_indicator, init_water_indicator, id_room, id_render) VALUES (?, ?, ?, ?, ?, ?)");
                psInsertRendRoom.setDate(1, rendDate);
                psInsertRendRoom.setDouble(2, deposit);
                psInsertRendRoom.setInt(3, initElectric);
                psInsertRendRoom.setInt(4, initWater);
                psInsertRendRoom.setInt(5, rt_id_room);
                psInsertRendRoom.setInt(6, rt_id_render);
                psInsertRendRoom.executeUpdate();

                psEditStatus = conn.prepareStatement("UPDATE tbl_room SET roomStatus = 'Đã thuê' WHERE roomName = ?");
                psEditStatus.setString(1, roomName);
                psEditStatus.executeUpdate();

                psInsertElectricService = conn.prepareStatement("INSERT INTO tbl_service (new_indicator, old_indicator, id_serviceType, id_room) VALUES (?, ?, ?, ?)");
                psInsertElectricService.setInt(1, initElectric);
                psInsertElectricService.setInt(2, initElectric);
                psInsertElectricService.setInt(3, rt_id_electric);
                psInsertElectricService.setInt(4, rt_id_room);
                psInsertElectricService.executeUpdate();
                psInsertWaterService = conn.prepareStatement("INSERT INTO tbl_service (new_indicator, old_indicator, id_serviceType, id_room) VALUES (?, ?, ?, ?)");
                psInsertWaterService.setInt(1, initWater);
                psInsertWaterService.setInt(2, initWater);
                psInsertWaterService.setInt(3, rt_id_water);
                psInsertWaterService.setInt(4, rt_id_room);
                psInsertWaterService.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Thêm người thuê thành công!");
                alert.show();
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if (rsIDRender != null && rsIDRoom != null && rsSelectIdElectric != null && rsSelectIdWater != null) {
                rsIDRender.close();
                rsIDRoom.close();
                rsSelectIdElectric.close();
                rsSelectIdWater.close();
            }
            if (psInsertRendRoom != null && psEditStatus != null && psSelectIDRender != null && psSelectIDRoom != null &&
                    psInsertRender != null && psInsertElectricService != null && psInsertWaterService != null &&
                    psSelectIdElectric != null && psSelectIdWater != null) {
                psInsertRendRoom.close();
                psEditStatus.close();
                psSelectIDRender.close();
                psSelectIDRoom.close();
                psInsertRender.close();
                psInsertElectricService.close();
                psInsertWaterService.close();
                psSelectIdElectric.close();
                psSelectIdWater.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
    }

    //lay du lieu phong trong, hien thi ten phong ra comboBox trong form checkIn
    public static ObservableList<roomData> getEmptyRoomList() throws SQLException {
        Connection conn = null;
        PreparedStatement psCheck = null;
        ResultSet resultSet = null;
        ObservableList<roomData> emptyRoomList = FXCollections.observableArrayList();

        try {
            conn = ConnectDB.connectDB();
            //chuan bi cau lenh truy van SQL
            assert conn != null;
            psCheck = conn.prepareStatement("SELECT roomName, roomTypeName FROM tbl_roomType JOIN tbl_room ON tbl_roomType.id_roomType = tbl_room.id_roomType WHERE roomStatus = 'Phòng trống'");
            resultSet = psCheck.executeQuery();

            while(resultSet.next()) {
                String rt_roomName = resultSet.getString("roomName");
                String rt_roomTypeName = resultSet.getString("roomTypeName");
                emptyRoomList.add(new roomData(rt_roomName, rt_roomTypeName));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally { //dong cac ket noi va kq cua truy van csdl
            if(resultSet != null) {
                try{
                    resultSet.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(psCheck != null) {
                try{
                    psCheck.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return emptyRoomList;
    }

//    //lay du lieu khach thue phong hien thi len view quan ly khach thue
    public static ObservableList<InfoRendRoomData> getRenderList() throws SQLException {
        Connection conn = null;
        PreparedStatement psSelect = null;
        ResultSet rsSelect = null;
        ObservableList<InfoRendRoomData> renderList = FXCollections.observableArrayList();

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psSelect = conn.prepareStatement("SELECT fullname, gender, CCCD, phone, homeTown, check_in_date, roomName FROM tbl_infoRender JOIN tbl_rendRoom ON tbl_infoRender.id_render = tbl_rendRoom.id_render JOIN tbl_room ON tbl_rendRoom.id_room = tbl_room.id_room");
            rsSelect = psSelect.executeQuery();

            while(rsSelect.next()) {
                String rt_fullName = rsSelect.getString("fullname");
                String rt_gender = rsSelect.getString("gender");
                String rt_CCCD = rsSelect.getString("CCCD");
                String rt_phone = rsSelect.getString("phone");
                String rt_homeTown = rsSelect.getString("homeTown");
                Date rt_checkInDate = rsSelect.getDate("check_in_date");
                String rt_roomName = rsSelect.getString("roomName");

                renderList.add(new InfoRendRoomData(rt_fullName, rt_CCCD, rt_gender, rt_homeTown, rt_phone, rt_checkInDate, rt_roomName));
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelect != null) {
                rsSelect.close();
            }
            if(psSelect != null) {
                psSelect.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
        return renderList;
    }

    //nut sua thong tin render
    public static void editInfo(ActionEvent event, String oldName, String name, String cccd, String homeTown, String phone) {
        Connection conn = null;
        PreparedStatement psSelect = null;
        PreparedStatement psEdit = null;
        ResultSet rsSelect = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psSelect = conn.prepareStatement("SELECT fullname, CCCD, homeTown, phone FROM tbl_infoRender WHERE fullname = ?");
            psSelect.setString(1, oldName);
            rsSelect = psSelect.executeQuery();

            if(!rsSelect.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Khách thuê không tồn tại!");
                alert.show();
            }
            else {
                while(rsSelect.next()) {
                    String rt_name = rsSelect.getString("fullname");
                    String rt_cccd = rsSelect.getString("CCCD");
                    String rt_homeTown = rsSelect.getString("homeTown");
                    String rt_phone = rsSelect.getString("phone");

                    if(name.equals(rt_name) && cccd.equals(rt_cccd) && homeTown.equals(rt_homeTown) && phone.equals(rt_phone)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Nội dung không có sự thay đổi!");
                        alert.show();
                    }
                    else {
                        psEdit = conn.prepareStatement("UPDATE tbl_infoRender SET fullname = ?, CCCD = ?, homeTown = ?, phone = ? WHERE fullname = ?");
                        psEdit.setString(1, name);
                        psEdit.setString(2, cccd);
                        psEdit.setString(3, homeTown);
                        psEdit.setString(4, phone);
                        psEdit.setString(5, oldName);
                        psEdit.executeUpdate();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Cập nhật thành công!");
                        alert.show();
                    }
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelect != null) {
                try {
                    rsSelect.close();
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(psEdit != null) {
                try {
                    psEdit.close();
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(psSelect != null) {
                try {
                    psSelect.close();
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //nut xoa thong tin render
    public static void delInfo(ActionEvent event, String name, String cccd, String homeTown, String phone) {
        Connection conn = null;
        PreparedStatement psSelect = null;
        PreparedStatement psDel = null;
        ResultSet rsSelect = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psSelect = conn.prepareStatement("SELECT fullname, CCCD, homeTown, phone FROM tbl_infoRender WHERE fullname = ? AND CCCD = ? AND homeTown = ? AND phone = ?");
            psSelect.setString(1, name);
            psSelect.setString(2, cccd);
            psSelect.setString(3, homeTown);
            psSelect.setString(4, phone);
            rsSelect = psSelect.executeQuery();

            if(!rsSelect.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Khách thuê không tồn tại!");
                alert.show();
            }
            else {
                psDel = conn.prepareStatement("DELETE FROM tbl_infoRender WHERE fullname = ?");
                psDel.setString(1, name);
                psDel.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Xóa thành công!");
                alert.show();
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelect != null){
                try {
                    rsSelect.close();
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(psDel != null){
                try {
                    psDel.close();
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(psSelect != null){
                try {
                    psSelect.close();
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null){
                try {
                    conn.close();
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

//add, sua, xoa dich vu
    //nut them dich vu
    public static void addService(ActionEvent event, String serviceName, Double servicePrice) {
        Connection conn = null;
        PreparedStatement psSelect = null;
        PreparedStatement psInsert = null;
        ResultSet rsSelect = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psSelect = conn.prepareStatement("SELECT serviceName FROM tbl_serviceType WHERE serviceName = ?");
            psSelect.setString(1, serviceName);
            rsSelect = psSelect.executeQuery();

            if(rsSelect.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Tên dịch vụ đã tồn tại!");
                alert.show();
            }else {
                psInsert = conn.prepareStatement("INSERT INTO tbl_serviceType (serviceName, servicePrice) VALUES (?, ?)");
                psInsert.setString(1, serviceName);
                psInsert.setDouble(2, servicePrice);
                psInsert.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Thêm dịch vụ thành công!");
                alert.show();
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelect != null) {
                try {
                    rsSelect.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(psSelect != null) {
                try {
                    psSelect.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //nut sua dich vu
    public static void editService(ActionEvent event, String serviceName, Double servicePrice) {
        Connection conn = null;
        PreparedStatement psSelect = null;
        PreparedStatement psInsert = null;
        ResultSet rsSelect = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psSelect = conn.prepareStatement("SELECT serviceName, servicePrice FROM tbl_serviceType WHERE serviceName = ?");
            psSelect.setString(1, serviceName);
            rsSelect = psSelect.executeQuery();

            if(!rsSelect.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Dịch vụ không tồn tại!");
                alert.show();
            }else {
                while(rsSelect.next()) {
                    String rt_Name = rsSelect.getString("serviceName");
                    Double rt_Price = rsSelect.getDouble("servicePrice");
                    if(rt_Name.equals(serviceName) && rt_Price.equals(servicePrice)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Nội dung không thay đổi!");
                        alert.show();
                    }else {
                        psInsert = conn.prepareStatement("UPDATE tbl_serviceType SET servicePrice = ? WHERE serviceName = ?");
                        psInsert.setDouble(1, servicePrice);
                        psInsert.setString(2, serviceName);
                        psInsert.executeUpdate();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Cập nhật dịch vụ thành công!");
                        alert.show();
                    }
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelect != null) {
                try {
                    rsSelect.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(psSelect != null) {
                try {
                    psSelect.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //nut xoa dich vu
    public static void delService(ActionEvent event, String serviceName) {
        Connection conn = null;
        PreparedStatement psSelect = null;
        PreparedStatement psDel = null;
        ResultSet rsSelect = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psSelect = conn.prepareStatement("SELECT serviceName FROM tbl_serviceType WHERE serviceName = ?");
            psSelect.setString(1, serviceName);
            rsSelect = psSelect.executeQuery();

            if(!rsSelect.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Dịch vụ không tồn tại!");
                alert.show();
            }else {
                psDel = conn.prepareStatement("DELETE FROM tbl_serviceType WHERE serviceName = ?");
                psDel.setString(1, serviceName);
                psDel.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Xóa dịch vụ thành công!");
                alert.show();
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelect != null) {
                try {
                    rsSelect.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(psDel != null) {
                try {
                    psDel.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(psSelect != null) {
                try {
                    psSelect.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
//hien thi danh sach dich vu len bang
    public static ObservableList<serviceData> getServiceList() {
        Connection conn = null;
        PreparedStatement psSelect = null;
        ResultSet rsSelect = null;
        ObservableList<serviceData> serviceList = FXCollections.observableArrayList();

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psSelect = conn.prepareStatement("SELECT serviceName, servicePrice FROM tbl_serviceType");
            rsSelect = psSelect.executeQuery();

            while(rsSelect.next()) {
                String rt_Name = rsSelect.getString("serviceName");
                BigDecimal rt_Price = rsSelect.getBigDecimal("servicePrice");
                serviceList.add(new serviceData(rt_Name, rt_Price));
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelect != null) {
                try {
                    rsSelect.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(psSelect != null) {
                try {
                    psSelect.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return serviceList;
    }

//nut tao hoa don (btn_bill_confirm)
    public static void createBill(ActionEvent event, String roomName, Double newElectric,
                                  Double newWater, Date dateCreate, String billBoss, Double otherBill,
                                  Double electricBill, Double waterBill, Double sumBill, String note, String paid, String fullName) throws SQLException {
        Connection conn = null;
        PreparedStatement psCheckRoom = null, psSelectIdElectric = null, psSelectIdWater = null, psSelectInitIndicator = null,
                psSelectOldElecIndi = null, psSelectOldWaterIndi = null, psSelectIdRender = null, psInsertInitWaterIndex = null,
                psInsertInitElectricIndex = null, psInsertBill = null, psCheck = null;
        ResultSet rsCheckRoom = null, rsSelectIdElectric = null, rsSelectIdWater = null, rsSelectInitIndicator = null,
                rsSelectIdRender = null, rsSelectOldElecIndi = null, rsSelectOldWaterIndi, rsCheck = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;

            //kiem tra phong da tao hoa don roi
            psCheck = conn.prepareStatement("SELECT roomName FROM tbl_bill JOIN tbl_infoRender ON tbl_bill.id_render = tbl_infoRender.id_render JOIN tbl_rendRoom ON tbl_infoRender.id_render = tbl_rendRoom.id_render JOIN tbl_room ON tbl_rendRoom.id_room = tbl_room.id_room JOIN tbl_roomType ON tbl_room.id_roomType = tbl_roomType.id_roomType WHERE roomName = ?");
            psCheck.setString(1, roomName);
            rsCheck = psCheck.executeQuery();

            //lay chi so dien, chi so nuoc ban dau cua phong tro
            psSelectInitIndicator = conn.prepareStatement("SELECT tbl_room.id_room FROM tbl_room JOIN tbl_rendRoom ON tbl_room.id_room = tbl_rendRoom.id_room JOIN tbl_infoRender ON tbl_rendRoom.id_render = tbl_infoRender.id_render WHERE roomName = ?");
            psSelectInitIndicator.setString(1, roomName);
            rsSelectInitIndicator = psSelectInitIndicator.executeQuery();

            //kiem tra phong nay co la phong moi thue khong
            psCheckRoom = conn.prepareStatement("SELECT tbl_room.id_room FROM tbl_room JOIN tbl_service ON tbl_room.id_room = tbl_service.id_room WHERE roomName = ?");
            psCheckRoom.setString(1, roomName);
            rsCheckRoom = psCheckRoom.executeQuery();

            //lay id loai dich vu dien, nuoc
            psSelectIdElectric = conn.prepareStatement("SELECT id_serviceType FROM tbl_serviceType WHERE serviceName LIKE '%điện%' OR serviceName LIKE '%dien%'");
            rsSelectIdElectric = psSelectIdElectric.executeQuery();
            psSelectIdWater = conn.prepareStatement("SELECT id_serviceType FROM tbl_serviceType WHERE serviceName LIKE '%nước%' OR serviceName LIKE '%nuoc%'");
            rsSelectIdWater = psSelectIdWater.executeQuery();

            if(rsCheck.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Phòng đã được tạo hóa đơn!");
                alert.show();
            }else {
                if(!rsSelectIdElectric.isBeforeFirst() || !rsSelectIdWater.isBeforeFirst()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Dịch vụ điện hoặc nước không tồn tại!");
                    alert.show();
                }
                else {
                    while(rsSelectIdElectric.next() && rsSelectIdWater.next()) {
                        int rt_idElectric = rsSelectIdElectric.getInt("id_serviceType");
                        int rt_idWater = rsSelectIdWater.getInt("id_serviceType");

                        if(rsCheckRoom.isBeforeFirst()) {
                            while(rsSelectInitIndicator.next()) {
                                int rt_id_room = rsSelectInitIndicator.getInt("id_room");
                                //lay chi so dien, nuoc cu tu chi so dien, nuoc moi cua bang service
                                psSelectOldElecIndi = conn.prepareStatement("SELECT new_indicator FROM tbl_service WHERE id_serviceType = ? AND id_room = ?");
                                psSelectOldElecIndi.setInt(1, rt_idElectric);
                                psSelectOldElecIndi.setInt(2, rt_id_room);
                                rsSelectOldElecIndi = psSelectOldElecIndi.executeQuery();
                                psSelectOldWaterIndi = conn.prepareStatement("SELECT new_indicator FROM tbl_service WHERE id_serviceType = ? AND id_room = ?");
                                psSelectOldWaterIndi.setInt(1, rt_idWater);
                                psSelectOldWaterIndi.setInt(2, rt_id_room);
                                rsSelectOldWaterIndi = psSelectOldWaterIndi.executeQuery();

                                if(rsSelectOldElecIndi.isBeforeFirst() && rsSelectOldWaterIndi.isBeforeFirst()) {
                                    while (rsSelectOldElecIndi.next() && rsSelectOldWaterIndi.next()) {
                                        int rt_new_elec_indi = rsSelectOldElecIndi.getInt("new_indicator");
                                        int rt_new_water_indi = rsSelectOldWaterIndi.getInt("new_indicator");

                                        psInsertInitElectricIndex = conn.prepareStatement("UPDATE tbl_service SET new_indicator = ?, old_indicator = ? WHERE id_room = ? AND id_serviceType = ?");
                                        psInsertInitElectricIndex.setDouble(1, newElectric);
                                        psInsertInitElectricIndex.setDouble(2, rt_new_elec_indi);
                                        psInsertInitElectricIndex.setInt(3, rt_id_room);
                                        psInsertInitElectricIndex.setInt(4, rt_idElectric);
                                        psInsertInitElectricIndex.executeUpdate();
                                        psInsertInitWaterIndex = conn.prepareStatement("UPDATE tbl_service SET new_indicator = ?, old_indicator = ? WHERE id_room = ? AND id_serviceType = ?");
                                        psInsertInitWaterIndex.setDouble(1, newWater);
                                        psInsertInitWaterIndex.setDouble(2, rt_new_water_indi);
                                        psInsertInitWaterIndex.setInt(3, rt_id_room);
                                        psInsertInitWaterIndex.setInt(4, rt_idWater);
                                        psInsertInitWaterIndex.executeUpdate();
                                    }
                                }
                            }
                        }
                    }

                    //lay id_render de luu vao csdl tbl_bill
                    psSelectIdRender = conn.prepareStatement("SELECT tbl_infoRender.id_render FROM tbl_infoRender JOIN tbl_rendRoom ON tbl_infoRender.id_render = tbl_rendRoom.id_render JOIN tbl_room ON tbl_rendRoom.id_room = tbl_room.id_room WHERE fullname = ?");
                    psSelectIdRender.setString(1, fullName);
                    rsSelectIdRender = psSelectIdRender.executeQuery();

                    while(rsSelectIdRender.next()) {
                        int rt_id_render = rsSelectIdRender.getInt("id_render");
                        psInsertBill = conn.prepareStatement("INSERT INTO tbl_bill (date_created, bill_boss, note, paid, electric_bill, water_bill, other_bill, sum_bill, id_render) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        psInsertBill.setDate(1, dateCreate);
                        psInsertBill.setString(2, billBoss);
                        psInsertBill.setString(3, note);
                        psInsertBill.setString(4, paid);
                        psInsertBill.setDouble(5, electricBill);
                        psInsertBill.setDouble(6, waterBill);
                        psInsertBill.setDouble(7, otherBill);
                        psInsertBill.setDouble(8, sumBill);
                        psInsertBill.setInt(9, rt_id_render);
                        psInsertBill.executeUpdate();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Tạo hóa đơn thành công!");
                        alert.show();
                    }
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelectOldElecIndi != null && rsSelectInitIndicator != null && rsCheckRoom != null && rsSelectIdWater != null &&
               rsSelectIdElectric != null && psInsertBill != null && psInsertInitWaterIndex != null &&
               psInsertInitElectricIndex != null && rsSelectIdRender != null) {
                rsSelectOldElecIndi.close();
                rsSelectInitIndicator.close();
                rsCheckRoom.close();
                rsSelectIdWater.close();
                rsSelectIdElectric.close();
                psInsertBill.close();
                psInsertInitWaterIndex.close();
                psInsertInitElectricIndex.close();
                rsSelectIdRender.close();
            }
            if(psSelectOldWaterIndi != null && psSelectIdRender != null && psSelectInitIndicator != null && psSelectIdWater != null &&
               psSelectIdElectric != null && psCheckRoom != null && psSelectOldElecIndi != null) {
                psSelectOldWaterIndi.close();
                psSelectIdRender.close();
                psSelectInitIndicator.close();
                psSelectIdWater.close();
                psSelectIdElectric.close();
                psCheckRoom.close();
                psSelectOldElecIndi.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
    }

//nut sua bill (btn_bill_edit)
    public static void editBill(ActionEvent event, String roomName, Double roomPrice,
                                Date dateCreate, String billBoss, Double otherBill, Double electricBill, Double waterBill,
                                Double sumBill, String note, String paid) throws SQLException {
        Connection conn = null;
        PreparedStatement psSelectIdBill = null, psSelectBill = null, psUpdate = null;
        ResultSet rsSelectIdBill = null, rsSelectBill = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            //lay tat ca noi dung cua cac o cua bill
            psSelectBill = conn.prepareStatement("SELECT roomName, roomPrice, date_created, note, paid, bill_boss, other_bill, electric_bill, water_bill, sum_bill FROM tbl_bill JOIN tbl_infoRender ON tbl_bill.id_render = tbl_infoRender.id_render JOIN tbl_rendRoom ON tbl_infoRender.id_render = tbl_rendRoom.id_render JOIN tbl_room ON tbl_rendRoom.id_room = tbl_room.id_room JOIN tbl_roomType ON tbl_room.id_roomType = tbl_roomType.id_roomType WHERE roomName = ?");
            psSelectBill.setString(1, roomName);
            rsSelectBill = psSelectBill.executeQuery();
            //lay id_bill
            psSelectIdBill = conn.prepareStatement("SELECT id_bill, tbl_bill.id_render FROM tbl_bill JOIN tbl_infoRender ON tbl_bill.id_render = tbl_infoRender.id_render JOIN tbl_rendRoom ON tbl_infoRender.id_render = tbl_rendRoom.id_render JOIN tbl_room ON tbl_rendRoom.id_room = tbl_room.id_room WHERE roomName = ?");
            psSelectIdBill.setString(1, roomName);
            rsSelectIdBill = psSelectIdBill.executeQuery();

            if(!rsSelectBill.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Không tìm thấy hóa đơn của phòng");
                alert.show();
            }else {
                while(rsSelectBill.next()) {
                    String rt_room_name = rsSelectBill.getString("roomName");
                    Double rt_room_price = rsSelectBill.getDouble("roomPrice");
                    Date rt_date_created = rsSelectBill.getDate("date_created");
                    String rt_bill_boss = rsSelectBill.getString("bill_boss");
                    Double rt_other_service = rsSelectBill.getDouble("other_bill");
                    Double rt_electric_bill = rsSelectBill.getDouble("electric_bill");
                    Double rt_water_bill = rsSelectBill.getDouble("water_bill");
                    Double rt_sum_bill = rsSelectBill.getDouble("sum_bill");
                    String rt_note = rsSelectBill.getString("note");
                    String rt_paid = rsSelectBill.getString("paid");

                    if(roomName.equals(rt_room_name) && roomPrice.equals(rt_room_price) && dateCreate.equals(rt_date_created)
                            && billBoss.equals(rt_bill_boss) && otherBill.equals(rt_other_service) &&
                            electricBill.equals(rt_electric_bill) && waterBill.equals(rt_water_bill) && sumBill.equals(rt_sum_bill)
                            && note.equals(rt_note) && paid.equals(rt_paid)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setContentText("Nội dung không thay đổi");
                        alert.show();
                    }else {
                        if(!rsSelectIdBill.isBeforeFirst()) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Không tìm thấy hóa đơn của phòng " + roomName);
                            alert.show();
                        }else {
                            while(rsSelectIdBill.next()) {
                                int rt_id_bill = rsSelectIdBill.getInt("id_bill");
                                int rt_id_render = rsSelectIdBill.getInt("id_render");

                                psUpdate = conn.prepareStatement("UPDATE tbl_bill SET date_created = ?, bill_boss = ?, note = ?, paid = ?, electric_bill = ?, water_bill = ?, other_bill = ?, sum_bill = ?, id_render = ? WHERE id_bill = ?");
                                psUpdate.setDate(1, dateCreate);
                                psUpdate.setString(2, billBoss);
                                psUpdate.setString(3, note);
                                psUpdate.setString(4, paid);
                                psUpdate.setDouble(5, electricBill);
                                psUpdate.setDouble(6, waterBill);
                                psUpdate.setDouble(7, otherBill);
                                psUpdate.setDouble(8, sumBill);
                                psUpdate.setInt(9, rt_id_render);
                                psUpdate.setInt(10, rt_id_bill);
                                psUpdate.executeUpdate();

                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Thông báo");
                                alert.setContentText("Cập nhật hóa đơn thành công!");
                                alert.show();
                            }
                        }
                    }
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelectIdBill != null && rsSelectBill != null) {
                rsSelectBill.close();
                rsSelectIdBill.close();
            }
            if(psSelectIdBill != null && psSelectBill != null && psUpdate != null) {
                psUpdate.close();
                psSelectBill.close();
                psSelectIdBill.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
    }

//xu ly hoa don
    //lay ten tu tbv mainController sang billController
    public static void getRoomName(ActionEvent event, String roomName, String renderName) throws SQLException {
        Connection conn = null;
        PreparedStatement psSelect = null, psSelectIdElectric = null, psSelectIdWater = null, psSelectWaterIndi = null,
                psCheck = null, psSelectElectricIndi = null;
        ResultSet rsSelect = null, rsSelectIdElectric = null, rsSelectIdWater = null, rsSelectWaterIndi = null,
                rsCheck = null, rsSelectElectricIndi = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;

            //kiem tra phong da tao hoa don roi
            psCheck = conn.prepareStatement("SELECT roomName FROM tbl_bill JOIN tbl_infoRender ON tbl_bill.id_render = tbl_infoRender.id_render JOIN tbl_rendRoom ON tbl_infoRender.id_render = tbl_rendRoom.id_render JOIN tbl_room ON tbl_rendRoom.id_room = tbl_room.id_room JOIN tbl_roomType ON tbl_room.id_roomType = tbl_roomType.id_roomType WHERE roomName = ?");
            psCheck.setString(1, roomName);
            rsCheck = psCheck.executeQuery();
            //lay id loai dich vu dien, nuoc
            psSelectIdElectric = conn.prepareStatement("SELECT id_serviceType FROM tbl_serviceType WHERE serviceName LIKE '%điện%' OR serviceName LIKE '%dien%'");
            rsSelectIdElectric = psSelectIdElectric.executeQuery();
            psSelectIdWater = conn.prepareStatement("SELECT id_serviceType FROM tbl_serviceType WHERE serviceName LIKE '%nước%' OR serviceName LIKE '%nuoc%'");
            rsSelectIdWater = psSelectIdWater.executeQuery();

            psSelect = conn.prepareStatement("SELECT id_room, roomName, roomPrice FROM tbl_roomType JOIN tbl_room ON tbl_roomType.id_roomType = tbl_room.id_roomType WHERE roomName = ?");
            psSelect.setString(1, roomName);
            rsSelect = psSelect.executeQuery();

            if(rsCheck.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Phòng đã được tạo hóa đơn!");
                alert.show();
            }else {
                while(rsSelect.next() && rsSelectIdElectric.next() && rsSelectIdWater.next()) {
                    String rt_roomName = rsSelect.getString("roomName");
                    BigDecimal rt_roomPrice = rsSelect.getBigDecimal("roomPrice");
                    int rt_id_room = rsSelect.getInt("id_room");
                    int rt_id_electric = rsSelectIdElectric.getInt("id_serviceType");
                    int rt_id_water = rsSelectIdWater.getInt("id_serviceType");

                    psSelectElectricIndi = conn.prepareStatement("SELECT old_indicator FROM tbl_serviceType JOIN tbl_service ON tbl_serviceType.id_serviceType = tbl_service.id_serviceType JOIN tbl_room ON tbl_service.id_room = tbl_room.id_room WHERE tbl_service.id_serviceType = ? AND tbl_room.id_room = ?");
                    psSelectElectricIndi.setInt(1, rt_id_electric);
                    psSelectElectricIndi.setInt(2, rt_id_room);
                    rsSelectElectricIndi = psSelectElectricIndi.executeQuery();
                    psSelectWaterIndi = conn.prepareStatement("SELECT old_indicator FROM tbl_serviceType JOIN tbl_service ON tbl_serviceType.id_serviceType = tbl_service.id_serviceType JOIN tbl_room ON tbl_service.id_room = tbl_room.id_room WHERE tbl_service.id_serviceType = ? AND tbl_room.id_room = ?");
                    psSelectWaterIndi.setInt(1, rt_id_water);
                    psSelectWaterIndi.setInt(2, rt_id_room);
                    rsSelectWaterIndi = psSelectWaterIndi.executeQuery();

                    while(rsSelectElectricIndi.next() && rsSelectWaterIndi.next()) {
                        int rt_old_electric = rsSelectElectricIndi.getInt("old_indicator");
                        int rt_old_water = rsSelectWaterIndi.getInt("old_indicator");

                        Stage stage = new Stage();
                        Parent viewBill = null;
                        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(DatabaseDriver.class.getResource("/FXMLs/Bill.fxml")));
                        try {
                            viewBill = loader.load();
                            BillController billController = loader.getController();
                            billController.setRoomNamePrice(rt_roomName, rt_roomPrice, rt_old_electric, rt_old_water, renderName);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        stage.setTitle("Hóa đơn");
                        stage.getIcons().add(new Image(String.valueOf(DatabaseDriver.class.getResource("/Images/icon.png"))));
                        stage.setScene(new Scene(viewBill));
                        stage.show();
                    }

                }
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelect != null && rsSelectIdElectric != null && rsSelectIdWater != null && rsSelectWaterIndi != null &&
                    rsSelectElectricIndi != null) {
                rsSelect.close();
                rsSelectIdElectric.close();
                rsSelectIdWater.close();
                rsSelectWaterIndi.close();
                rsSelectElectricIndi.close();
            }
            if(psSelect != null && psSelectIdElectric != null && psSelectIdWater != null && psSelectWaterIndi != null &&
                    psSelectElectricIndi != null) {
                psSelect.close();
                psSelectIdElectric.close();
                psSelectIdWater.close();
                psSelectWaterIndi.close();
                psSelectElectricIndi.close();

            }
            if(conn != null) {
                conn.close();
            }
        }
    }

    //tinh toan tien dich vu khac ngoai dien va nuoc
    public static int calculatorOtherBill() throws SQLException {
        Connection conn = null;
        PreparedStatement psSelectOtherPrice = null;
        ResultSet rsSelectOtherPrice = null;

        int otherBill = 0;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;

            psSelectOtherPrice = conn.prepareStatement("SELECT servicePrice FROM tbl_serviceType WHERE (serviceName NOT LIKE '%dien%' AND serviceName NOT LIKE '%điện%') AND (serviceName NOT LIKE '%nước%' AND serviceName NOT LIKE '%nuoc%')");
            rsSelectOtherPrice = psSelectOtherPrice.executeQuery();

            while(rsSelectOtherPrice.next()) {
                int rt_otherPriceItem = rsSelectOtherPrice.getInt("servicePrice");
                otherBill += rt_otherPriceItem;
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(rsSelectOtherPrice != null) {
                rsSelectOtherPrice.close();
            }
            if(psSelectOtherPrice != null) {
                psSelectOtherPrice.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
        return otherBill;
    }

//hien thi hoa don cua cac phong ra
    public static ObservableList<billData> getBillList() throws SQLException {
        Connection conn = null;
        PreparedStatement psSelect = null;
        ResultSet rsSelect = null;

        ObservableList<billData> billList = FXCollections.observableArrayList();

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psSelect = conn.prepareStatement("SELECT roomName, paid, date_created FROM tbl_bill JOIN tbl_infoRender ON tbl_bill.id_render = tbl_infoRender.id_render JOIN tbl_rendRoom ON tbl_infoRender.id_render = tbl_rendRoom.id_render JOIN tbl_room ON tbl_rendRoom.id_room = tbl_room.id_room JOIN tbl_roomType ON tbl_room.id_roomType = tbl_roomType.id_roomType");
            rsSelect = psSelect.executeQuery();
            while(rsSelect.next()) {
                String rt_room_name = rsSelect.getString("roomName");
                String rt_paid = rsSelect.getString("paid");
                Date rt_date_created = rsSelect.getDate("date_created");

                billList.add(new billData(rt_room_name, rt_paid, rt_date_created));
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(conn != null && psSelect != null && rsSelect != null) {
                conn.close();
                psSelect.close();
                rsSelect.close();
            }
        }
        return billList;
    }
//bam vao dong trong tbv_bill_list se hien thi hoa don tuong ung cua phong
    public static void getBillDetail(MouseEvent mouseEvent, String roomName) {
        Connection conn = null;
        PreparedStatement psSelectBill = null, psSelectService1 = null, psSelectService2 = null, psSelectIdElectric = null, psSelectIdWater = null;
        ResultSet rsSelectBill = null, rsSelectService1 = null, rsSelectService2 = null, rsSelectIdElectric = null, rsSelectIdWater = null;

        try {
            conn = ConnectDB.connectDB();
            assert conn != null;
            psSelectBill = conn.prepareStatement("SELECT roomName, roomPrice, date_created, bill_boss, other_bill, electric_bill, water_bill, sum_bill, note, paid, fullname FROM tbl_bill JOIN tbl_infoRender ON tbl_bill.id_render = tbl_infoRender.id_render JOIN tbl_rendRoom ON tbl_infoRender.id_render = tbl_rendRoom.id_render JOIN tbl_room ON tbl_rendRoom.id_room = tbl_room.id_room JOIN tbl_roomType ON tbl_room.id_roomType = tbl_roomType.id_roomType WHERE roomName = ?");
            psSelectBill.setString(1, roomName);
            rsSelectBill = psSelectBill.executeQuery();

            psSelectIdElectric = conn.prepareStatement("SELECT id_serviceType FROM tbl_serviceType WHERE serviceName LIKE '%điện%' OR serviceName LIKE '%dien%'");
            rsSelectIdElectric = psSelectIdElectric.executeQuery();
            psSelectIdWater = conn.prepareStatement("SELECT id_serviceType FROM tbl_serviceType WHERE serviceName LIKE '%nước%' OR serviceName LIKE '%nuoc%'");
            rsSelectIdWater = psSelectIdWater.executeQuery();

            while(rsSelectIdElectric.next() && rsSelectIdWater.next()) {
                int rt_id_electric = rsSelectIdElectric.getInt("id_serviceType");
                int rt_id_water = rsSelectIdWater.getInt("id_serviceType");

                psSelectService1 = conn.prepareStatement("SELECT new_indicator, old_indicator FROM tbl_room JOIN tbl_service ON tbl_room.id_room = tbl_service.id_room WHERE roomName = ? AND id_serviceType = ?");
                psSelectService1.setString(1, roomName);
                psSelectService1.setInt(2, rt_id_electric);
                rsSelectService1 = psSelectService1.executeQuery();
                psSelectService2 = conn.prepareStatement("SELECT new_indicator, old_indicator FROM tbl_room JOIN tbl_service ON tbl_room.id_room = tbl_service.id_room WHERE roomName = ? AND id_serviceType = ?");
                psSelectService2.setString(1, roomName);
                psSelectService2.setInt(2, rt_id_water);
                rsSelectService2 = psSelectService2.executeQuery();

                while(rsSelectBill.next() && rsSelectService1.next() && rsSelectService2.next()) {
                    String rt_roomName = rsSelectBill.getString("roomName");
                    BigDecimal rt_roomPrice = rsSelectBill.getBigDecimal("roomPrice");
                    Date rt_dateCreated = rsSelectBill.getDate("date_created");
                    String rt_billBoss = rsSelectBill.getString("bill_boss");
                    BigDecimal rt_otherBill = rsSelectBill.getBigDecimal("other_bill");
                    BigDecimal rt_electricBill = rsSelectBill.getBigDecimal("electric_bill");
                    BigDecimal rt_waterBill = rsSelectBill.getBigDecimal("water_bill");
                    BigDecimal rt_sumBill = rsSelectBill.getBigDecimal("sum_bill");
                    String rt_note = rsSelectBill.getString("note");
                    String rt_paid = rsSelectBill.getString("paid");
                    String rt_renderName = rsSelectBill.getString("fullname");
                    int rt_newElectric = rsSelectService1.getInt("new_indicator");
                    int rt_newWater = rsSelectService2.getInt("new_indicator");
                    int rt_oldElectric = rsSelectService1.getInt("old_indicator");
                    int rt_oldWater = rsSelectService2.getInt("old_indicator");

                    Stage stage = new Stage();
                    Parent viewBill = null;
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(DatabaseDriver.class.getResource("/FXMLs/Bill.fxml")));
                    try {
                        viewBill = loader.load();
                        BillController billController = loader.getController();
                        billController.setBill(rt_roomName, rt_roomPrice, rt_oldElectric, rt_newElectric, rt_oldWater, rt_newWater,
                                rt_dateCreated, rt_billBoss, rt_otherBill, rt_electricBill, rt_waterBill, rt_sumBill, rt_note,
                                rt_paid, rt_renderName);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    stage.setTitle("Hóa đơn");
                    stage.getIcons().add(new Image(String.valueOf(DatabaseDriver.class.getResource("/Images/icon.png"))));
                    stage.setScene(new Scene(viewBill));
                    stage.show();
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try{
                if(rsSelectBill != null && rsSelectService1 != null && rsSelectService2 != null && rsSelectIdElectric != null &&
                        rsSelectIdWater != null) {
                    rsSelectBill.close();
                    rsSelectService1.close();
                    rsSelectService2.close();
                    rsSelectIdElectric.close();
                    rsSelectIdWater.close();
                }
                if(psSelectBill != null && psSelectService1 != null && psSelectService2 != null && psSelectIdElectric != null &&
                        psSelectIdWater != null) {
                    psSelectBill.close();
                    psSelectService1.close();
                    psSelectService2.close();
                    psSelectIdElectric.close();
                    psSelectIdWater.close();
                }
                if(conn != null) {
                    conn.close();
                }
            }catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

