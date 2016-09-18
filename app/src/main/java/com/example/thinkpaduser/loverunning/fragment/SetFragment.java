package com.example.thinkpaduser.loverunning.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
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
    private Switch notifySwitch,checkNewSwitch;
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
        notifySwitch = (Switch)view.findViewById(R.id.fragment_set_switch_notification);
        checkNewSwitch = (Switch)view.findViewById(R.id.fragment_set_switch_checkNew);

        notifySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SharedPreferences sp  = getContext().getSharedPreferences("notifySwitchStatus", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("notifySwitchStatus","" +notifySwitch.isChecked());
                    editor.commit();
                }else{
                    SharedPreferences sp  = getContext().getSharedPreferences("notifySwitchStatus", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("notifySwitchStatus","" +notifySwitch.isChecked());
                    editor.commit();
                }
            }
        });

        checkNewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SharedPreferences sp  = getContext().getSharedPreferences("checkNewSwitchStatus", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("checkNewSwitchStatus","" +checkNewSwitch.isChecked());
                    editor.commit();
                }else{
                    SharedPreferences sp  = getContext().getSharedPreferences("checkNewSwitchStatus", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("checkNewSwitchStatus","" +checkNewSwitch.isChecked());
                    editor.commit();
                }
            }
        });

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
    public void onStart() {
        super.onStart();
        SharedPreferences sp = getContext().getSharedPreferences("notifySwitchStatus",Context.MODE_PRIVATE);
        if (sp.getString("notifySwitchStatus","").equals("true")){
            notifySwitch.setChecked(true);
        }else{
            notifySwitch.setChecked(false);
        }

        SharedPreferences sp2 = getContext().getSharedPreferences("checkNewSwitchStatus",Context.MODE_PRIVATE);
        if (sp2.getString("checkNewSwitchStatus","").equals("true")){
            checkNewSwitch.setChecked(true);
        }else {
            checkNewSwitch.setChecked(false);
        }
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
