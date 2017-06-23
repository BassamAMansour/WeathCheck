package com.bassamworks.weathcheck.utilities;

import android.util.Log;

import com.bassamworks.weathcheck.dataTypes.Location;
import com.bassamworks.weathcheck.dataTypes.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bassam on 6/22/2017.
 */

public class JSONWeatherParser {

    private final String LOG_TAG = getClass().getSimpleName();
    private String unit;

    public Weather getCurrentWeather(String currentWeatherJSON, String unit) {

        this.unit = unit;

        Weather currentWeather = new Weather(unit);
        if (currentWeatherJSON == null || currentWeatherJSON.isEmpty()) {
            return currentWeather;
        }

        try {

            JSONObject rootObject = new JSONObject(currentWeatherJSON);

            currentWeather.setLocation(getCurrentWeatherLocation(rootObject));
            currentWeather.setDateUNIX(rootObject.getLong("dt")*1000);
            JSONObject weatherMainInfoObject = rootObject.getJSONObject("main");
            currentWeather = setCurrentWeatherMainInfo(currentWeather, weatherMainInfoObject);

            JSONArray weatherTypeArray = rootObject.getJSONArray("weather");
            currentWeather = setWeatherTypeInfo(currentWeather, weatherTypeArray.getJSONObject(0));

            JSONObject windInfoObject = rootObject.getJSONObject("wind");
            currentWeather = setWeatherWindInfo(currentWeather, windInfoObject);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing the JSON current weather root object", e);
        }

        return currentWeather;
    }


    public List<Weather> getForecastWeather(String forecastWeatherJSON, String unit) {

        this.unit = unit;

        ArrayList<Weather> forecastWeather = new ArrayList<>();
        if (forecastWeatherJSON == null || forecastWeatherJSON.isEmpty()) {
            return forecastWeather;
        }
        try {

            JSONObject rootObject = new JSONObject(forecastWeatherJSON);

            JSONArray forecastsArray = rootObject.getJSONArray("list");

            Location location = getForecastLocation(rootObject.getJSONObject("city"));

            for (int i = 0; i < forecastsArray.length(); i++) {

                Weather weather = getForecastWeather(forecastsArray.getJSONObject(i));

                weather.setLocation(location);
                weather.setDateUNIX(forecastsArray.getJSONObject(i).getLong("dt")*1000);
                forecastWeather.add(weather);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing the JSON forecast root object", e);
        }

        return forecastWeather;
    }

    private Location getCurrentWeatherLocation(JSONObject rootObject) {

        Location location = new Location(0, 0);

        try {

            location.setCityName(rootObject.getString("name"));
            location.setCountry(rootObject.getJSONObject("sys").getString("country"));
            location.setLatitude((float) rootObject.getJSONObject("coord").getDouble("lat"));
            location.setLongitude((float) rootObject.getJSONObject("coord").getDouble("lon"));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing the JSON current weather location object", e);
        }


        return location;
    }

    private Location getForecastLocation(JSONObject cityObject) {

        Location location = new Location(0, 0);

        try {

            location.setCityName(cityObject.getString("name"));
            location.setCountry(cityObject.getString("country"));

            JSONObject coordinatesObject = cityObject.getJSONObject("coord");
            location.setLatitude((float) coordinatesObject.getDouble("lat"));
            location.setLongitude((float) coordinatesObject.getDouble("lon"));


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing the JSON forecast city object", e);
        }

        return location;
    }

    private Weather getForecastWeather(JSONObject weatherObject) {

        Weather weather = new Weather(unit);

        try {

            weather.setDateUNIX(weatherObject.getLong("dt")*1000);

            JSONObject weatherMainInfoObject = weatherObject.getJSONObject("main");
            weather = setForecastWeatherMainInfo(weather, weatherMainInfoObject);

            JSONArray weatherTypeInfoArray = weatherObject.getJSONArray("weather");
            weather = setWeatherTypeInfo(weather, weatherTypeInfoArray.getJSONObject(0));

            JSONObject windInfoObject = weatherObject.getJSONObject("wind");
            weather = setWeatherWindInfo(weather, windInfoObject);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing the JSON object", e);
        }

        return weather;
    }

    private Weather setCurrentWeatherMainInfo(Weather currentWeather, JSONObject weatherMainInfoObject) {

        try {

            currentWeather.setPressure((float) weatherMainInfoObject.getDouble("pressure"));
            currentWeather.setTemperature((float) weatherMainInfoObject.getDouble("temp"));
            currentWeather.setHumidityPercentage((float) weatherMainInfoObject.getDouble("humidity"));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing the JSON current weather main info object", e);
        }


        return currentWeather;
    }

    private Weather setForecastWeatherMainInfo(Weather weather, JSONObject weatherMainInfoObject) {

        try {

            weather.setTemperature((float) weatherMainInfoObject.getDouble("temp"));
            weather.setPressure((float) weatherMainInfoObject.getDouble("pressure"));
            weather.setHumidityPercentage((float) weatherMainInfoObject.getDouble("humidity"));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing the JSON forecast weather main object", e);
        }

        return weather;
    }

    private Weather setWeatherTypeInfo(Weather weather, JSONObject weatherTypeObject) {

        try {
            weather.setWeatherType(weatherTypeObject.getString("main"));
            weather.setWeatherDescription(weatherTypeObject.getString("description"));
            weather.setWeatherIconID("i_" + weatherTypeObject.getString("icon"));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing the JSON current/forecast weather type object", e);
        }

        return weather;
    }

    private Weather setWeatherWindInfo(Weather weather, JSONObject windInfoObject) {
        try {

            weather.setWindSpeed((float) windInfoObject.getDouble("speed"));
            weather.setWindDegree((float) windInfoObject.getDouble("deg"));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing the JSON current/forecast wind info object", e);
        }
        return weather;
    }
}
