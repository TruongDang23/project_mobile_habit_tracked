package com.example.finalproject.model;

import java.io.Serializable;

public class ListviewHomeTest implements Serializable {
    private String habitId;
    private String nameHabit;
    private String section;
    private String timeHabit;
    private String doneProgress;
    private int done;
    private double donViTang;
    private String status;
    private Double doing;
    public ListviewHomeTest(String habitID,String nameHabit, String section, String timeHabit, String doneProgress, int done, double donVi, String status, Double doing) {
        this.habitId = habitID;
        this.nameHabit = nameHabit;
        this.section = section;
        this.timeHabit = timeHabit;
        this.doneProgress = doneProgress;
        this.done = done;
        this.donViTang = donVi;
        this.status = status;
        this.doing = doing;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getDoing() {
        return doing;
    }

    public void setDoing(Double doing) {
        this.doing = doing;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public double getDonViTang() {
        return donViTang;
    }

    public void setDonViTang(double donViTang) {
        this.donViTang = donViTang;
    }

    public String getHabitId() {
        return habitId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public String getNameHabit() {
        return nameHabit;
    }

    public void setNameHabit(String nameHabit) {
        this.nameHabit = nameHabit;
    }

    public String getTimeHabit() {
        return timeHabit;
    }

    public void setTimeHabit(String timeHabit) {
        this.timeHabit = timeHabit;
    }

    public String getDoneProgress() {
        return doneProgress;
    }
    public int getDone() {return done;}

    public void setDoneProgress(String doneProgress) {
        this.doneProgress = doneProgress;
    }
}
