package com.example.quanlyphongtro.Models;

import java.math.BigDecimal;

public class roomData {
    private String roomName;
    private String roomTypeName;
    private String roomStatus;
    private BigDecimal roomPrice;


    public roomData(String roomName, String roomTypeName, String roomStatus, BigDecimal roomPrice) {
        this.roomName = roomName;
        this.roomTypeName = roomTypeName;
        this.roomStatus = roomStatus;
        this.roomPrice = roomPrice;
    }

    public roomData(String roomName, String roomTypeName) {
        this.roomName = roomName;
        this.roomTypeName = roomTypeName;
    }

    public String getRoomName() {
        return roomName;
    }

    //dung de hien thi ten phong, chứ khong phai la doi tuong roomDataList "com.nienluan.boardinghouse.Models.roomDataList@219da2ee"
    @Override
    public String toString() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public BigDecimal getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(BigDecimal roomPrice) {
        this.roomPrice = roomPrice;
    }
}
