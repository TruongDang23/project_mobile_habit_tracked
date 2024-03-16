package com.example.finalproject.model;

import java.io.Serializable;

public class ListviewHomeTest implements Serializable {
    private String nameHabit;
    private String timeHabit;
    private String doneProgress;

    public ListviewHomeTest() {
    }

    public ListviewHomeTest(String nameHabit, String timeHabit, String doneProgress) {
        this.nameHabit = nameHabit;
        this.timeHabit = timeHabit;
        this.doneProgress = doneProgress;
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

    public void setDoneProgress(String doneProgress) {
        this.doneProgress = doneProgress;
    }
}
