package com.example.quanlyphongtro.Controllers;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import com.example.quanlyphongtro.Models.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    public Button btn_profile;

    @FXML
    public Button btn_rent;

    @FXML
    public Button btn_update_room;

    @FXML
    public AreaChart<?, ?> chart_income;

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
    public TableView<?> tbv_list_customer;

    @FXML
    public TableView<roomData> tbv_list_room;

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
    public TextField tf_search_render;

    ObservableList<roomTypeData> roomTypeList;
    ObservableList<roomData> roomDataLists;

    // Tạo đối tượng `DecimalFormat`
    DecimalFormat decimalFormat = new DecimalFormat();

    //ham gan thong tin ca nhan cua chu nha tro
    public void setUserLabel(String username) {
        lbl_username.setText(username);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        //nut checkIn phong
        btn_rent.setOnAction(e -> Model.getInstance().getViewFactory().showCheckInWindow());

        //hien thi du lieu phong len tableView
        try {
            showRoomList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //bam vao tung dong trong tableView, hien du lieu ra các o tai Quan ly phong
        tbv_list_room.setOnMouseClicked(e -> clickItem());

        //o tim kiem
        onSearch();

        //nut dang xuat
        btn_logout.setOnAction(e -> onMoveLogin());
    }


    //cac ham chuc nang
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
            clear();
            onSearch();
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
            clear();
            onSearch();
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
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            onSearch();
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
                clear();
                onSearch();
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
                clear();
                onSearch();
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
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            onSearch();
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

    private void clickItem() {
        roomData roomRow = tbv_list_room.getSelectionModel().getSelectedItem();
        int num = tbv_list_room.getSelectionModel().getSelectedIndex();

        //kiem tra nguoi dung co chon dong nao khong, neu khong chon thi khong thuc thi lenh ben duoi
        if((num -1) < -1){
            return;
        }

        tf_room_name.setText(roomRow.getRoomName());
    }

    public void showRoomList() throws SQLException {
        roomDataLists = DatabaseDriver.getRoomList();
        col_room_name.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        col_room_type.setCellValueFactory(new PropertyValueFactory<>("roomTypeName"));
        col_room_status.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
        col_room_price.setCellValueFactory(new PropertyValueFactory<>("roomPrice"));

        tbv_list_room.setItems(roomDataLists);
    }

    private void clear() {
        tf_room_name.setText("");
        combo_room_type.getSelectionModel().selectFirst();
        combo_room_status.getSelectionModel().selectFirst();
        tf_room_price.setText("");
        tf_room_type.setText("");
        tf_roomPrice.setText("");
    }

    private void onSearch() {
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

        if(option.get().equals(ButtonType.OK)){
            Stage stage = (Stage) btn_logout.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
            Model.getInstance().getViewFactory().showLoginWindow();
        }
        else return;
    }
}
