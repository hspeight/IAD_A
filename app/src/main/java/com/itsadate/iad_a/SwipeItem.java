package com.itsadate.iad_a;

public class SwipeItem {
    private int id;
    private String title;
    private String info;
    private int time;
    private int direction;
    private int incsec; // include seconds in timer?
    private int usedayyear;// Days only or days & years
    private int bgcolor;

    public SwipeItem(int id, String title, String info, int time, int direction, int incsec, int usedayyear) {
        this.id = id;
        this.info = info;
        this.title = title;
        this.time = time;
        this.direction = direction;
        this.incsec = incsec;
        this.usedayyear = usedayyear;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getInfo() {
        return info;
    }
    public int getTime() {
        return time;
    }
    public int getDirection() {
        return direction;
    }
    public int getIncsec() {
        return incsec;
    }
    public int getDayyear() {
        return usedayyear;
    }
    public int getBgcolor() {
        return bgcolor;
    }

}