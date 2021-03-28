package com.example.android.sqliteweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import com.example.android.sqliteweather.data.FiveDayForecast;
import com.example.android.sqliteweather.data.ForecastCity;
import com.example.android.sqliteweather.data.ForecastData;
import com.example.android.sqliteweather.data.LoadingStatus;
import com.example.android.sqliteweather.utils.OpenWeatherUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

//, CityNameAdapter.OnLocationItemClickListener
public class MainActivity extends AppCompatActivity
        implements ForecastAdapter.OnForecastItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener,
        NavigationView.OnNavigationItemSelectedListener,CityNameAdapter.OnLocationItemClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    /*
     * To use your own OpenWeather API key, create a file called `gradle.properties` in your
     * GRADLE_USER_HOME directory (this will usually be `$HOME/.gradle/` in MacOS/Linux and
     * `$USER_HOME/.gradle/` in Windows), and add the following line:
     *
     *   OPENWEATHER_API_KEY="<put_your_own_OpenWeather_API_key_here>"
     *
     * The Gradle build for this project is configured to automatically grab that value and store
     * it in the field `BuildConfig.OPENWEATHER_API_KEY` that's used below.  You can read more
     * about this setup on the following pages:
     *
     *   https://developer.android.com/studio/build/gradle-tips#share-custom-fields-and-resource-values-with-your-app-code
     *
     *   https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties
     *
     * Alternatively, you can just hard-code your API key below 🤷‍.  If you do hard code your API
     * key below, make sure to get rid of the following line (line 18) in build.gradle:
     *
     *   buildConfigField("String", "OPENWEATHER_API_KEY", OPENWEATHER_API_KEY)
     */
    private static final String OPENWEATHER_APPID = BuildConfig.OPENWEATHER_API_KEY;

    private ForecastAdapter forecastAdapter;
   // private CityNameAdapter cityNameAdapter;
    private FiveDayForecastViewModel fiveDayForecastViewModel;

    private SharedPreferences sharedPreferences;

    private ForecastCity forecastCity;

    private RecyclerView forecastListRV;
    private ProgressBar loadingIndicatorPB;
    private TextView errorMessageTV;

    private Toast errorToast;

    private DrawerLayout drawerLayout;

    private SavedCityViewHolder cityNameViewModel;
    private long timeSearched;

    private RecyclerView cityNamesRV;
    private CityNameAdapter cityNameAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);
        this.forecastListRV = findViewById(R.id.rv_forecast_list);
        this.forecastListRV.setLayoutManager(new LinearLayoutManager(this));
        this.forecastListRV.setHasFixedSize(true);

        this.forecastAdapter = new ForecastAdapter(this);
        this.forecastListRV.setAdapter(this.forecastAdapter);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        this.drawerLayout = findViewById(R.id.drawer_layout);//attach the layout set up in activity_main.xml

        this.fiveDayForecastViewModel = new ViewModelProvider(this)
                .get(FiveDayForecastViewModel.class);
        this.loadForecast();

        /*
         * Update UI to reflect newly fetched forecast data.
         */
        this.fiveDayForecastViewModel.getFiveDayForecast().observe(
                this,
                new Observer<FiveDayForecast>() {
                    @Override
                    public void onChanged(FiveDayForecast fiveDayForecast) {
                        forecastAdapter.updateForecastData(fiveDayForecast);
                        if (fiveDayForecast != null) {
                            forecastCity = fiveDayForecast.getForecastCity();
                            ActionBar actionBar = getSupportActionBar();
                            actionBar.setTitle(forecastCity.getName());
                        }
                    }
                }
        );

        /*
         * Update UI to reflect changes in loading status.
         */
        this.fiveDayForecastViewModel.getLoadingStatus().observe(
                this,
                new Observer<LoadingStatus>() {
                    @Override
                    public void onChanged(LoadingStatus loadingStatus) {
                        if (loadingStatus == LoadingStatus.LOADING) {
                            loadingIndicatorPB.setVisibility(View.VISIBLE);
                        } else if (loadingStatus == LoadingStatus.SUCCESS) {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            forecastListRV.setVisibility(View.VISIBLE);
                            errorMessageTV.setVisibility(View.INVISIBLE);
                        } else {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            forecastListRV.setVisibility(View.INVISIBLE);
                            errorMessageTV.setVisibility(View.VISIBLE);
                            errorMessageTV.setText(getString(R.string.loading_error, "ヽ(。_°)ノ"));
                        }
                    }
                }
        );


        NavigationView navigationView = findViewById(R.id.nv_nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        //

        //grad the toolbar out from the layout, and tell the activity to use it as the regular toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);



    }

    @Override
    public void onForecastItemClick(ForecastData forecastData) {
        Intent intent = new Intent(this, ForecastDetailActivity.class);
        intent.putExtra(ForecastDetailActivity.EXTRA_FORECAST_DATA, forecastData);
        intent.putExtra(ForecastDetailActivity.EXTRA_FORECAST_CITY, this.forecastCity);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                viewForecastCityInMap();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                this.drawerLayout.openDrawer(GravityCompat.START);
                /*Part 4
                  we can add code to initialize our RecyclerView.  First, we can grab a reference to it using its ID.
                  Then, we can add a new LinearLayoutManager to make sure our list items are laid out in a list,
                  and we can let the RecyclerView know that all of our items have the same size,
                  which will allow it to perform some rendering optimizations.
                 */
                cityNamesRV = findViewById(R.id.rv_saved_cities_list);
                //cityNameAdapter = new CityNameAdapter(this,this);
                cityNameAdapter = new CityNameAdapter(this);
                cityNamesRV.setLayoutManager(new LinearLayoutManager(this));
                cityNamesRV.setHasFixedSize(true);
                cityNamesRV.setAdapter(cityNameAdapter);
                //**************************************************************
                //cityNameAdapter.updateLocationItems(getSavedCity());
                //**************************************************************
                /*In this case, because our ViewModel is an instance of the AndroidViewModel class, we’ll
                 have to pass an AndroidViewModelFactory instance into the constructor of ViewModelProvider.
                 This will supply the Application instance needed by the AndroidViewModel class
                */
                this.cityNameViewModel = new ViewModelProvider(
                        this,
                        new ViewModelProvider.AndroidViewModelFactory(getApplication())
                ).get(SavedCityViewHolder.class);

                cityNameViewModel.getAllFavoriteCities().observe(this, new Observer<List<ForecastCity>>() {
                    @Override
                    public void onChanged(@Nullable List<ForecastCity> locations) {
                        cityNameAdapter.updateLocationItems(locations);
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //make sure the navigation drawer closes before we navigate by calling
        this.drawerLayout.closeDrawers();
//        switch (item.getItemId()) {
////            case R.id.nav_search://change this to "Add Location"
////                return true;
//            case R.id.nav_saved_repos:
////                Intent savedReposIntent  = new Intent(this, SavedReposActivity.class);
////                startActivity(savedReposIntent );
//                return true;
//            case R.id.nav_settings:
//                Intent settingsIntent = new Intent(this, SettingsActivity.class);
//                startActivity(settingsIntent);
//                return true;
//            default:
//                return false;
//        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //this.cityNameAdapter.updateLocationItems(this.forecastCity);
        this.loadForecast();
        //this.forecastCity.setName(this.forecastCity.getName());
        this.timeSearched = System.currentTimeMillis();
        this.forecastCity.setTimeStamp(timeSearched);
        this.cityNameViewModel.insertFavoriteCity(this.forecastCity);
    }

    /**
     * Triggers a new forecast to be fetched based on current preference values.
     */
    private void loadForecast() {
        this.fiveDayForecastViewModel.loadForecast(
                this.sharedPreferences.getString(
                        getString(R.string.pref_location_key),
                        "Corvallis,OR,US"
                ),
                this.sharedPreferences.getString(
                        getString(R.string.pref_units_key),
                        getString(R.string.pref_units_default_value)
                ),
                OPENWEATHER_APPID
        );
    }

    /**
     * This function uses an implicit intent to view the forecast city in a map.
     */
    private void viewForecastCityInMap() {
        if (this.forecastCity != null) {
            Uri forecastCityGeoUri = Uri.parse(getString(
                    R.string.geo_uri,
                    this.forecastCity.getLatitude(),
                    this.forecastCity.getLongitude(),
                    12
            ));
            Intent intent = new Intent(Intent.ACTION_VIEW, forecastCityGeoUri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                if (this.errorToast != null) {
                    this.errorToast.cancel();
                }
                this.errorToast = Toast.makeText(
                        this,
                        getString(R.string.action_map_error),
                        Toast.LENGTH_LONG
                );
                this.errorToast.show();
            }
        }
    }


    @Override
    public void onLocationItemClicked(ForecastCity forecastCity) {
        drawerLayout.closeDrawers();
        //perfect ran
        //***************************************************************************************************
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor clickedCity = sharedPreferences.edit();
        clickedCity.putString("location pref name", String.valueOf(forecastCity.getName()));
        clickedCity.commit();
        Log.d(TAG, "changing to location" + forecastCity.getName());
        String units = sharedPreferences.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_default_value)
        );
        String location = forecastCity.getName();
        fiveDayForecastViewModel.loadForecast(location, units,OPENWEATHER_APPID);
        //***************************************************************************************************

//        Intent intent = new Intent(this, ForecastDetailActivity.class);
//        intent.putExtra(ForecastDetailActivity.EXTRA_FORECAST_DATA, this.forecastData);
//        intent.putExtra(ForecastDetailActivity.EXTRA_FORECAST_CITY, forecastCity);
//        startActivity(intent);


    }
}