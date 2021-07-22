package com.example.parkinson.model.general_models;

public class Time {
    private int minutes, hour;

    public Time(){}

    public Time(int minutes, int hour) {
        this.minutes = minutes;
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String fullTime(){
        String hoursString;
        String minutesString;
        if(hour < 10){
            hoursString = "0" + hour;
        } else {
            hoursString = String.valueOf(hour);
        }

        if(minutes == 0){
            minutesString = "00";
        } else {
            minutesString = "30";
        }

        return hoursString +""+ minutesString;
    }
}
