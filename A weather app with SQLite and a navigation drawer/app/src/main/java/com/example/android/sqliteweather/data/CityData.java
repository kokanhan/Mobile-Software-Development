package com.example.android.sqliteweather.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

//@Entity(tableName = "favoriteCities")
public class CityData implements Serializable {

//    @PrimaryKey
//    @NonNull
    public String cityName;
//
//    @NonNull
    public long timestamp;

    public CityData(String cityName, long timestamp){
        this.cityName = cityName;
        this.timestamp = timestamp;
    }


}