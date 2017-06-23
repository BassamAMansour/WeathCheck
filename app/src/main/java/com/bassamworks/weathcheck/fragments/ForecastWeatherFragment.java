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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bassamworks.weathcheck.R;
import com.bassamworks.weathcheck.adapters.ForecastWeatherArrayAdapter;
import com.bassamworks.weathcheck.dataTypes.Location;
import com.bassamworks.weathcheck.dataTypes.Weather;
import com.bassamworks.weathcheck.loaders.ForecastWeatherLoader;

import java.util.List;

import static com.bassamworks.weathcheck.activities.MainActivity.STATUS_DONE_LOADING;
import static com.bassamworks.weathcheck.activities.MainActivity.STATUS_LOADING;
import static com.bassamworks.weathcheck.activities.MainActivity.STATUS_NO_CONNECTION;

/**
 * Created by Bassam on 6/21/2017.
 */

public class ForecastWeatherFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Weather>> {

    public static final int FORECAST_LOADER_ID = 1;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.rootView = inflater.inflate(R.layout.forecast_weather, container, false);

        if (isConnectedToInternet()) {
            setLayoutStatus(STATUS_LOADING);
            getLoaderManager().initLoader(FORECAST_LOADER_ID, null, this);
        } else {
            setLayoutStatus(STATUS_NO_CONNECTION);
        }

        return rootView;
    }

    @Override
    public Loader<List<Weather>> onCreateLoader(int id, Bundle args) {
        return new ForecastWeatherLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Weather>> loader, List<Weather> forecastList) {

        if (forecastList != null && forecastList.size() != 0) {
            updateListView(forecastList);
            updateLocation(forecastList.get(0).getLocation());
            setLayoutStatus(STATUS_DONE_LOADING);
        }
    }


    @Override
    public void onLoaderReset(Loader<List<Weather>> loader) {

    }

    private void updateLocation(Location location) {

        String locationText = location.getCityName();
        locationText += ", ";
        locationText += location.getCountry();

        TextView locationView = (TextView) rootView.findViewById(R.id.forecast_weather_location_textView);
        locationView.setText(locationText);
    }

    private void updateListView(List<Weather> forecastList) {

        ForecastWeatherArrayAdapter adapter = new ForecastWeatherArrayAdapter(getActivity(), forecastList);

        ListView forecastListView = (ListView) rootView.findViewById(R.id.forecast_listView);

        forecastListView.setAdapter(adapter);
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
        LinearLayout forecastLayout = (LinearLayout) rootView.findViewById(R.id.forecast_weather_main_linearLayout);
        forecastLayout.setVisibility(visibilityID);
    }

    private boolean isConnectedToInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }
}
