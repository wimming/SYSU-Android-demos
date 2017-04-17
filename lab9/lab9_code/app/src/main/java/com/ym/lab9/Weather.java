package com.ym.lab9;

/**
 * Created by ym on 16-11-30.
 */

public class Weather {
    private String weather_description = "";
    private String date = "";
    private String temperature = "";

    public Weather(String date, String weather_description, String temperature) {
        this.date = date;
        this.weather_description = weather_description;
        this.temperature = temperature;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDate() {
        return date;
    }

    public String getWeather_description() {
        return weather_description;
    }
}