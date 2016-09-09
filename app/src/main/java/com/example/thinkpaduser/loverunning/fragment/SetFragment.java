package com.example.thinkpaduser.loverunning.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thinkpaduser.loverunning.PersonalInfoActivity;
import com.example.thinkpaduser.loverunning.R;
import com.example.thinkpaduser.loverunning.RemindRunningActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends Fragment {
    @BindView(R.id.fragment_set_tv_personal_info)
    TextView fragmentSetTvPersonalInfo;
    @BindView(R.id.fragment_set_tv_running_remind)
    TextView fragmentSetTvRunningRemind;
    @BindView(R.id.fragment_set_tv_clear)
    TextView fragmentSetTvClear;
    @BindView(R.id.fragment_set_tv_location_statics)
    TextView fragmentSetTvLocationStatics;
    private Unbinder unbinder;

    public SetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set, container, false);
        unbinder = ButterKnife.bind(this, view);
//        Toolbar toolbar = (Toolbar)view.findViewById(R.id.fragment_set_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentSetTvRunningRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RemindRunningActivity.class);
                startActivity(intent);
            }
        });
//        AppCompatActivity.getDelegate().setSupportActionBar(toolbar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fragment_set_tv_personal_info)
    public void onClick() {
        Intent intent = new Intent(getContext(),PersonalInfoActivity.class);
        startActivity(intent);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();;
//        inflater.inflate(R.menu.fragment_set_menu,menu);
//    }
}
