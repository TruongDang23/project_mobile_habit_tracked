package com.example.finalproject.model;

import java.io.Serializable;
import java.util.Date;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class Habit implements Serializable {
    @PropertyName("DonVi")
    private String donVi;

    @PropertyName("DonViTang")
    private double donViTang;

    @PropertyName("KhoangThoiGian")
    private String khoangThoiGian;

    @PropertyName("LoiNhacNho")
    private String loiNhacNho;


    @PropertyName("MoTa")
    private String moTa;

    @PropertyName("MucTieu")
    private double mucTieu;

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

    // Constructor
    public Habit() {
        // Empty constructor required for Firebase
    }

    // Getters and setters
    @PropertyName("DonVi")
    public String getDonVi() {
        return donVi;
    }

    @PropertyName("DonVi")
    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    @PropertyName("DonViTang")
    public double getDonViTang() {
        return donViTang;
    }
    @PropertyName("DonViTang")
    public void setDonViTang(double donViTang) {
        this.donViTang = donViTang;
    }
    @PropertyName("KhoangThoiGian")
    public String getKhoangThoiGian() {
        return khoangThoiGian;
    }
    @PropertyName("KhoangThoiGian")
    public void setKhoangThoiGian(String khoangThoiGian) {
        this.khoangThoiGian = khoangThoiGian;
    }
    @PropertyName("LoiNhacNho")
    public String getLoiNhacNho() {
        return loiNhacNho;
    }

    @PropertyName("LoiNhacNho")
    public void setLoiNhacNho(String loiNhacNho) {
        this.loiNhacNho = loiNhacNho;
    }
    @PropertyName("MoTa")
    public String getMoTa() {
        return moTa;
    }
    @PropertyName("MoTa")
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    @PropertyName("MucTieu")
    public double getMucTieu() {
        return mucTieu;
    }
    @PropertyName("MucTieu")
    public void setMucTieu(double mucTieu) {
        this.mucTieu = mucTieu;
    }
    @PropertyName("Ten")
    public String getTen() {
        return ten;
    }
    @PropertyName("Ten")
    public void setTen(String ten) {
        this.ten = ten;
    }
    @PropertyName("ThoiDiem")
    public String getThoiDiem() {
        return thoiDiem;
    }
    @PropertyName("ThoiDiem")
    public void setThoiDiem(String thoiDiem) {
        this.thoiDiem = thoiDiem;
    }
    @PropertyName("ThoiGianBatDau")
    public String getThoiGianBatDau() {
        return thoiGianBatDau;
    }
    @PropertyName("ThoiGianBatDau")
    public void setThoiGianBatDau(String thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    @PropertyName("ThoiGianKetThuc")
    public String getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }
    @PropertyName("ThoiGianKetThuc")
    public void setThoiGianKetThuc(String thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }
    @PropertyName("ThoiGianNhacNho")
    public String getThoiGianNhacNho() {
        return thoiGianNhacNho;
    }
    @PropertyName("ThoiGianNhacNho")
    public void setThoiGianNhacNho(String thoiGianNhacNho) {
        this.thoiGianNhacNho = thoiGianNhacNho;
    }
    @PropertyName("TrangThai")
    public String getTrangThai() {
        return trangThai;
    }
    @PropertyName("TrangThai")
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

