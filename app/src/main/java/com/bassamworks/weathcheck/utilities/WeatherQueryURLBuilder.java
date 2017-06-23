package com.bassamworks.weathcheck.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.bassamworks.weathcheck.R;
import com.bassamworks.weathcheck.dataTypes.Weather;

/**
 * Created by Bassam on 6/22/2017.
 */

public class WeatherQueryURLBuilder {

    public static final String ACCURATE_SEARCH = "accurate";
    public static final String APPROXIMATE_SEARCH = "like";
    private final String LOG_TAG = getClass().getSimpleName();
    private final String BASE_FORECAST_WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?q=";
    private final String BASE_CURRENT_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private Context context;

    public WeatherQueryURLBuilder(Context context) {
        this.context = context;
    }

    public String getForecastURL(String latitude, String longitude, String units, String accuracy) {
        return getQueryURL(BASE_FORECAST_WEATHER_URL, latitude, longitude, units, accuracy);
    }

    public String getCurrentWeatherURL(String latitude, String longitude, String units, String accuracy) {
        return getQueryURL(BASE_CURRENT_WEATHER_URL, latitude, longitude, units, accuracy);
    }

    public String getForecastURL(SharedPreferences preferences, String accuracy) {
        return getQueryURL(BASE_FORECAST_WEATHER_URL, preferences, accuracy);
    }

    public String getCurrentWeatherURL(SharedPreferences preferences, String accuracy) {
        return getQueryURL(BASE_CURRENT_WEATHER_URL, preferences, accuracy);
    }

    private String getQueryURL(String baseURL, SharedPreferences preferences, String accuracy) {

        String latitude = preferences.getString(context.getString(R.string.latitude_key), context.getString(R.string.latitude_default));
        String longitude = preferences.getString(context.getString(R.string.longitude_key), context.getString(R.string.longitude_default));
        String units = preferences.getString(context.getString(R.string.units_key), context.getString(R.string.units_default));

        return getQueryURL(baseURL, latitude, longitude, units, accuracy);
    }


    private String getQueryURL(String baseURL, String latitude, String longitude, String units, String accuracy) {

        Uri baseUri = Uri.parse(baseURL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("appid", context.getString(R.string.open_weather_map_appid));

        uriBuilder.appendQueryParameter("lat", latitude);
        uriBuilder.appendQueryParameter("lon", longitude);

        if (!units.equals(Weather.UNIT_STANDARD)) {
            uriBuilder.appendQueryParameter("units", units);
        }

        uriBuilder.appendQueryParameter("type", accuracy);

        return uriBuilder.build().toString();
    }

}
