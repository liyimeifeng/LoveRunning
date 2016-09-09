package com.example.thinkpaduser.loverunning.fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thinkpaduser.loverunning.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaticsFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    public StaticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTabLayout = (TabLayout) view.findViewById(R.id.fragment_statics_tabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_statics_viewpager);
        Adapter adapter = new Adapter(getChildFragmentManager());//避免出现加载不出来的情况！！！！！！！！
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public class Adapter extends FragmentPagerAdapter{

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            StaticsDataFragment fragment = new StaticsDataFragment();
            Bundle bundle = new Bundle();
            //传入一个type用于区分年月日周的数据
           bundle.putInt("类型",position);
           fragment.setArguments(bundle);
            return fragment; //点击哪个就返回哪个fragment
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] array = getResources().getStringArray(R.array.statics_title);//数组命名写法
            return array[position];
        }
    }
}
