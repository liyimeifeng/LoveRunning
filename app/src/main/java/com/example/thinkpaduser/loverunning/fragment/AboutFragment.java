package com.example.thinkpaduser.loverunning.fragment;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thinkpaduser.loverunning.AboutAppActivity;
import com.example.thinkpaduser.loverunning.FeedBackActivity;
import com.example.thinkpaduser.loverunning.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    @BindView(R.id.fragment_about_app)
    TextView fragmentAboutApp;
    @BindView(R.id.fragment_about_company)
    TextView fragmentAboutCompany;
    @BindView(R.id.fragment_about_advice)
    TextView fragmentAboutAdvice;
    @BindView(R.id.fragment_about_weibo)
    TextView fragmentAboutWeibo;
    @BindView(R.id.fragment_about_versions)
    TextView fragmentAboutVersions;
    private Unbinder unbinder;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
         unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        fragmentAboutApp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        fragmentAboutAdvice.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        fragmentAboutCompany.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        fragmentAboutVersions.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        fragmentAboutWeibo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        fragmentAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutAppActivity.class);
                startActivity(intent);
            }
        });
        fragmentAboutAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
