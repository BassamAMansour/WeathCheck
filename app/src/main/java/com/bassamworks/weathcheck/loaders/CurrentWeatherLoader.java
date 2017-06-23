package com.bassamworks.weathcheck.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bassamworks.weathcheck.R;
import com.bassamworks.weathcheck.utilities.JSONResponseHTTPRequest;
import com.bassamworks.weathcheck.utilities.JSONWeatherParser;
import com.bassamworks.weathcheck.dataTypes.Weather;
import com.bassamworks.weathcheck.utilities.WeatherQueryURLBuilder;

/**
 * Created by Bassam on 6/22/2017.
 */

public class CurrentWeatherLoader extends AsyncTaskLoader<Weather> {

    private Context context;

    public CurrentWeatherLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Weather loadInBackground() {

        WeatherQueryURLBuilder urlBuilder = new WeatherQueryURLBuilder(context);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String queryURL = urlBuilder.getCurrentWeatherURL(preferences, WeatherQueryURLBuilder.ACCURATE_SEARCH);

        String JSONCurrentWeatherResponse = (new JSONResponseHTTPRequest()).getJSONStringResponse(queryURL);

        JSONWeatherParser jsonWeatherParser = new JSONWeatherParser();

        String unit = preferences.getString(context.getString(R.string.units_key), context.getString(R.string.units_default));

        return jsonWeatherParser.getCurrentWeather(JSONCurrentWeatherResponse, unit);
    }
}
