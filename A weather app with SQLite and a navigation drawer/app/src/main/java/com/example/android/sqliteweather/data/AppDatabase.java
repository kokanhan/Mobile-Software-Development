package com.example.android.sqliteweather.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*Next, though, we’ll create our Room database class.  Remember, this class will serve as the
  main access point for our database, and we’ll use it to get DAOs so we can make queries on
  the entities in the database.
 */

@Database(entities = {ForecastCity.class}, version = 1)
/*The version number should start at 1 and should be incremented every time a change is made to the
  database schema (e.g. adding a new column, adding a new entity, etc.).
*/
public abstract class AppDatabase extends RoomDatabase {
    // volatile keyword helps to  ensure atomic operations on this particular field when running in multiple threads
    // AppDatabase INSTANCE is a singleton instance, one object in one class
    private static volatile AppDatabase INSTANCE;

    // Threads pool we can use
    private static final int NUM_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_THREADS);

    public abstract FavoriteCitiesDao favoriteCitiesDao();

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            // Only one thread can enter the synchronized block at once
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    /* provide an application context, the database class, and the name of the
                       database (to use as an SQLite filename)
                     */
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "BNFavorite_cities.db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
