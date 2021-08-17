package com.example.covidcontacttracing.Models;

public class History {

    private String location, date, time, risk, address, tel, img;
    private Boolean expandable;

    public History(String location, String date, String time, String risk, String address, String tel, String img) {
        this.location = location;
        this.date = date;
        this.time = time;
        this.risk = risk;
        this.address = address;
        this.tel = tel;
        this.img = img;
        this.expandable = false;
    }

    public Boolean getExpandable() {
        return expandable;
    }

    public void setExpandable(Boolean expandable) {
        this.expandable = expandable;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
