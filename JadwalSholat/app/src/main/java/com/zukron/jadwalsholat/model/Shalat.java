package com.zukron.jadwalsholat.model;

import org.threeten.bp.LocalTime;

import java.util.ArrayList;

/**
 * Project name is Jadwal Sholat
 * Created by Zukron Alviandy R on 7/21/2020
 */
public class Shalat {
    private String cityCode;
    private String dateToday;
    private ArrayList<String> name;
    private ArrayList<LocalTime> times;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDateToday() {
        return dateToday;
    }

    public void setDateToday(String dateToday) {
        this.dateToday = dateToday;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public ArrayList<LocalTime> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<LocalTime> times) {
        this.times = times;
    }
}
