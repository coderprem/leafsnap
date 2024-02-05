package com.leafsnap;

import com.google.gson.annotations.SerializedName;

public class target_weather {

    @SerializedName("weather")
    weather weather;

    public com.leafsnap.weather getWeather() {
        return weather;
    }

    public void setWeather(com.leafsnap.weather weather) {
        this.weather = weather;
    }
}
