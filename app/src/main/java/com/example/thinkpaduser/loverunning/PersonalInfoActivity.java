package com.example.thinkpaduser.loverunning;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalInfoActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener,
        NumberPicker.OnScrollListener,NumberPicker.Formatter {
    // 个人资料界面
    @BindView(R.id.activity_personal_info_np_sex)
    NumberPicker mSexPicker;
    @BindView(R.id.activity_personal_info_np_height)
    NumberPicker mHeightPicker;
    @BindView(R.id.activity_personal_info_np_weight)
    NumberPicker mWeightPicker;
    @BindView(R.id.activity_personal_info_but_save)
    Button mButton;
    private final static String LOG_TAG = "PersonalInfoActivity";
    private int mSex = 0,mHeight = 170,mWeight = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        // 数字选择是可以滑动，所以需要定义一个OnValueChangeListener事件，OnScrollListener滑动事件，Formatter事件:
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalInfoActivity.this,"已保存",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        mHeightPicker.setFormatter(this);
        mHeightPicker.setOnValueChangedListener(this);
        mHeightPicker.setOnScrollListener(this);
        mHeightPicker.setMaxValue(210);
        mHeightPicker.setMinValue(140);
        SharedPreferences sp = getSharedPreferences("height",Context.MODE_PRIVATE);
        Log.v(LOG_TAG,"------"+ sp.getString("height",""));
        if (sp.getString("height","").isEmpty()){
            Log.v(LOG_TAG,"如果height记录为空——————————————》");
            mHeightPicker.setValue(170);
        }else{
            Log.v(LOG_TAG,"Integer.parseInt(sp.getString(\"height\",\"\"))"+Integer.parseInt(sp.getString("height","")));
            int height = Integer.parseInt(sp.getString("height",""));
            Log.v(LOG_TAG,"height---------->" + height);
            mHeightPicker.setValue(height);
        }

        mWeightPicker.setFormatter(this);
        mWeightPicker.setOnValueChangedListener(this);
        mWeightPicker.setOnScrollListener(this);
        mWeightPicker.setMaxValue(100);
        mWeightPicker.setMinValue(40);
        SharedPreferences sp2 = getSharedPreferences("weight",Context.MODE_PRIVATE);
        Log.v(LOG_TAG,"------"+ sp2.getString("weight",""));
        if (sp2.getString("weight","").isEmpty()){
            mWeightPicker.setValue(60);
        }else{
            int weight = Integer.parseInt(sp2.getString("weight",""));
            Log.v(LOG_TAG,"weight-------------------->" + weight);
            mWeightPicker.setValue(weight);
        }

        String[] sex = {"男","女"};
        mSexPicker.setFormatter(this);
        mSexPicker.setOnValueChangedListener(this);
        mSexPicker.setOnScrollListener(this);
        mSexPicker.setDisplayedValues(sex);
        mSexPicker.setMinValue(0);
        mSexPicker.setMaxValue(sex.length - 1);

        SharedPreferences sp3 = getSharedPreferences("sex",Context.MODE_PRIVATE);
        Log.v(LOG_TAG,"------"+ sp3.getString("sex",""));
        if (sp3.getString("sex","").isEmpty()){
            mSexPicker.setValue(0);
        }else{
            mSexPicker.setValue(Integer.parseInt(sp3.getString("sex","")));
        }
//        Log.v(LOG_TAG,"Integer.parseInt(sp3.getString(\"sex\",\"\"))"+Integer.parseInt(sp3.getString("sex","")));
//        mSexPicker.setValue(Integer.parseInt(sp3.getString("sex","")));
//        mSexPicker.setValue(1);
//        mSexPicker.setValue(0);
    }

    // Formatter事件
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 210) {
            tmpStr = "" + tmpStr;
        }
        return tmpStr;
    }

    // OnScrollListener滑动事件
    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        switch (scrollState) {
            case NumberPicker.OnScrollListener.SCROLL_STATE_FLING: // 后续滑动
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_IDLE: // 不滑动
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: // 滑动中
                break;
        }
    }

    // OnValueChangeListener事件
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.v(LOG_TAG,"picker======>" +picker+"oldVal--------->" + oldVal + "newVal--------->" + newVal);
        if (picker == mSexPicker){
            Log.v(LOG_TAG,"性别改变");
            mSex = newVal;
            SharedPreferences sp = getSharedPreferences("sex", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("sex",mSex+"");
            editor.commit();

        }else if (picker == mHeightPicker){
            Log.v(LOG_TAG,"身高改变");
            mHeight = newVal;
            SharedPreferences sp = getSharedPreferences("height", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("height",mHeight+"");
            editor.commit();

        }else if (picker == mWeightPicker){
            Log.v(LOG_TAG,"体重改变");

            mWeight = newVal;
            SharedPreferences sp = getSharedPreferences("weight", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("weight",mWeight+"");
            editor.commit();
        }

    }

}

