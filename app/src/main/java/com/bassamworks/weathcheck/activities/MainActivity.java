package com.bassamworks.weathcheck.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bassamworks.weathcheck.R;
import com.bassamworks.weathcheck.adapters.WeatherFragmentsPagerAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;


public class MainActivity extends AppCompatActivity {

    public final static int NUMBER_OF_FRAGMENTS = 2;
    public final static int CURRENT_WEATHER_FRAGMENT_POSITION = 0;
    public final static int FORECAST_WEATHER_FRAGMENT_POSITION = 1;
    public static final short STATUS_LOADING = 0;
    public static final short STATUS_DONE_LOADING = 1;
    public static final short STATUS_NO_CONNECTION = 2;
    private final String LOG_TAG = getClass().getSimpleName();
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private final int SETTINGS_REQUEST_CODE = 2;
    public ViewPager viewPager;
    public WeatherFragmentsPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.viewPager = (ViewPager) findViewById(R.id.weather_viewPager);

        this.pagerAdapter = new WeatherFragmentsPagerAdapter(getSupportFragmentManager(), MainActivity.this);

        refreshViewPager();

        TabLayout weatherTabLayout = (TabLayout) findViewById(R.id.weather_tabLayout);

        weatherTabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.location_search:
                launchPlaceAutoComplete();
                break;
            case R.id.settings:
                launchSettingsActivity();
                break;
            case R.id.reload_info:
                refreshViewPager();
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case PLACE_AUTOCOMPLETE_REQUEST_CODE:

                if (resultCode == RESULT_OK && data != null) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    setLocationPreference(place);
                    refreshViewPager();
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    Log.e(LOG_TAG, status.getStatusMessage());
                }
                break;
            case SETTINGS_REQUEST_CODE:
                if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
                    refreshViewPager();
                }
        }
    }


    private void refreshViewPager() {
        viewPager.setAdapter(pagerAdapter);

    }

    private void setLocationPreference(Place place) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferences.edit().putString(getString(R.string.adress), place.getAddress().toString()).apply();
        sharedPreferences.edit().putString(getString(R.string.latitude_key), Float.toString((float) place.getLatLng().latitude)).apply();
        sharedPreferences.edit().putString(getString(R.string.longitude_key), Float.toString((float) place.getLatLng().longitude)).apply();
    }


    private void launchPlaceAutoComplete() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE).build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(LOG_TAG, "Google Play Repairable Error", e);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(LOG_TAG, "Google Play Not Available Error", e);
        }
    }


    private void launchSettingsActivity() {
        Intent settingActivityIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingActivityIntent);
    }


}
