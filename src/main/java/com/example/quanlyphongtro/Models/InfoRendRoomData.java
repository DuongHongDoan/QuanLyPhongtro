package com.example.quanlyphongtro.Models;

import java.util.Date;

public class InfoRendRoomData {
    private String fullname;
    private String gender;
    private String CCCD;
    private String phone;
    private String homeTown;

    private String roomName;
    private Date rendDate;

    public InfoRendRoomData(String fullname, String CCCD, String gender, String homeTown, String phone, Date rendDate, String roomName) {
        this.fullname = fullname;
        this.gender = gender;
        this.CCCD = CCCD;
        this.phone = phone;
        this.homeTown = homeTown;
        this.rendDate = rendDate;
        this.roomName = roomName;
    }

    public String getFullname() {
        return fullname;
    }

    public String getGender() {
        return gender;
    }

    public String getCCCD() {
        return CCCD;
    }

    public String getPhone() {
        return phone;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public String getRoomName() {
        return roomName;
    }

    public Date getRendDate() {
        return rendDate;
    }
}
