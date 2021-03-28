package com.example.android.sqliteweather;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.sqliteweather.data.FavoriteCitiesRepository;
import com.example.android.sqliteweather.data.ForecastCity;

import java.util.List;

// Compare to ViewModel, AndroidViewModel is designed for application context
public class SavedCityViewHolder extends AndroidViewModel {
    private FavoriteCitiesRepository cityRepo;

    public SavedCityViewHolder(Application application) {
        super(application);
        this.cityRepo = new FavoriteCitiesRepository(application);
    }

    //add a method we can call to use our Repository to insert a new city name
    public void insertFavoriteCity(ForecastCity city) {
        this.cityRepo.insertFavoriteCity(city);
    }

    //add a method we can call to use our Repository to delete a city name
    public void deleteFavoriteCity(ForecastCity city) {
        this.cityRepo.deleteFavoriteCity(city);
    }

    public LiveData<List<ForecastCity>> getAllFavoriteCities() {
        return cityRepo.getAllFavoriteCities();
    }

}
