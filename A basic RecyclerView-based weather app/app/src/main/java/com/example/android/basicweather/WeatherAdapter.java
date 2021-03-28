package com.example.android.basicweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private ArrayList<String> mWeatherList;
    private ArrayList<String> mdate;
    private ArrayList<String> mtemplow;
    private ArrayList<String> mtemphigh;
    private ArrayList<String> mprecip;

    private OnWeatherChangeListener mOnWeatherInfoChangeListener;

    public interface OnWeatherChangeListener {
        /*********************************/
        void onWeatherChanged(int i);
        /*********************************/
    }

    public WeatherAdapter(OnWeatherChangeListener infoChangeListener) {
        mWeatherList = new ArrayList<>();
        mdate = new ArrayList<>();
        mtemplow = new ArrayList<>();
        mtemphigh = new ArrayList<>();
        mprecip = new ArrayList<>();
        mOnWeatherInfoChangeListener = infoChangeListener;
    }


    public void addDummyWeather(String dummyWeather,String date,String temlow,String temhi,String p) {
        mWeatherList.add(0, dummyWeather);
        //notifyItemInserted(0);
        mdate.add(0, date);
        //notifyItemInserted(0);
        mtemplow.add(0, temlow);
        mtemphigh.add(0, temhi);
        mprecip.add(0, p);
    }

    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.weather_item, parent, false);
        return new WeatherViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.WeatherViewHolder holder, int position) {
        String weather = this.mWeatherList.get(position);
        holder.bindtext(weather);

        String date = this.mdate.get(position);
        holder.binddate(date);

        String templow = this.mtemplow.get(position);
        holder.bindlow(templow);
        String temphi = this.mtemphigh.get(position);
        holder.bindhi(temphi);
        String pp = this.mprecip.get(position);
        holder.bindpp(pp);
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {
        private TextView mWeatherTV;
        private TextView mDateTV;
        private TextView mTmpLTV;
        private TextView mTmpHTV;
        private TextView mPPTV;
        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mWeatherTV = itemView.findViewById(R.id.tv_weather_text);
            this.mDateTV = itemView.findViewById(R.id.tv_weather_date);
            this.mTmpLTV = itemView.findViewById(R.id.tv_weather_templ);
            this.mTmpHTV = itemView.findViewById(R.id.tv_weather_temph);
            this.mPPTV = itemView.findViewById(R.id.tv_weather_precip);

            mWeatherTV.setOnClickListener(new CompoundButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    mOnWeatherInfoChangeListener.onWeatherChanged(index);
                }
            });

        }

        void bindtext(String weatherText) {
            this.mWeatherTV.setText(weatherText);
        }
        void binddate(String datetext) {
            this.mDateTV.setText(datetext);
        }
        void bindlow(String tmplt) {
            this.mTmpLTV.setText(tmplt);
        }
        void bindhi(String tmpht) {
            this.mTmpHTV.setText(tmpht);
        }
        void bindpp(String pp) {
            this.mPPTV.setText(pp);
        }
    }

}





