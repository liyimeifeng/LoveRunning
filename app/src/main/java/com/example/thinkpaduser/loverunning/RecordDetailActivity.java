package com.example.thinkpaduser.loverunning;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;

import com.baidu.mapapi.model.LatLng;
import com.example.thinkpaduser.loverunning.fragment.HistoryFragment;
import com.example.thinkpaduser.loverunning.fragment.RecordDetailFragment;
import com.example.thinkpaduser.loverunning.fragment.RecordTrackFragment;
import com.example.thinkpaduser.loverunning.myclass.RunnigRecord;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordDetailActivity extends AppCompatActivity {
private final static String LOG_TAG = "RecordDetailActivity";

    @BindView(R.id.content_detail_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.content_detail_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.content_detail)
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //从数据库查询数据
       DatabassHelper helper = new DatabassHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        //接受historyAdapter适配器intent里面传过来的数据
        RunnigRecord runnigRecord = getIntent().getParcelableExtra("record");
        Log.v(LOG_TAG,"runnigRecord为空？？？？" + runnigRecord);

        int recordID = runnigRecord.getId();
//       int recordID = ("id",-1);
        //执行查询
        Cursor cursor = db.query("record_lnglat",
                new String[]{"lng","lat"},
                "record_id=?",//问号的意思是这里的内容用下面的recordID来代替，实现自动导入id
                new String[]{Integer.toString(recordID)},
                null,null,null);
       ArrayList<LatLng> latLngs = new ArrayList<>();
        while (cursor.moveToNext()){
            LatLng latLng = new LatLng(cursor.getDouble(0),cursor.getDouble(1));
            latLngs.add(latLng);
        }
        db.close();
        //将数据传入Fragment,采用的是bundle这个方法！！！！！！
        RecordDetailFragment detailFragment = new RecordDetailFragment();
        //创建一个Bundle对象
        Bundle bundle = new Bundle();
        //将数据放入bundle对象，该拿啥拿啥,绿色字一定要对
        bundle.putParcelableArrayList("latlng",latLngs);
        bundle.putParcelable("record",runnigRecord);//传进了RecordDetailfragment里面
        //调用fragment的setArugument();
        detailFragment.setArguments(bundle);//一定要用这个方法！！！！！！//一定要用这个方法！！！！！！
        //轨迹Fragment
        RecordTrackFragment trackFragment = new RecordTrackFragment();
//        HistoryFragment historyFragment = new HistoryFragment();
        trackFragment.setArguments(bundle);//一定要用这个方法！！！！！！
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(detailFragment);
        fragments.add(trackFragment);

        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragments));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragment) {
            super(fm);
            this.fragments = fragment;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return fragments.get(position);
            } else if (position == 1) {
                return fragments.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "运动详情";
            } else if (position == 1) {
                return "运动轨迹";
            }
            return super.getPageTitle(position);
        }
    }

}
