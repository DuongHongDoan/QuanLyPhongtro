package com.example.quanlyphongtro.Models;

import java.util.Date;

public class billData {
    private String roomName;
    private String paid;
    private Date date_created;

    public billData(String roomName, String paid, Date date_created) {
        this.roomName = roomName;
        this.paid = paid;
        this.date_created = date_created;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPaid() {
        return paid;
    }

    public Date getDate_created() {
        return date_created;
    }
}
