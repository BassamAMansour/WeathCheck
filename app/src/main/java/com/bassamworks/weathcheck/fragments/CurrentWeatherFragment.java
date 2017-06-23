package com.bassamworks.weathcheck.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bassamworks.weathcheck.R;
import com.bassamworks.weathcheck.dataTypes.Location;
import com.bassamworks.weathcheck.dataTypes.Weather;
import com.bassamworks.weathcheck.loaders.CurrentWeatherLoader;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.bassamworks.weathcheck.activities.MainActivity.STATUS_DONE_LOADING;
import static com.bassamworks.weathcheck.activities.MainActivity.STATUS_LOADING;
import static com.bassamworks.weathcheck.activities.MainActivity.STATUS_NO_CONNECTION;

/**
 * Created by Bassam on 6/21/2017.
 */

public class CurrentWeatherFragment extends Fragment implements LoaderManager.LoaderCallbacks<Weather> {
    public static final int CURRENT_WEATHER_LOADER_ID = 1;
    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.rootView = inflater.inflate(R.layout.current_weather, container, false);

        if (isConnectedToInternet()) {
            setLayoutStatus(STATUS_LOADING);
            getLoaderManager().initLoader(CURRENT_WEATHER_LOADER_ID, null, this);
        } else {
            setLayoutStatus(STATUS_NO_CONNECTION);
        }

        return rootView;
    }


    @Override
    public Loader<Weather> onCreateLoader(int id, Bundle args) {
        return new CurrentWeatherLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Weather> loader, Weather weather) {
        if (weather != null && weather.getLocation() != null && weather.getLocation().getCityName() != null) {
            updateMainLayout(weather);
            setLayoutStatus(STATUS_DONE_LOADING);
        }
    }


    @Override
    public void onLoaderReset(Loader<Weather> loader) {

    }

    private void updateMainLayout(Weather weather) {

        updateLocation(weather.getLocation());
        updateWeatherType(weather.getWeatherType());
        updateWeatherDescription(weather.getWeatherDescription());
        updateWeatherImage(weather.getWeatherIconID());
        updateTemperature(weather.getTemperature(), weather.getUnits());
        updatePressure(weather.getPressure());
        updateHumidityPercentage(weather.getHumidityPercentage());
        updateWindSpeed(weather.getWindSpeed(), weather.getUnits());
        updateWindDirection(weather.getWindDegree());
        updateMeasuringTime(weather.getDateUNIX());
    }


    private void updateLocation(Location location) {
        String locationText = location.getCityName();
        locationText += ", ";
        locationText += location.getCountry();

        TextView locationView = (TextView) rootView.findViewById(R.id.current_weather_location_textView);
        locationView.setText(locationText);
    }


    private void updateWeatherType(String weatherType) {
        TextView weatherTypeView = (TextView) rootView.findViewById(R.id.weather_type_textView);
        weatherTypeView.setText(weatherType);
    }


    private void updateWeatherDescription(String weatherDescription) {
        TextView weatherDescriptionView = (TextView) rootView.findViewById(R.id.weather_description_textView);
        weatherDescriptionView.setText(weatherDescription);
    }


    private void updateWeatherImage(String weatherIconID) {
        ImageView currentImage = (ImageView) rootView.findViewById(R.id.current_weather_image);
        currentImage.setImageResource(getContext().getResources().getIdentifier(weatherIconID, "drawable", getContext().getPackageName()));
    }


    private void updateTemperature(float temperature, String unit) {
        TextView currentTemperature = (TextView) rootView.findViewById(R.id.current_temperature_value_textView);

        String temperatureString = Float.toString(temperature);

        if (unit.equals(Weather.UNIT_IMPERIAL)) {
            temperatureString += " °F";
        } else if (unit.equals(Weather.UNIT_METRIC)) {
            temperatureString += " °C";
        } else {
            temperatureString += " °K";
        }

        currentTemperature.setText(temperatureString);
    }


    private void updatePressure(float pressure) {
        TextView currentPressure = (TextView) rootView.findViewById(R.id.current_pressure_value_textView);

        String pressureString = Float.toString(pressure);

        pressureString += " hPa";

        currentPressure.setText(pressureString);
    }


    private void updateHumidityPercentage(float humidityPercentage) {
        TextView currentHumidity = (TextView) rootView.findViewById(R.id.current_humidity_value_textView);

        String humidityString = Float.toString(humidityPercentage);

        humidityString += "%";

        currentHumidity.setText(humidityString);
    }


    private void updateWindSpeed(float windSpeed, String unit) {
        TextView currentWindSpeed = (TextView) rootView.findViewById(R.id.current_wind_speed_value_textView);

        String windSpeedText = Float.toString(windSpeed);

        if (unit.equals(Weather.UNIT_IMPERIAL)) {

            windSpeedText += " miles/h";

        } else {
            windSpeedText += " meters/s";
        }

        currentWindSpeed.setText(windSpeedText);
    }


    private void updateWindDirection(float windDegree) {
        TextView currentWindDegree = (TextView) rootView.findViewById(R.id.current_wind_direction_value_textView);

        String windDegreeText = Float.toString(windDegree);

        windDegreeText += "°";

        currentWindDegree.setText(windDegreeText);
    }

    private void updateMeasuringTime(long dateUNIX) {
        TextView currentMeasuringTime = (TextView) rootView.findViewById(R.id.current_measuring_time_value_textView);

        currentMeasuringTime.setText(getFormattedDate(dateUNIX));
    }

    private boolean isConnectedToInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    public void setLayoutStatus(short layoutStatus) {

        switch (layoutStatus) {
            case STATUS_LOADING:
                setLoadingLayoutVisibility(rootView.VISIBLE);
                setNoConnectionLayoutVisibility(rootView.GONE);
                setForecastLayoutVisibility(rootView.GONE);
                break;
            case STATUS_DONE_LOADING:
                setLoadingLayoutVisibility(rootView.GONE);
                setNoConnectionLayoutVisibility(rootView.GONE);
                setForecastLayoutVisibility(rootView.VISIBLE);
                break;
            case STATUS_NO_CONNECTION:
                setLoadingLayoutVisibility(rootView.GONE);
                setNoConnectionLayoutVisibility(rootView.VISIBLE);
                setForecastLayoutVisibility(rootView.GONE);
                break;
        }
    }

    private void setLoadingLayoutVisibility(int visibilityID) {
        RelativeLayout loadingLayout = (RelativeLayout) rootView.findViewById(R.id.loading_relativeLayout);
        loadingLayout.setVisibility(visibilityID);
    }

    private void setNoConnectionLayoutVisibility(int visibilityID) {
        TextView noConnectionLayout = (TextView) rootView.findViewById(R.id.no_connection_textView);
        noConnectionLayout.setVisibility(visibilityID);
    }

    private void setForecastLayoutVisibility(int visibilityID) {
        RelativeLayout currentWeatherLayout = (RelativeLayout) rootView.findViewById(R.id.current_weather_main_relativeLayout);
        currentWeatherLayout.setVisibility(visibilityID);
    }

    private String getFormattedDate(long timeUNIX) {

        Date date = new Date(timeUNIX);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a MMM dd, yyyy");

        return dateFormatter.format(date);
    }

}
