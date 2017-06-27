package com.store.kiwi.kiwistore.model;

/**
 * Created by admin on 5/25/2017.
 */

public class TheLoai {
    private String id;
    private String ten;
    private String soLuong;
    private String icon;
    private boolean checked;

    public TheLoai() {
    }

    public TheLoai(String id, String ten, String soLuong, String icon, boolean checked) {
        this.id = id;
        this.ten = ten;
        this.soLuong = soLuong;
        this.icon = icon;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
