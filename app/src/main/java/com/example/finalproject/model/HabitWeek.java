package com.example.finalproject.model;

public class HabitWeek {
    private double mucTieu;
    private int soNgayThucHien;
    private int khoangThoiGian;
    private String trangThai;

    public HabitWeek() {
    }

    public HabitWeek(double mucTieu, int soNgayThucHien, int khoangThoiGian, String trangThai) {
        this.mucTieu = mucTieu;
        this.soNgayThucHien = soNgayThucHien;
        this.khoangThoiGian = khoangThoiGian;
        this.trangThai = trangThai;
    }

    public double getMucTieu() {
        return mucTieu;
    }

    public void setMucTieu(double mucTieu) {
        this.mucTieu = mucTieu;
    }

    public int getSoNgayThucHien() {
        return soNgayThucHien;
    }

    public void setSoNgayThucHien(int soNgayThucHien) {
        this.soNgayThucHien = soNgayThucHien;
    }

    public int getKhoangThoiGian() {
        return khoangThoiGian;
    }

    public void setKhoangThoiGian(int khoangThoiGian) {
        this.khoangThoiGian = khoangThoiGian;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
