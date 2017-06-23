package com.bassamworks.weathcheck.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bassamworks.weathcheck.R;
import com.bassamworks.weathcheck.dataTypes.Weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Bassam on 6/21/2017.
 */

public class ForecastWeatherArrayAdapter extends ArrayAdapter<Weather> {

    public ForecastWeatherArrayAdapter(Context context, List<Weather> weatherList) {
        super(context, 0, weatherList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View forecastListItem = convertView;
        Weather weather = getItem(position);

        if (forecastListItem == null) {
            forecastListItem = LayoutInflater.from(getContext()).inflate(R.layout.forecast_list_view_item, parent, false);
        }

        modifyWeatherImage(forecastListItem, weather.getWeatherIconID());
        modifyTemperature(forecastListItem, weather.getTemperature(), weather.getUnits());
        modifyPressure(forecastListItem, weather.getPressure());
        modifyHumidityPercentage(forecastListItem, weather.getHumidityPercentage());
        modifyWindSpeed(forecastListItem, weather.getWindSpeed(), weather.getUnits());
        modifyWindDirection(forecastListItem, weather.getWindDegree());
        modifyMeasuringTime(forecastListItem, weather.getDateUNIX());

        return forecastListItem;
    }


    private void modifyWeatherImage(View rootView, String weatherIconID) {

        ImageView forecastImage = (ImageView) rootView.findViewById(R.id.forecast_weather_image);

        forecastImage.setImageResource(getContext().getResources().getIdentifier(weatherIconID, "drawable", getContext().getPackageName()));
    }


    private void modifyTemperature(View rootView, float temperature, String unit) {

        TextView forecastTemperature = (TextView) rootView.findViewById(R.id.forecast_temperature_value_textView);

        String temperatureString = Float.toString(temperature);

        if (unit.equals(Weather.UNIT_IMPERIAL)) {
            temperatureString += " °F";
        } else if (unit.equals(Weather.UNIT_METRIC)) {
            temperatureString += " °C";
        } else {
            temperatureString += " °K";
        }

        forecastTemperature.setText(temperatureString);

    }


    private void modifyPressure(View rootView, float pressure) {

        TextView forecastPressure = (TextView) rootView.findViewById(R.id.forecast_pressure_value_textView);

        String pressureString = Float.toString(pressure);

        pressureString += " hPa";

        forecastPressure.setText(pressureString);

    }


    private void modifyHumidityPercentage(View rootView, float humidityPercentage) {

        TextView forecastHumidity = (TextView) rootView.findViewById(R.id.forecast_humidity_value_textView);

        String humidityString = Float.toString(humidityPercentage);

        humidityString += "%";

        forecastHumidity.setText(humidityString);

    }


    private void modifyWindSpeed(View rootView, float windSpeed, String unit) {

        TextView forecastWindSpeed = (TextView) rootView.findViewById(R.id.forecast_wind_speed_value_textView);

        String windSpeedText = Float.toString(windSpeed);

        if (unit.equals(Weather.UNIT_IMPERIAL)) {

            windSpeedText += " miles/h";

        } else {
            windSpeedText += " meters/s";
        }

        forecastWindSpeed.setText(windSpeedText);
    }


    private void modifyWindDirection(View rootView, float windDegree) {
        TextView forecastWindDegree = (TextView) rootView.findViewById(R.id.forecast_wind_direction_value_textView);

        String windDegreeText = Float.toString(windDegree);

        windDegreeText += "°";

        forecastWindDegree.setText(windDegreeText);
    }

    private void modifyMeasuringTime(View rootView, long dateUNIX) {
        TextView forecastMeasuringTime = (TextView) rootView.findViewById(R.id.forecast_measuring_time_value_textView);

        forecastMeasuringTime.setText(getFormattedDate(dateUNIX));
    }

    private String getFormattedDate(long timeUNIX) {

        Date date = new Date(timeUNIX);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a\nMMM dd, yyyy");

        return dateFormatter.format(date);
    }
}
