package com.example.quanlyphongtro.Models;

public class roomTypeData {
    private String roomTypeName;
    private Double roomPrice;

    public roomTypeData(String roomTypeName, Double roomPrice) {
        this.roomTypeName = roomTypeName;
        this.roomPrice = roomPrice;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    @Override
    public String toString() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public Double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(Double roomPrice) {
        this.roomPrice = roomPrice;
    }
}
