package com.example.thinkpaduser.loverunning.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thinkpaduser.loverunning.R;
import com.example.thinkpaduser.loverunning.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    @BindView(R.id.fragment_weather_tv_date)
    TextView fragmentWeatherTvDate;
    @BindView(R.id.fragment_weather_tv_time)
    TextView fragmentWeatherTvTime;
    private Unbinder unbinder;

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
