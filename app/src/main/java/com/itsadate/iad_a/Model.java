package com.itsadate.iad_a;

public class Model {

    private String title;
    private String time;
    private int value;
    private boolean checked = false;

    public Model(String title, int value) {
        this.title = title;
        this.value = value;
    }
    public Model(String title, String time) {
        this.title = title;
        this.time = time;
    }
    public Model(String title, boolean checked) {
        this.title = title;
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }
    public int getValue(){
        return this.value;
    }
    public String getTime() {
        return time;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public boolean isChecked() {
        return checked;
    }
    public void toggleChecked() {
        checked = !checked;
    }
}
