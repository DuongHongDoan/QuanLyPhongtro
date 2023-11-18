package com.example.quanlyphongtro.Controllers;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import com.example.quanlyphongtro.Models.*;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class MainViewController implements Initializable {
    @FXML
    public Button btn_add_room;

    @FXML
    public Button btn_add_room_type;

    @FXML
    public Button btn_del;

    @FXML
    public Button btn_del_info;

    @FXML
    public Button btn_delete_room;

    @FXML
    public Button btn_delete_room_type;

    @FXML
    public Button btn_edit;

    @FXML
    public Button btn_edit_info;

    @FXML
    public Button btn_edit_room_type;

    @FXML
    public Button btn_home;

    @FXML
    public Button btn_logout;

    @FXML
    public Button btn_manage_renter;

    @FXML
    public Button btn_manage_room;
    @FXML
    public Button btn_manage_service;
    @FXML
    public Button btn_service_add;

    @FXML
    public Button btn_service_del;

    @FXML
    public Button btn_service_edit;

    @FXML
    public Button btn_profile;

    @FXML
    public Button btn_rent;

    @FXML
    public Button btn_update_room;

    @FXML
    public Button btn_edit_save;

    @FXML
    public Button btn_pay;

    @FXML
    public AreaChart<String, Double> chart_income;

    @FXML
    public TableColumn<?, ?> col_addr;

    @FXML
    public TableColumn<?, ?> col_cccd;

    @FXML
    public TableColumn<?, ?> col_fullname;

    @FXML
    public TableColumn<?, ?> col_gender;

    @FXML
    public TableColumn<?, ?> col_phone;

    @FXML
    public TableColumn<?, ?> col_rent_date;

    @FXML
    public TableColumn<?, ?> col_room_name;

    @FXML
    public TableColumn<?, ?> col_room_name_render;

    @FXML
    public TableColumn<?, ?> col_room_price;

    @FXML
    public TableColumn<?, ?> col_room_status;

    @FXML
    public TableColumn<?, ?> col_room_type;

    @FXML
    public TableColumn<?, ?> col_service_name;

    @FXML
    public TableColumn<?, ?> col_service_price;

    @FXML
    public TableColumn<?, ?> col_billDetail_paidDate;

    @FXML
    public TableColumn<?, ?> col_billDetail_roomName;

    @FXML
    public TableColumn<?, ?> col_billDetail_status;

    @FXML
    public ComboBox<String> combo_room_status;

    @FXML
    public ComboBox<roomTypeData> combo_room_type;

    @FXML
    public Label lbl_email;

    @FXML
    public Label lbl_gender;

    @FXML
    public Label lbl_phone;

    @FXML
    public Label lbl_sum_income;

    @FXML
    public Label lbl_sum_renter;

    @FXML
    public Label lbl_sum_room;

    @FXML
    public Label lbl_username;

    @FXML
    public Label lbl_username_info;

    @FXML
    public Label lbl_totalIncome;

    @FXML
    public TableView<InfoRendRoomData> tbv_list_customer;

    @FXML
    public TableView<roomData> tbv_list_room;
    @FXML
    public TableView<serviceData> tbv_service_list;

    @FXML
    public TableView<billData> tbv_bill_list;

    @FXML
    public TextField tf_edit_cccd;

    @FXML
    public TextField tf_edit_homeTown;

    @FXML
    public TextField tf_edit_name;

    @FXML
    public TextField tf_edit_phone;

    @FXML
    public TextField tf_roomPrice;

    @FXML
    public TextField tf_room_name;

    @FXML
    public TextField tf_room_price;

    @FXML
    public TextField tf_room_type;

    @FXML
    public TextField tf_search;

    @FXML
    public TextField tf_search_bill;

    @FXML
    public TextField tf_search_render;
    @FXML
    public TextField tf_service_name;

    @FXML
    public TextField tf_service_price;
    @FXML
    public TextField tf_edit_username;
    @FXML
    public TextField tf_edit_gender;
    @FXML
    public TextField tf_edit_email;
    @FXML
    public TextField tf_edit_phone_user;

    @FXML
    public AnchorPane view_home;

    @FXML
    public AnchorPane view_manage_render;

    @FXML
    public AnchorPane view_manage_room;

    @FXML
    public AnchorPane view_manage_service;

    @FXML
    public AnchorPane view_profile;

    @FXML
    public AnchorPane view_edit_profile;

    ObservableList<roomTypeData> roomTypeList;
    ObservableList<roomData> roomDataLists;
    static ObservableList<InfoRendRoomData> renderList;
    ObservableList<serviceData> serviceDataList;
    ObservableList<billData> billDataList;

    public static void reloadRenderList() throws SQLException {
        renderList = DatabaseDriver.getRenderList();
    }

    // Tạo đối tượng `DecimalFormat`
    DecimalFormat decimalFormat = new DecimalFormat();

    //ham gan thong tin ca nhan cua chu nha tro
    public void setUserLabel(String username, String gender, String email, String phone) {
        lbl_username.setText(username);
        lbl_username_info.setText(username);
        lbl_gender.setText(gender);
        lbl_email.setText(email);
        lbl_phone.setText(phone);

        tf_edit_username.setText(username);
        tf_edit_gender.setText(gender);
        tf_edit_email.setText(email);
        tf_edit_phone_user.setText(phone);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl_totalIncome.setText("Tổng doanh thu tháng " + LocalDate.now().getMonthValue());
        //su kien chuyen view
        btn_home.setOnAction(this::switchView);
        btn_manage_room.setOnAction(this::switchView);
        btn_manage_renter.setOnAction(this::switchView);
        btn_manage_service.setOnAction(this::switchView);
        btn_profile.setOnAction(this::switchView);
        btn_edit_info.setOnAction(this::switchView);

//hien thi so luong thong ke
        //thong ke so phong da duoc thue
        showTotalRendRoom();
        //thong ke so nguoi thue
        showTotalRender();
        //thong ke doanh thu theo thang
        showTotalByMonth();
        //hien thi bang do doanh thu
        showIncomeChart();
//gan du lieu lay tu db ra comboBox de chon
        //comboBox loai phong
        try {
            comboBox_roomType();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //comboBox trang thai phong
        combo_room_status.getItems().addAll("Phòng trống", "Đã thuê", "Bảo trì");
        combo_room_status.getSelectionModel().selectFirst();

//cac nut them, sua, clear, xoa tai Quan ly phong
        //nut them
        btn_add_room.setOnAction(this::addRoom);
        //nut sua
        btn_update_room.setOnAction(this::updateRoom);
        //nut xoa
        btn_delete_room.setOnAction(this::delRoom);
//hai nut them, sua tai o Them Loai Phong
        //nut them loai phong
        btn_add_room_type.setOnAction(this::addRoomType);
        //nut sua gia loai phong
        btn_edit_room_type.setOnAction(this::editRoomType);
        //nut xoa loai phong
        btn_delete_room_type.setOnAction(this::delRoomType);

        //nut checkIn phong, nut thanh toan
        btn_rent.setOnAction(e -> Model.getInstance().getViewFactory().showCheckInWindow());
        btn_pay.setOnAction(this::clickPay);

        //nut sua, xoa khach thue
        btn_edit.setOnAction(this::editInfo);
        btn_del.setOnAction(this::delInfo);

//cac nut o view quan ly dich vu
        //nut them, sua xoa
        btn_service_add.setOnAction(this::addService);
        btn_service_edit.setOnAction(this::editService);
        btn_service_del.setOnAction(this::delService);

        //nut hoan tat khi sua thong tin nguoi dung xong va nut xoa tai khoan
        btn_edit_save.setOnAction(this::saveEdit);
        btn_del_info.setOnAction(this::delUser);

        //hien thi du lieu phong len tableView
        try {
            showRoomList();
            showRenderList();
            showServiceList();
            showBillList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //bam vao tung dong trong tableView, hien du lieu ra các o tai Quan ly phong
        tbv_list_room.setOnMouseClicked(e -> clickItemRoom());
        tbv_list_customer.setOnMouseClicked(e -> clickItemRender());
        tbv_service_list.setOnMouseClicked(e -> clickItemService());
        tbv_bill_list.setOnMouseClicked(this::clickItemBill);

        //o tim kiem
        onSearchRoom();
        onSearchRender();
        onSearchBill();

        //nut dang xuat
        btn_logout.setOnAction(e -> onMoveLogin());
    }

    private void clickPay(ActionEvent event) {
        InfoRendRoomData infoRow = tbv_list_customer.getSelectionModel().getSelectedItem();
        int n = tbv_list_customer.getSelectionModel().getSelectedIndex();

        if ((n - 1) < -1) {
            return;
        }

        String roomName = infoRow.getRoomName();
        String renderName = infoRow.getFullname();
//        Date date_created = infoRow.getRendDate();
//        if((date_created.getMonth()+1) <= LocalDate.now().getMonthValue()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Thông báo");
//            alert.setContentText("Hãy tạo hóa đơn vào tháng kế tiếp!");
//            alert.show();
//        }else{
        // Duyệt qua danh sách các dòng
            try {
                DatabaseDriver.getRoomName(event, roomName, renderName);
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
//        }
    }
    private void delService(ActionEvent event) {
        if(tf_service_name.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống tên dịch vụ!");
            alert.show();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Bạn có chắc muốn xóa dịch vụ?");
            Optional<ButtonType> optional = alert.showAndWait();

            if(optional.isPresent() && optional.get().equals(ButtonType.OK)) {
                DatabaseDriver.delService(event, tf_service_name.getText());
                showServiceList();
                clearService();
            }
        }
    }


    private void editService(ActionEvent event) {
        if(tf_service_name.getText().isEmpty() || tf_service_price.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống!");
            alert.show();
        }else {
            DatabaseDriver.editService(event, tf_service_name.getText(), Double.valueOf(tf_service_price.getText()));
            showServiceList();
            clearService();
        }
    }

    private void addService(ActionEvent event) {
        if(tf_service_name.getText().isEmpty() || tf_service_price.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống!");
            alert.show();
        }else {
            DatabaseDriver.addService(event, tf_service_name.getText(), Double.valueOf(tf_service_price.getText()));
            showServiceList();
            clearService();
        }
    }

    private void showIncomeChart() {
        chart_income.getData().clear();
        Connection conn = null;
        PreparedStatement psSelect = null;
        ResultSet rsSelect = null;
        double totalIncome1 = 0;
        double totalIncome2 = 0;
        double totalIncome3 = 0;
        double totalIncome4 = 0;
        double totalIncome5 = 0;
        double totalIncome6 = 0;
        double totalIncome7 = 0;
        double totalIncome8 = 0;
        double totalIncome9 = 0;
        double totalIncome10 = 0;
        double totalIncome11 = 0;
        double totalIncome12 = 0;

        try {
            conn = ConnectDB.connectDB();
            assert conn!= null;
            psSelect = conn.prepareStatement("SELECT date_created, sum_bill FROM tbl_bill");
            rsSelect = psSelect.executeQuery();

            int currentMonth = LocalDate.now().getMonthValue();

            LocalDate rt_date = null;
            double rt_sum_bill = 0;
            while(rsSelect.next()) {
                rt_date = rsSelect.getDate("date_created").toLocalDate();
                rt_sum_bill = rsSelect.getDouble("sum_bill");
//                if(rt_date.getMonthValue() == currentMonth) {
//                    totalIncome += rt_sum_bill;
//                }
                switch (rt_date.getMonthValue()) {
                    case 1 -> totalIncome1 += rt_sum_bill;
                    case 2 -> totalIncome2 += rt_sum_bill;
                    case 3 -> totalIncome3 += rt_sum_bill;
                    case 4 -> totalIncome4 += rt_sum_bill;
                    case 5 -> totalIncome5 += rt_sum_bill;
                    case 6 -> totalIncome6 += rt_sum_bill;
                    case 7 -> totalIncome7 += rt_sum_bill;
                    case 8 -> totalIncome8 += rt_sum_bill;
                    case 9 -> totalIncome9 += rt_sum_bill;
                    case 10 -> totalIncome10 += rt_sum_bill;
                    case 11 -> totalIncome11 += rt_sum_bill;
                    case 12 -> totalIncome12 += rt_sum_bill;
                }
            }

            XYChart.Series<String, Double> chart = new XYChart.Series<>();
            assert rt_date != null;
            chart.getData().add(new XYChart.Data<>(String.valueOf(1), totalIncome1));
            chart.getData().add(new XYChart.Data<>(String.valueOf(2), totalIncome2));
            chart.getData().add(new XYChart.Data<>(String.valueOf(3), totalIncome3));
            chart.getData().add(new XYChart.Data<>(String.valueOf(4), totalIncome4));
            chart.getData().add(new XYChart.Data<>(String.valueOf(5), totalIncome5));
            chart.getData().add(new XYChart.Data<>(String.valueOf(6), totalIncome6));
            chart.getData().add(new XYChart.Data<>(String.valueOf(7), totalIncome7));
            chart.getData().add(new XYChart.Data<>(String.valueOf(8), totalIncome8));
            chart.getData().add(new XYChart.Data<>(String.valueOf(9), totalIncome9));
            chart.getData().add(new XYChart.Data<>(String.valueOf(10), totalIncome10));
            chart.getData().add(new XYChart.Data<>(String.valueOf(11), totalIncome11));
            chart.getData().add(new XYChart.Data<>(String.valueOf(12), totalIncome12));
            chart.setName("Tổng thu nhập của tháng");

            chart_income.getData().add(chart);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                if(conn!=null && psSelect!=null && rsSelect!=null) {
                    conn.close();
                    psSelect.close();
                    rsSelect.close();
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void showTotalByMonth() {
        double tmp = 0;
        tmp = DatabaseDriver.getTotalByMonth();
        lbl_sum_income.setText(decimalFormat.format(tmp));
    }

    private void showTotalRender() {
        int cnt_render;
        try {
            cnt_render = DatabaseDriver.getTotalRender();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        lbl_sum_renter.setText(String.valueOf(cnt_render));
    }

    private void showTotalRendRoom() {
        String cnt_rend_room;
        try {
            cnt_rend_room = DatabaseDriver.getTotalRendRoom();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        lbl_sum_room.setText(String.valueOf(cnt_rend_room));
    }

    private void delUser(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setContentText("Bạn có chắc muốn xóa tài khoản?");
        Optional<ButtonType> optional = alert.showAndWait();

        if(optional.isPresent() && optional.get().equals(ButtonType.OK)) {
            DatabaseDriver.delUser(event, lbl_username_info.getText());
            Stage stage = (Stage) btn_profile.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
            Model.getInstance().getViewFactory().showLoginWindow();
        }
    }

    private void saveEdit(ActionEvent event) {
        if (tf_edit_username.getText().isEmpty() || tf_edit_gender.getText().isEmpty() || tf_edit_email.getText().isEmpty() || tf_edit_phone_user.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống!");
            alert.show();
        }
        else {
            try {
                if(!checkPhone(tf_edit_phone_user.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Số điện thoại không đúng định dạng!");
                    alert.show();
                }
                else if (!checkEmail(tf_edit_email.getText())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setContentText("Email không đúng định dạng!");
                    alert.show();
                }else {
                    String oldName = lbl_username.getText();
                    DatabaseDriver.updateUserInfo(event, oldName, tf_edit_username.getText(), tf_edit_gender.getText(), tf_edit_email.getText(), tf_edit_phone_user.getText());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    //cac ham chuc nang
    //chuyen doi cac view qua lai
    public void switchView(ActionEvent event) {
        if(event.getSource() == btn_home) {
            view_home.setVisible(true);
            view_manage_room.setVisible(false);
            view_manage_render.setVisible(false);
            view_manage_service.setVisible(false);
            view_profile.setVisible(false);
            view_edit_profile.setVisible(false);

            //thong ke so phong da duoc thue
            showTotalRendRoom();

            //thong ke so nguoi thue
            showTotalRender();

            showTotalByMonth();
            showIncomeChart();

            btn_home.setStyle("-fx-background-color: linear-gradient(to bottom right, #17EA9D, #6078EA);" +
                    "-fx-effect: dropshadow(three-pass-box, #D3D3D3, 0, 0, 0, 0);");
            btn_manage_room.setStyle("-fx-background-color: transparent;");
            btn_manage_renter.setStyle("-fx-background-color: transparent;");
            btn_manage_service.setStyle("-fx-background-color: transparent;");
            btn_profile.setStyle("-fx-background-color: transparent;");
            btn_logout.setStyle("-fx-background-color: transparent;");
        }
        else if(event.getSource() == btn_manage_room) {
            view_home.setVisible(false);
            view_manage_room.setVisible(true);
            view_manage_render.setVisible(false);
            view_manage_service.setVisible(false);
            view_profile.setVisible(false);
            view_edit_profile.setVisible(false);

            try {
                showRoomList();
                onSearchRoom();
            }catch(SQLException e) {
                System.out.println(e.getMessage());
            }

            btn_home.setStyle("-fx-background-color: transparent;");
            btn_manage_room.setStyle("-fx-background-color: linear-gradient(to bottom right, #17EA9D, #6078EA);" +
                    "-fx-effect: dropshadow(three-pass-box, #D3D3D3, 0, 0, 0, 0);");
            btn_manage_renter.setStyle("-fx-background-color: transparent;");
            btn_manage_service.setStyle("-fx-background-color: transparent;");
            btn_profile.setStyle("-fx-background-color: transparent;");
            btn_logout.setStyle("-fx-background-color: transparent;");
        }
        else if(event.getSource() == btn_manage_renter) {
            view_home.setVisible(false);
            view_manage_room.setVisible(false);
            view_manage_render.setVisible(true);
            view_manage_service.setVisible(false);
            view_profile.setVisible(false);
            view_edit_profile.setVisible(false);

            try {
                showRenderList();
                onSearchRender();
            }catch(SQLException e) {
                System.out.println(e.getMessage());
            }

            btn_home.setStyle("-fx-background-color: transparent;");
            btn_manage_room.setStyle("-fx-background-color: transparent;");
            btn_manage_renter.setStyle("-fx-background-color: linear-gradient(to bottom right, #17EA9D, #6078EA);" +
                    "-fx-effect: dropshadow(three-pass-box, #D3D3D3, 0, 0, 0, 0);");
            btn_manage_service.setStyle("-fx-background-color: transparent;");
            btn_profile.setStyle("-fx-background-color: transparent;");
            btn_logout.setStyle("-fx-background-color: transparent;");
        }
        else if(event.getSource() == btn_manage_service) {
            view_home.setVisible(false);
            view_manage_room.setVisible(false);
            view_manage_render.setVisible(false);
            view_profile.setVisible(false);
            view_edit_profile.setVisible(false);
            view_manage_service.setVisible(true);

            showServiceList();
            try {
                showBillList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            onSearchBill();
            clearService();

            btn_home.setStyle("-fx-background-color: transparent;");
            btn_manage_room.setStyle("-fx-background-color: transparent;");
            btn_manage_renter.setStyle("-fx-background-color: transparent;");
            btn_manage_service.setStyle("-fx-background-color: linear-gradient(to bottom right, #17EA9D, #6078EA);" +
                    "-fx-effect: dropshadow(three-pass-box, #D3D3D3, 0, 0, 0, 0);");
            btn_profile.setStyle("-fx-background-color: transparent;");
            btn_logout.setStyle("-fx-background-color: transparent;");
        }
        else if(event.getSource() == btn_profile) {
            view_home.setVisible(false);
            view_manage_room.setVisible(false);
            view_manage_render.setVisible(false);
            view_profile.setVisible(true);
            view_edit_profile.setVisible(false);
            view_manage_service.setVisible(false);

            btn_home.setStyle("-fx-background-color: transparent;");
            btn_manage_room.setStyle("-fx-background-color: transparent;");
            btn_manage_renter.setStyle("-fx-background-color: transparent;");
            btn_manage_service.setStyle("-fx-background-color: transparent;");
            btn_profile.setStyle("-fx-background-color: linear-gradient(to bottom right, #17EA9D, #6078EA);" +
                    "-fx-effect: dropshadow(three-pass-box, #D3D3D3, 0, 0, 0, 0);");
            btn_logout.setStyle("-fx-background-color: transparent;");
        }
        else if(event.getSource() == btn_edit_info) {
            view_home.setVisible(false);
            view_manage_room.setVisible(false);
            view_manage_render.setVisible(false);
            view_manage_service.setVisible(false);
            view_profile.setVisible(false);
            view_edit_profile.setVisible(true);

            btn_home.setStyle("-fx-background-color: transparent;");
            btn_manage_room.setStyle("-fx-background-color: transparent;");
            btn_manage_renter.setStyle("-fx-background-color: transparent;");
            btn_manage_service.setStyle("-fx-background-color: transparent;");
            btn_profile.setStyle("-fx-background-color: linear-gradient(to bottom right, #17EA9D, #6078EA);" +
                    "-fx-effect: dropshadow(three-pass-box, #D3D3D3, 0, 0, 0, 0);");
            btn_logout.setStyle("-fx-background-color: transparent;");
        }
        else if(event.getSource() == btn_logout) {
            btn_home.setStyle("-fx-background-color: transparent;");
            btn_manage_room.setStyle("-fx-background-color: transparent;");
            btn_manage_renter.setStyle("-fx-background-color: transparent;");
            btn_manage_service.setStyle("-fx-background-color: transparent;");
            btn_profile.setStyle("-fx-background-color: transparent;");
            btn_logout.setStyle("-fx-background-color: linear-gradient(to bottom right, #17EA9D, #6078EA);" +
                    "-fx-effect: dropshadow(three-pass-box, #D3D3D3, 0, 0, 0, 0);");
        }
    }

    private void addRoom(ActionEvent event) {
        if(tf_room_name.getText().isEmpty() || tf_room_price.getText().isEmpty() || combo_room_type.getSelectionModel().isEmpty()
                || combo_room_status.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống!");
            alert.show();
        }
        else {
            String roomTypeName = combo_room_type.getSelectionModel().getSelectedItem().getRoomTypeName();
            DatabaseDriver.addRoom(event, tf_room_name.getText(), combo_room_status.getValue(),roomTypeName);
            try {
                showRoomList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            clearRoom();
            onSearchRoom();
        }
    }

    private void updateRoom(ActionEvent event) {
        if(tf_room_name.getText().isEmpty() || tf_room_price.getText().isEmpty() || combo_room_type.getSelectionModel().isEmpty()
                || combo_room_status.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống!");
            alert.show();
        }
        else {
            String roomTypeName = combo_room_type.getSelectionModel().getSelectedItem().getRoomTypeName();
            DatabaseDriver.editRoom(event, tf_room_name.getText(), roomTypeName, combo_room_status.getValue());
            try {
                showRoomList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            clearRoom();
            onSearchRoom();
        }
    }

    private void delRoom(ActionEvent event) {
        if(tf_room_name.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng chọn phòng cần xóa!");
            alert.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Bạn có chắc muốn xóa phòng " + tf_room_name.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.isPresent() && option.get().equals(ButtonType.OK)) {
                DatabaseDriver.delRoom(event, tf_room_name.getText());
            }
            try {
                showRoomList();
                clearRoom();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            onSearchRoom();
        }
    }

    private void addRoomType(ActionEvent event){
        if(tf_room_type.getText().isEmpty() || tf_roomPrice.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống!");
            alert.show();
        }else {
            if(!Pattern.matches("[0-9]+", tf_roomPrice.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Vui lòng nhập số!");
                alert.show();
            }
            else {
                DatabaseDriver.addRoomType(event, tf_room_type.getText(), Double.valueOf(tf_roomPrice.getText()));
                try {
                    comboBox_roomType();
                    showRoomList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                clearRoom();
                onSearchRoom();
            }
        }
    }

    private void editRoomType(ActionEvent event) {
        if(tf_room_type.getText().isEmpty() || tf_roomPrice.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống!");
            alert.show();
        }else {
            if(!Pattern.matches("[0-9]+", tf_roomPrice.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setContentText("Vui lòng nhập số!");
                alert.show();
            }
            else {
                DatabaseDriver.editRoomType(event, tf_room_type.getText(), tf_roomPrice.getText());
                try {
                    comboBox_roomType();
                    showRoomList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                clearRoom();
                onSearchRoom();
            }
        }
    }

    private void delRoomType(ActionEvent event) {
        if(tf_room_type.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng điền thông tin loại phòng cần xóa!");
            alert.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Bạn có chắc muốn xóa loại phòng " + tf_room_type.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.isPresent() && option.get().equals(ButtonType.OK)) {
                DatabaseDriver.delRoomType(event, tf_room_type.getText());
            }
            try {
                comboBox_roomType();
                showRoomList();
                clearRoom();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            onSearchRoom();
        }
    }

    private void delInfo(ActionEvent event) {
        if(tf_edit_name.getText().isEmpty() || tf_edit_cccd.getText().isEmpty() || tf_edit_homeTown.getText().isEmpty() ||
                tf_edit_phone.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống!");
            alert.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Bạn có chắc muốn xóa khách " + tf_edit_name.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.isPresent() && option.get().equals(ButtonType.OK)) {
                DatabaseDriver.delInfo(event, tf_edit_name.getText(), tf_edit_cccd.getText(), tf_edit_homeTown.getText(), tf_edit_phone.getText());
            }
            try {
                showRenderList();
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            clearRender();
            onSearchRender();
        }
    }

    private void editInfo(ActionEvent event) {
        if(tf_edit_name.getText().isEmpty() || tf_edit_cccd.getText().isEmpty() || tf_edit_homeTown.getText().isEmpty() ||
                tf_edit_phone.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setContentText("Vui lòng không để trống!");
            alert.show();
        }
        else {
            int i = tbv_list_customer.getSelectionModel().getSelectedIndex();
            String oldName = tbv_list_customer.getItems().get(i).getFullname();
            DatabaseDriver.editInfo(event, oldName, tf_edit_name.getText(), tf_edit_cccd.getText(), tf_edit_homeTown.getText(), tf_edit_phone.getText());
            try {
                showRenderList();
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            clearRender();
            onSearchRender();
        }
    }

    private void comboBox_roomType() throws SQLException {
        roomTypeList = DatabaseDriver.getListLoaiPhong();
        combo_room_type.setItems(roomTypeList);

        //tuong ung gia tri comboBox se hien don gia tuong ung
        combo_room_type.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if(newVal != null){
                combo_room_type.setPromptText(newVal.toString());
                tf_room_price.setText(decimalFormat.format(newVal.getRoomPrice()));
            }
        });
    }

    private void clickItemRoom() {
        roomData roomRow = tbv_list_room.getSelectionModel().getSelectedItem();
        int num = tbv_list_room.getSelectionModel().getSelectedIndex();

        //kiem tra nguoi dung co chon dong nao khong, neu khong chon thi khong thuc thi lenh ben duoi
        if((num -1) < -1){
            return;
        }

        tf_room_name.setText(roomRow.getRoomName());
    }

    private void clickItemRender() {
        InfoRendRoomData infoRow = tbv_list_customer.getSelectionModel().getSelectedItem();
        int n = tbv_list_customer.getSelectionModel().getSelectedIndex();

        if((n-1) < -1) {
            return;
        }

        tf_edit_name.setText(infoRow.getFullname());
        tf_edit_cccd.setText(infoRow.getCCCD());
        tf_edit_homeTown.setText(infoRow.getHomeTown());
        tf_edit_phone.setText(infoRow.getPhone());
    }

    private void clickItemService() {
        serviceData serRow = tbv_service_list.getSelectionModel().getSelectedItem();
        int n = tbv_service_list.getSelectionModel().getSelectedIndex();

        if((n-1) < -1) {
            return;
        }

        tf_service_name.setText(serRow.getServiceName());
        tf_service_price.setText(String.valueOf(serRow.getServicePrice()));
    }

    private void clickItemBill(MouseEvent mouseEvent) {
        billData billRow = tbv_bill_list.getSelectionModel().getSelectedItem();
        int n = tbv_bill_list.getSelectionModel().getSelectedIndex();

        if((n-1) < -1) {
            return;
        }

        String roomName = billRow.getRoomName();
        DatabaseDriver.getBillDetail(mouseEvent, roomName);
        //phai viet dbdriver de hien thi bill khi nhan vao dong
    }

    public void showRoomList() throws SQLException {
        roomDataLists = DatabaseDriver.getRoomList();
        col_room_name.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        col_room_type.setCellValueFactory(new PropertyValueFactory<>("roomTypeName"));
        col_room_status.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
        col_room_price.setCellValueFactory(new PropertyValueFactory<>("roomPrice"));

        tbv_list_room.setItems(roomDataLists);
    }

    public void showRenderList() throws SQLException {
        renderList = DatabaseDriver.getRenderList();

        col_fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        col_cccd.setCellValueFactory(new PropertyValueFactory<>("CCCD"));
        col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        col_addr.setCellValueFactory(new PropertyValueFactory<>("homeTown"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_rent_date.setCellValueFactory(new PropertyValueFactory<>("rendDate"));
        col_room_name_render.setCellValueFactory(new PropertyValueFactory<>("roomName"));

        tbv_list_customer.setItems(renderList);
    }

    private void showServiceList() {
        serviceDataList = DatabaseDriver.getServiceList();

        col_service_name.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        col_service_price.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));

        tbv_service_list.setItems(serviceDataList);
    }

    private void showBillList() throws SQLException {
        billDataList = DatabaseDriver.getBillList();

        col_billDetail_roomName.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        col_billDetail_status.setCellValueFactory(new PropertyValueFactory<>("paid"));
        col_billDetail_paidDate.setCellValueFactory(new PropertyValueFactory<>("date_created"));

        tbv_bill_list.setItems(billDataList);
    }

    private void clearRoom() {
        tf_room_name.setText("");
        combo_room_type.getSelectionModel().selectFirst();
        combo_room_status.getSelectionModel().selectFirst();
        tf_room_price.setText("");
        tf_room_type.setText("");
        tf_roomPrice.setText("");
    }


    private void clearRender() {
        tf_edit_name.setText("");
        tf_edit_cccd.setText("");
        tf_edit_homeTown.setText("");
        tf_edit_phone.setText("");
    }

    private void clearService() {
        tf_service_name.setText("");
        tf_service_price.setText("");
    }

    private void onSearchRoom() {
        FilteredList<roomData> filter = new FilteredList<>(roomDataLists, e-> true);

        tf_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateMedicineData ->{

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if(predicateMedicineData.getRoomName().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getRoomTypeName().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getRoomStatus().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getRoomPrice().toString().contains(searchKey)){
                    return true;
                }else return false;
            });
        });

        SortedList<roomData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(tbv_list_room.comparatorProperty());
        tbv_list_room.setItems(sortList);
    }

    private void onSearchRender() {
        FilteredList<InfoRendRoomData> filter = new FilteredList<>(renderList, e-> true);

        tf_search_render.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateMedicineData ->{

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if(predicateMedicineData.getFullname().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getCCCD().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getGender().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getHomeTown().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getPhone().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getRendDate().toString().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getRoomName().toLowerCase().contains(searchKey)){
                    return true;
                }else return false;
            });
        });

        SortedList<InfoRendRoomData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(tbv_list_customer.comparatorProperty());
        tbv_list_customer.setItems(sortList);
    }

    private void onSearchBill() {
        FilteredList<billData> filter = new FilteredList<>(billDataList, e-> true);

        tf_search_bill.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateMedicineData ->{

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if(predicateMedicineData.getRoomName().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getPaid().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getDate_created().toString().contains(searchKey)){
                    return true;
                }else return false;
            });
        });

        SortedList<billData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(tbv_bill_list.comparatorProperty());
        tbv_bill_list.setItems(sortList);
    }

    //click dang xuat
    private void onMoveLogin() {
        btn_logout.setStyle("-fx-background-color: linear-gradient(to bottom right, #17EA9D, #6078EA);" +
                "-fx-effect: dropshadow(three-pass-box, #D3D3D3, 0, 0, 0, 0);");
        btn_home.setStyle("-fx-background-color: transparent;");
        btn_profile.setStyle("-fx-background-color: transparent;");
        btn_manage_renter.setStyle("-fx-background-color: transparent;");
        btn_manage_room.setStyle("-fx-background-color: transparent;");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setContentText("Bạn có chắc muốn thoát?");
        Optional<ButtonType> option = alert.showAndWait();

        if(option.isPresent() && option.get().equals(ButtonType.OK)){
            Stage stage = (Stage) btn_logout.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
            Model.getInstance().getViewFactory().showLoginWindow();
        }
        else return;
    }

    private boolean checkPhone(String str) throws Exception {
        // Bieu thuc chinh quy mo ta dinh dang so dien thoai
        String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";

        // Kiem tra dinh dang

        return str.matches(reg);
    }

    //kiem tra email
    private boolean checkEmail (String str) throws Exception {
        //bieu thuc chinh quy dinh dang 1 email
        String reg = "^[A-Za-z0-9]+[A-Za-z0-9]*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)$";

        return str.matches(reg);
    }
}
