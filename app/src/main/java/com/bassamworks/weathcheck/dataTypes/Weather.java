package com.bassamworks.weathcheck.dataTypes;

/**
 * Created by Bassam on 6/21/2017.
 */

public class Weather {

    public static final String UNIT_STANDARD = "standard";
    public static final String UNIT_IMPERIAL= "imperial";
    public static final String UNIT_METRIC = "metric";

    String units;
    Location location;
    long dateUNIX;
    float temperature;
    float pressure;
    float humidityPercentage;
    String weatherType;
    String weatherDescription;
    String weatherIconID;

    float windSpeed;
    float windDegree;

    public Weather(String units) {
        this.units = units;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getDateUNIX() {
        return dateUNIX;
    }

    public void setDateUNIX(long dateUNIX) {
        this.dateUNIX = dateUNIX;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidityPercentage() {
        return humidityPercentage;
    }

    public void setHumidityPercentage(float humidityPercentage) {
        this.humidityPercentage = humidityPercentage;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherIconID() {
        return weatherIconID;
    }

    public void setWeatherIconID(String weatherIconID) {
        this.weatherIconID = weatherIconID;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(float windDegree) {
        this.windDegree = windDegree;
    }
}
