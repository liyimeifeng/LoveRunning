package com.example.thinkpaduser.loverunning.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.thinkpaduser.loverunning.R;
import com.example.thinkpaduser.loverunning.TimeUtil;
import com.example.thinkpaduser.loverunning.myclass.RunnigRecord;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecordDetailFragment extends Fragment {
    @BindView(R.id.fragment_record_detail_date)
    TextView mDateView;
    @BindView(R.id.fragment_record_detail_step)
    TextView mStepView;
    @BindView(R.id.fragment_record_detail_distance)
    TextView mDistanceView;
    @BindView(R.id.fragment_record_detail_time)
    TextView mTimeView;
    @BindView(R.id.fragment_record_detail_calorie)
    TextView mCalorieView;
    @BindView(R.id.fragment_record_detail_linechart)
    LineChart mLineChartView;
   private Unbinder unbinder;//采用ButterKnife方法的时候才需要写，要绑定，也要解绑
    private final static String LOG_TAG = "RecordDetailFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_detail, container, false);
        unbinder = ButterKnife.bind(this, view);//这就是绑定！！！
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLineChartView = (LineChart) view.findViewById(R.id.fragment_record_detail_linechart);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //OnViewCreated之后的方法
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();//接受从RecordDetailActivity里传过来的bundle对象
        //获取运动记录,
        RunnigRecord record = bundle.getParcelable("record");
        mDateView.setText(record.getDate());
        mCalorieView.setText("消耗卡路里" + "\n" + "123" + "大卡");
        mDistanceView.setText("总里程" + "\n" + record.getDistance() + "米");
        mStepView.setText( record.getStep() + "\n" +"步");
        mTimeView.setText("运动时间" + "\n" + TimeUtil.getRunTime( record.getTotaltime()));

        //接受从RecordDeteilActivity里bundle对象传过来的数据
        ArrayList<LatLng> latLngs = bundle.getParcelableArrayList("latlng");
        if (!latLngs.isEmpty()){
            Log.v(LOG_TAG,"latlng-------->" + latLngs);
            List<Entry> entries = new ArrayList<>();
//        ArrayList<Float> aveSpeed = new ArrayList<>();
            int n = latLngs.size()/8;//这句话的意思是每隔上八个点描一次,n表示描几次的意思
            //开始画图
            for(int i = 1; i <= 8; i ++ ){
                int start = (i - 1)*n;
                int end = i*n;
                Log.v(LOG_TAG,"end>>>>>>>>>>" + latLngs.size());
                if (end >= latLngs.size()){
                    Log.v(LOG_TAG,"latLngs.size()>>>>>>>>>>" + latLngs.size());
                    end = latLngs.size()-1;
                }
                entries.add(new Entry(i*n,(float)DistanceUtil.getDistance(latLngs.get(start),latLngs.get(end))));
            }
            LineDataSet dataSet = new LineDataSet(entries,"数据表");
            LineData lineData = new LineData(dataSet);
            mLineChartView.setData(lineData);
            mLineChartView.getAxisLeft().setDrawGridLines(false);//隐藏右边坐标轴横网格线
            mLineChartView.getAxisRight().setDrawGridLines(false);//隐藏X轴竖网格线
            //取消legend
            //mLineChart.getLegend().setEnabled(false);
            mLineChartView.setDescription("平均速度：25km/h");//实现图表中添加签名
            mLineChartView.setDescriptionPosition(600f,60f);//签名的位置
            mLineChartView.setDescriptionTextSize(16);//签名字体大小
            mLineChartView.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
            mLineChartView.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // 让x轴在下面
            mLineChartView.getXAxis().setDrawGridLines(false);
//        mLineChartView.setDrawBorders(true);//是否显示外围边框
            mLineChartView.getXAxis().setGridColor(Color.BLUE);
//        mLineChartView.getXAxis().setEnabled(true);
//        mLineChartView.getXAxis().setDrawGridLines(true);//是否显示x轴上的刻度竖线
            xAxis = mLineChartView.getXAxis();//得到x轴
            yAxis = mLineChartView.getAxisLeft();//得到y轴
//        xAxis.setDrawGridLines(true);//是否显示XY轴上的刻度竖线
//        xAxis.setDrawGridLines(true);
//        yAxis.setDrawGridLines(true);
//        yAxis.setDrawGridLines(true);
            yAxis.setStartAtZero(true);//y轴坐标是否从零开始
//        mLineChartView.setBackgroundColor(Color.GRAY);
            mLineChartView.invalidate();
        }

    }
    private XAxis xAxis;
    private YAxis yAxis;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();//这就是解绑
    }
}
















