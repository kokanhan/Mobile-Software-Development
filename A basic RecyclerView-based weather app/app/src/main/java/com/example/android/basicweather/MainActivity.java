package com.example.android.basicweather;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements WeatherAdapter.OnWeatherChangeListener {

    private RecyclerView mWeatherListRV;
    private WeatherAdapter mWeatherAdapter;
    private Toast mToast;

    //    private TextView todoListTV;
//    private ArrayList<String> todoList;
    private ArrayList<String> mWeatherList;
    private ArrayList<String> mWeatherDetailedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        todoList = new ArrayList<>();

        this.mWeatherListRV = findViewById(R.id.rv_weather_list);

        this.mWeatherListRV.setLayoutManager(new LinearLayoutManager(this));
        this.mWeatherListRV.setHasFixedSize(true);


        mWeatherAdapter = new WeatherAdapter(this);
        mWeatherListRV.setAdapter(this.mWeatherAdapter);


        this.mWeatherListRV.setItemAnimator(new DefaultItemAnimator());

        mToast = null;


        //add dummy weather forecast
        mWeatherAdapter.addDummyWeather("AM showers", "1 Jan","36°F","36°F","Precip:80");
        mWeatherAdapter.addDummyWeather("AM showers", "2 Jan","35°F","48°F","Precip:80");
        mWeatherAdapter.addDummyWeather("Mostly Cloudy", "3 Jan","40°F","50°F","Precip:45");
        mWeatherAdapter.addDummyWeather("Partly Cloudy", "4 Jan","42°F","52°F","Precip:25");
        mWeatherAdapter.addDummyWeather("PM Showers", "5 Jan","35°F","45°F","Precip:80");
        mWeatherAdapter.addDummyWeather("PM Showers", "6 Jan","36°F","44°F","Precip:25");
        mWeatherAdapter.addDummyWeather("Mostly Cloudy", "7 Jan","33°F","43°F","Precip:88");
        mWeatherAdapter.addDummyWeather("Showers", "8 Jan","40°F","52°F","Precip:77");
        mWeatherAdapter.addDummyWeather("Few Showers", "9 Jan","30°F","55°F","Precip:66");
        mWeatherAdapter.addDummyWeather("AM showers", "10 Jan","36°F","48°F","Precip:65");
        mWeatherAdapter.addDummyWeather("AM showers", "11 Jan","41°F","55°F","Precip:65");
        mWeatherAdapter.addDummyWeather("Mostly Cloudy", "12 Jan","44°F","59°F","Precip:70");
        mWeatherAdapter.addDummyWeather("Partly Cloudy", "13 Jan","34°F","45°F","Precip:70");
        mWeatherAdapter.addDummyWeather("Rain", "14 Jan","36°F","46°F","Precip:80");
        mWeatherAdapter.addDummyWeather("Rainy", "15 Jan","29°F","39°F","Precip:80");


        mWeatherDetailedList = new ArrayList<>();

        //        additional info for dummy forecast data
        mWeatherDetailedList.add("Rain and snow likely before 1pm, then rain.Temperature falling to around 39 by 5pm. Breezy, with a south wind 10 to 15 mph, with gusts as high as 25 mph. Chance of precipitation is 90%. Little or no snow accumulation expected.");
        mWeatherDetailedList.add("Rain, mainly before 10pm, then a chance of showers after 1am. Snow level 1000 feet. Low around 33. South wind 6 to 9 mph. Chance of precipitation is 90%");
        mWeatherDetailedList.add("Showers likely, mainly after 4pm. Snow level 1000 feet rising to 1500 feet in the afternoon. Mostly cloudy, with a high near 41.  Chance of precipitation is 60%. New precipitation amounts of less than a tenth of an inch possible");
        mWeatherDetailedList.add("Showers likely, mainly before 10pm. Mostly cloudy, with a low around 37. Southeast wind 3 to 5 mph. Chance of precipitation is 60%. New precipitation amounts of less than a tenth of an inch possible. ");
        mWeatherDetailedList.add("A 30 percent chance of showers. Mostly cloudy, with a high near 46. Calm wind becoming south around 6 mph in the afternoon.");
        mWeatherDetailedList.add("A 20 percent chance of showers before 4am. Snow level 3000 feet lowering to 2500 feet. Mostly cloudy, with a low around 33.");
        mWeatherDetailedList.add("A 20 percent chance of showers after 10am. Snow level 1700 feet. Mostly cloudy, with a high near 46.");
        mWeatherDetailedList.add("Rain likely. Snow level 2400 feet. Cloudy, with a low around 37.");
        mWeatherDetailedList.add("Rain likely. Snow level 2800 feet. Mostly cloudy, with a high near 47.");
        mWeatherDetailedList.add("Rain likely. Cloudy, with a low around 39.");
        mWeatherDetailedList.add("Rain. Cloudy, with a high near 48.");
        mWeatherDetailedList.add("Rain. Cloudy, with a low around 39.");
        mWeatherDetailedList.add("Showers. Mostly cloudy, with a high near 46.");
        mWeatherDetailedList.add("Rain. Cloudy, with a high near 48.");
        mWeatherDetailedList.add("Rain. Cloudy, with a low around 39.");
    }



    @Override
    public void onWeatherChanged(int i) {
        if(mToast != null) {
            mToast.cancel();
        }

        String toastText =mWeatherDetailedList.get(i);
        mToast = Toast.makeText(this, toastText, Toast.LENGTH_LONG);
        mToast.show();
    }
}