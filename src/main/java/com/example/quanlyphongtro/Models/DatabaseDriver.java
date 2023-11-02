package com.example.quanlyphongtro.Models;

import com.example.quanlyphongtro.Controllers.MainViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

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

                        if(!username.equals(rt_username) || !security_question.equals(rt_security_question) || !security_answer.equals(rt_security_answer)){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Thông báo");
                            alert.setContentText("Thông tin không đúng!");
                            alert.show();
                        }
                        else {
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
                               String homeTown, String roomName, Double deposit, Date rendDate) throws SQLException {
        Connection conn = null;
        PreparedStatement psInsertRender = null;
        PreparedStatement psSelectIDRoom = null;
        PreparedStatement psSelectIDRender = null;
        PreparedStatement psEditStatus = null;
        PreparedStatement psInsertRendRoom = null;
        ResultSet rsIDRoom = null;
        ResultSet rsIDRender = null;
//        ResultSet rsNumberPeople = null;

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

//            while(rsIDRoom.next()){
//                int rt_id_room = rsIDRoom.getInt("id_room");
//                psSelectNumberPeople = conn.prepareStatement("SELECT COUNT(*) AS number_people FROM tbl_render JOIN tbl_renRoom ON tbl_render.id_render = tbl_rendRoom.id_render WHERE tbl_rendRoom.id_room = ?");
//                psSelectNumberPeople.setInt(1, rt_id_room);
//                rsNumberPeople = psSelectNumberPeople.executeQuery();
//            }

            while(rsIDRoom.next() && rsIDRender.next()) {
                int rt_id_room = rsIDRoom.getInt("id_room");
                int rt_id_render = rsIDRender.getInt("id_render");

                psInsertRendRoom = conn.prepareStatement("INSERT INTO tbl_rendRoom (check_in_date, deposit, id_room, id_render) VALUES (?, ?, ?, ?)");
                psInsertRendRoom.setDate(1, rendDate);
                psInsertRendRoom.setDouble(2, deposit);
                psInsertRendRoom.setInt(3, rt_id_room);
                psInsertRendRoom.setInt(4, rt_id_render);
                psInsertRendRoom.executeUpdate();

                psEditStatus = conn.prepareStatement("UPDATE tbl_room SET roomStatus = 'Đã thuê' WHERE roomName = ?");
                psEditStatus.setString(1, roomName);
                psEditStatus.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setContentText("Thêm người thuê thành công!");
                alert.show();
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if (rsIDRender != null) {
                rsIDRender.close();
            }
            if (rsIDRoom != null) {
                rsIDRoom.close();
            }
            if (psInsertRendRoom != null) {
                psInsertRendRoom.close();
            }
            if (psEditStatus != null) {
                psEditStatus.close();
            }
            if (psSelectIDRender != null) {
                psSelectIDRender.close();
            }
            if (psSelectIDRoom != null) {
                psSelectIDRoom.close();
            }
            if (psInsertRender != null) {
                psInsertRender.close();
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

//them, sua, xoa dich vu
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
}

