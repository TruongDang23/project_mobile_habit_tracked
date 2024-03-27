package com.example.finalproject.model;

import java.io.Serializable;

public class ListviewHomeTest implements Serializable {
    private String habitId;
    private String nameHabit;
    private String timeHabit;
    private String doneProgress;
    private int done;
    private double donViTang;

    public ListviewHomeTest() {
    }

    public ListviewHomeTest(String habitID,String nameHabit, String timeHabit, String doneProgress, int done, double donVi) {
        this.habitId = habitID;
        this.nameHabit = nameHabit;
        this.timeHabit = timeHabit;
        this.doneProgress = doneProgress;
        this.done = done;
        this.donViTang = donVi;
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
