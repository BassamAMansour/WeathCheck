package com.bassamworks.weathcheck.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bassamworks.weathcheck.activities.MainActivity;
import com.bassamworks.weathcheck.R;
import com.bassamworks.weathcheck.fragments.CurrentWeatherFragment;
import com.bassamworks.weathcheck.fragments.ForecastWeatherFragment;

/**
 * Created by Bassam on 6/21/2017.
 */

public class WeatherFragmentsPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public WeatherFragmentsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MainActivity.CURRENT_WEATHER_FRAGMENT_POSITION:
                return new CurrentWeatherFragment();

            case MainActivity.FORECAST_WEATHER_FRAGMENT_POSITION:
                return new ForecastWeatherFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return MainActivity.NUMBER_OF_FRAGMENTS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case MainActivity.CURRENT_WEATHER_FRAGMENT_POSITION:
                return context.getString(R.string.current_weather);

            case MainActivity.FORECAST_WEATHER_FRAGMENT_POSITION:
                return context.getString(R.string.weather_forecast);
            default:
                return null;
        }
    }

}
