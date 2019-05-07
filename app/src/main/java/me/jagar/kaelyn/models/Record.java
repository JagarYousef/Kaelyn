package me.jagar.kaelyn.models;

public class Record {

    public double amount, rate, time;
    public String note;
    public long date;
    public int emo;
    public int id;



    public Record(double amount, double rate, double time, String note, long date, int emo, int id) {
        this.amount = amount;
        this.rate = rate;
        this.time = time;
        this.note = note;
        this.date = date;
        this.emo = emo;
        this.id = id;
    }

    public Record() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getEmo() {
        return emo;
    }

    public void setEmo(int emo) {
        this.emo = emo;
    }
}
