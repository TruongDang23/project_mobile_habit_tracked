package com.example.finalproject.model;

import com.google.firebase.database.PropertyName;

public class Habit  {
    @PropertyName("DonVi")
    private String donVi;

    @PropertyName("DonViTang")
    private double donViTang;

    @PropertyName("KhoangThoiGian")
    private String khoangThoiGian;

    @PropertyName("LoiNhacNho")
    private String loiNhacNho;

    @PropertyName("MauSac")
    private String mauSac;

    @PropertyName("MoTa")
    private String moTa;

    @PropertyName("MucTieu")
    private int mucTieu;

    @PropertyName("Ten")
    private String ten;

    @PropertyName("ThoiDiem")
    private String thoiDiem;

    @PropertyName("ThoiGianBatDau")
    private String thoiGianBatDau;

    @PropertyName("ThoiGianKetThuc")
    private String thoiGianKetThuc;

    @PropertyName("ThoiGianNhacNho")
    private String thoiGianNhacNho;

    @PropertyName("TrangThai")
    private String trangThai;

    public Habit() {
    }

    public Habit(String donVi, double donViTang, String khoangThoiGian, String loiNhacNho, String mauSac, String moTa, int mucTieu, String ten, String thoiDiem, String thoiGianBatDau, String thoiGianKetThuc, String thoiGianNhacNho, String trangThai) {
        this.donVi = donVi;
        this.donViTang = donViTang;
        this.khoangThoiGian = khoangThoiGian;
        this.loiNhacNho = loiNhacNho;
        this.mauSac = mauSac;
        this.moTa = moTa;
        this.mucTieu = mucTieu;
        this.ten = ten;
        this.thoiDiem = thoiDiem;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.thoiGianNhacNho = thoiGianNhacNho;
        this.trangThai = trangThai;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public double getDonViTang() {
        return donViTang;
    }

    public void setDonViTang(double donViTang) {
        this.donViTang = donViTang;
    }

    public String getKhoangThoiGian() {
        return khoangThoiGian;
    }

    public void setKhoangThoiGian(String khoangThoiGian) {
        this.khoangThoiGian = khoangThoiGian;
    }

    public String getLoiNhacNho() {
        return loiNhacNho;
    }

    public void setLoiNhacNho(String loiNhacNho) {
        this.loiNhacNho = loiNhacNho;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getMucTieu() {
        return mucTieu;
    }

    public void setMucTieu(int mucTieu) {
        this.mucTieu = mucTieu;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getThoiDiem() {
        return thoiDiem;
    }

    public void setThoiDiem(String thoiDiem) {
        this.thoiDiem = thoiDiem;
    }

    public String getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(String thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public String getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(String thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public String getThoiGianNhacNho() {
        return thoiGianNhacNho;
    }

    public void setThoiGianNhacNho(String thoiGianNhacNho) {
        this.thoiGianNhacNho = thoiGianNhacNho;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    // Constructor, Getter, Setter
}
