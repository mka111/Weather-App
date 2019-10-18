package com.bignerdranch.android.forecast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {
    private String location;
    private String icon;
    private long time;
    private double temperature;
    private double feelLike;
    private double humidity;
    private String timeZone;
    private String Day;
    private double wind;

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public String getDay() {
        return Day;
    }

    public CurrentWeather() {
    }


    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = ((temperature-32)*5)/9;
    }

    public double getFeelLike() {
        return feelLike;
    }

    public void setFeelLike(double feelLike) {
        this.feelLike = ((feelLike-32)*5)/9;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = (humidity*100);
    }

    public CurrentWeather(String location, String icon, long time, double temperature, double wind, double humidity, String timeZone, String Day ) {
        this.location = location;
        this.icon = icon;
        this.time = time;
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.timeZone = timeZone;
        this.Day = Day;
    }

    public String FormatTime(){
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date dateTime =  new Date(time*1000);
        return format.format(dateTime);


    }

    public void setDay(long time){

// convert seconds to milliseconds
        Date date = new java.util.Date(time*1000L);
// the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEE");
// give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone(timeZone));
        String formattedDay = sdf.format(date);


        switch (formattedDay){
            case "Mon":
                formattedDay = "Monday";
                break;
            case "Tue":
                formattedDay = "Tuesday";
                break;
            case "Wed":
                formattedDay = "Wednesday";
                break;
            case "Thu":
                formattedDay = "Thursday";
                break;
            case "Fri":
                formattedDay = "Friday";
                break;
            case "Sat":
                formattedDay = "Saturday";
                break;
            case "Sun":
                formattedDay = "Sunday";
                break;
        }

        this.Day = formattedDay;


    }
    public String formatIcon(String icon){
        String summary = "Clear Day";
        switch(icon){
            case "clear-day":
                summary = "Clear Day";
                break;
            case "clear-night":
                summary = "Clear Night";
                break;
            case "rain":
                summary = "Rain";
                break;
            case "sleet":
                summary = "Sleet";
                break;
            case "wind":
                summary = "Wind";
                break;
            case "fog":
                summary = "Fog";
                break;
            case "cloudy":
                summary = "Cloudy";
                break;
            case "partly-cloudy":
                summary = "Partly Cloudy";
                break;
            case "partly-cloudy-night":
                summary = "Partly Cloudy Night";
                break;
            case "snow":
                summary = "Snow";
                break;
        }
        return summary;


    }

    public int getIconNumber(){
        //Icons:
        //clear-day, clear-night, rain, snow, sleet, wind, fog,
        //cloudy, partly-cloudy, partly-cloudy-night.
        int iconid = R.drawable.partlycloudyday;

        switch(icon){
            case "clear-day":
                iconid = R.drawable.clearday;
                break;
            case "clear-night":
                iconid = R.drawable.clearnight;
                break;
            case "rain":
                iconid = R.drawable.rainday;
                break;
            case "sleet":
                iconid = R.drawable.sleetday;
                break;
            case "wind":
                iconid = R.drawable.windyday;
                break;
            case "fog":
                iconid = R.drawable.fogday;
                break;
            case "cloudy":
                iconid = R.drawable.cloudyday;
                break;
            case "partly-cloudy":
                iconid = R.drawable.partlycloudyday;
                break;
            case "partly-cloudy-night":
                iconid = R.drawable.cloudynight;
                break;
            case "snow":
                iconid = R.drawable.snowday;
                break;
        }

        return iconid;


    }

}
