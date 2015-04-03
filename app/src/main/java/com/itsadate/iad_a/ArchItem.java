package com.itsadate.iad_a;

/** Holds planet data. */
public class ArchItem {
    private String name = "";
    private String time = "";
    private boolean checked = false;

    //public mItems() {
    //}

    //public mItems(String name) {
    //    this.name = name;
    //}
    //public mItems(String name, String time) {
    //    this.name = name;
    //    this.time = time;
    //}

    public ArchItem(String name, String time, boolean checked) {
        this.name = name;
        this.time = time;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }
    public String getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String toString() {
        return name;
    }

    public void toggleChecked() {
        checked = !checked;
    }
}