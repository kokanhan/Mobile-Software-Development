package com.example.android.sqliteweather.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/* a DAO contains methods for fetching entities from the database and for saving changes to entities
  (including creating or deleting entities) back to the database.
  These methods are mapped directly to corresponding SQLite queries.
 */
@Dao
public interface FavoriteCitiesDao {
    //Add one method to this interface for each different kind of query for database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ForecastCity city);

    @Delete
    void delete(ForecastCity city);

    @Query("SELECT * FROM favoriteCities ORDER by timestamp DESC")
    LiveData<List<ForecastCity>> getAllCities();
}
