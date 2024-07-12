package com.example.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private final UUID Id;
    private String Title;
    private Date Date;
    private boolean Solved;

    public Crime() {
        Id = UUID.randomUUID();
        Date = new Date();
    }

    public UUID getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String mTitle) {
        this.Title = mTitle;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public boolean isSolved() {
        return Solved;
    }

    public void setSolved(boolean solved) {
        Solved = solved;
    }
}
