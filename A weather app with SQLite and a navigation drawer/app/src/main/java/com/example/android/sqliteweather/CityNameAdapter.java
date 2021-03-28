package com.example.android.sqliteweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sqliteweather.data.FiveDayForecast;
import com.example.android.sqliteweather.data.ForecastCity;

import java.util.ArrayList;
import java.util.List;


/*
    RecyclerView.Adapter – this is an abstract inner class of RecyclerView that is used to manage
    the data underlying the list, to create and manage ViewHolder objects, and to bind data to those
    ViewHolder objects as the user scrolls through the list.
    We must extend this class ourselves to model the underlying data as is appropriate for our app.
*/
public class CityNameAdapter extends RecyclerView.Adapter<CityNameAdapter.CityNameViewHolder> {
    //complete the declaration of our adapter class by adding our view holder as the type parameter
    private static final String TAG = MainActivity.class.getSimpleName();

    //Create a private member to hold data structure that will contain our city name strings
    private List<ForecastCity> locationItemsList;
    private OnLocationItemClickListener locationItemClickListener;
    private Context context;
    //private ArrayList<String> prefLocations;

    interface OnLocationItemClickListener {
        void onLocationItemClicked(ForecastCity forecastCity);
    }

    //public CityNameAdapter(Context nContext, OnLocationItemClickListener listener) {
   public CityNameAdapter(OnLocationItemClickListener listener) {
        //public CityNameAdapter() {
        //**********************************************
        //Initialize our city name data structure in a constructor for our adapter class
        //locationItemsList = new ArrayList<>();
        //**********************************************
       // context = nContext;
        locationItemClickListener = listener;

    }

    // Add a public function to add a new city name into the adapter’s data structure
    public void updateLocationItems(List<ForecastCity> locationItemsList) {
        this.locationItemsList = locationItemsList;
        // calling notifyDataSetChanged() to let the adapter class know a new item was added at the beginning of the list.
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.locationItemsList != null) {
            return this.locationItemsList.size();
        } else {
            return 0;
        }
    }

    /*
     * The view holder is a class that represents the individual elements of our city name list.
     * In our current case, it has one major responsibility: setting its contents to the appropriate
     * city name string when the adapter binds data to it.
     *
     * onCreateViewHolder() – called when a new view holder is created
     * */
    @NonNull
    @Override
    public CityNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Note that the argument viewGroup parent here represents the larger collection of list elements represented by the RecyclerView.
        View itemView = inflater.inflate(R.layout.saved_cities_list_item, parent, false);
        return new CityNameViewHolder(itemView);
    }

    // onBindViewHolder() – called when a view holder is bound to new data
    @Override
    public void onBindViewHolder(@NonNull CityNameViewHolder holder, int position) {
        holder.bind(this.locationItemsList.get(position));
    }


    /*
        Since the view holder is so tightly coupled to the adapter, we will actually make the view holder an inner class within our adapter class.
        One part of the RecyclerView model
        RecyclerView.ViewHolder – this is an abstract inner class of RecyclerView that is used to display the individual objects in the list
    */

    class CityNameViewHolder extends RecyclerView.ViewHolder {

        /* Remember, each view holder instance will need to modify its text content to represent the appropriate
            city name content when it’s being bound to new data by the adapter.
            To accomplish this, we’ll need a reference to the view holder’s TextView.
         */
        private TextView locationNameTV;

        // Let’s add a private member to hold this reference and a constructor that grabs it using its ID:
        public CityNameViewHolder(@NonNull View itemView) {
            super(itemView);
            locationNameTV = itemView.findViewById(R.id.location_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationItemClickListener.onLocationItemClicked(
                            locationItemsList.get(getAdapterPosition())
                    );
//                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//                    SharedPreferences.Editor clickedCity = sharedPreferences.edit();
//                      https://developer.android.com/reference/android/content/SharedPreferences.Editor
//                    clickedCity.putString("location pref name", String.valueOf(locationItemsList.get(1)));
//                    clickedCity.commit();
//                    Log.d(TAG, "changing to location" + locationItemsList.get(1));
                }
            });
        }

        public void bind(ForecastCity cityData) {
            Log.d("TAG", cityData.getName());
            this.locationNameTV.setText(cityData.getName());
        }
    }
}





