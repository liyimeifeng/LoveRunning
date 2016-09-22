package com.example.thinkpaduser.loverunning.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thinkpaduser.loverunning.R;
import com.example.thinkpaduser.loverunning.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    @BindView(R.id.fragment_weather_tv_date)
    TextView fragmentWeatherTvDate;
    @BindView(R.id.fragment_weather_tv_time)
    TextView fragmentWeatherTvTime;
    @BindView(R.id.fragment_weather_tv_weather_icon)
    ImageView mWeatherIcon;
    @BindView(R.id.fragment_weather_tv_temperature)
    TextView mTemperatureView;
    @BindView(R.id.fragment_weather_tv_weather)
    TextView mWeatherView;
    @BindView(R.id.fragment_weather_tv_temperature_range)
    TextView mTemperatureRangeView;
    @BindView(R.id.fragment_weather_tv_UV)
    TextView mUVView;
    @BindView(R.id.fragment_weather_tv_sport_status)
    TextView mSportStatusView;
    @BindView(R.id.fragment_weather_tv_advice)
    TextView mAdviceView;
    private Unbinder unbinder;
    private final static String LOG_TAG = "WeatherFragment";

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(view);
        fragmentWeatherTvDate.setText(TimeUtil.getCurrentMonthAndDay());
        fragmentWeatherTvTime.setText(TimeUtil.getTime(System.currentTimeMillis()));
        Bundle bundle = getArguments();
        mTemperatureView.setText(bundle.get("temperature") + "℃");
        mTemperatureRangeView.setText(bundle.get("temperature_range") + "");
        mSportStatusView.setText(bundle.get("sportStatus")+"");
        mWeatherView.setText("" + bundle.get("weather"));
        mUVView.setText(bundle.get("UV")+"");
        mAdviceView.setText(bundle.get("advice")+ " ");
        String mWeather = (String)bundle.get("weather");
        if (mWeather.contains("雨")){
            mWeatherIcon.setImageResource(R.drawable.rain);
        }else if (mWeather.contains("晴")){
            mWeatherIcon.setImageResource(R.drawable.sun);
        }else {
            mWeatherIcon.setImageResource(R.drawable.cloudy);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
