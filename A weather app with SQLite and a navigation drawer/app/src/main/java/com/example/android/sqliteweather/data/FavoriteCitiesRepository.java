package com.example.android.sqliteweather.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FavoriteCitiesRepository {
    private FavoriteCitiesDao dao;

    // Application application is for accessing database itself
    public FavoriteCitiesRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.dao = db.favoriteCitiesDao();
    }


    public void insertFavoriteCity(ForecastCity city) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(city);
            }
        });
    }

    public void deleteFavoriteCity(ForecastCity city) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(city);
            }
        });
    }

    public LiveData<List<ForecastCity>> getAllFavoriteCities() {
        return this.dao.getAllCities();
    }

}
