package com.example.thinkpaduser.loverunning.fragment;


import android.app.ActivityManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.thinkpaduser.loverunning.DatabassHelper;
import com.example.thinkpaduser.loverunning.R;
import com.example.thinkpaduser.loverunning.TimeUtil;
import com.example.thinkpaduser.loverunning.myclass.RunnigRecord;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaticsDataFragment extends Fragment {
    private int type;
    private LineChart mLineChartView;
    private TextView mStepView,mDistanceView,mCalorieView,mTimeView,mTodayView,mMonthView,mHourOrMinuteView;
    private final static String LOG_TAG = "StaticsDataFragment";
    public StaticsDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statics_data, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLineChartView = (LineChart)view.findViewById(R.id.fragment_statics_data_linechart);
        mStepView = (TextView)view.findViewById(R.id.fragment_statics_data_tv_step);
        mCalorieView = (TextView)view.findViewById(R.id.fragment_statics_data_tv_calorie);
        mDistanceView = (TextView)view.findViewById(R.id.fragment_statics_data_tv_distance);
        mTimeView = (TextView)view.findViewById(R.id.fragment_statics_data_tv_time);
        mTodayView = (TextView)view.findViewById(R.id.fragment_statics_data_tv_today);
        mMonthView = (TextView)view.findViewById(R.id.fragment_statics_data_tv_monthAndDay);
        mHourOrMinuteView = (TextView)view.findViewById(R.id.fragment_statics_tv_hour_or_minute);
        seekSQLite("2016-09-01","2016-09-02");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        //数据量大的情况查找就会很慢，就形成了耗时任务，就应该放在子线程里执行,所以我们就放在了下面的异步任务里
//        DatabassHelper helper = new DatabassHelper(getActivity());
//        SQLiteDatabase db = helper.getReadableDatabase();
        LoadDataTask loadDataTask = new LoadDataTask();
        loadDataTask.execute();//第一个参数就是异步任务里面需要的第一个参数的类型，这里是字符串
        Log.v(LOG_TAG,"异步通信--------》");
//        Bundle bundle = getArguments();
//        ArrayList<RunnigRecord> steps = new ArrayList<>();
//        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<RunnigRecord> runnigRecords = new ArrayList<>();
        RunnigRecord rn = new RunnigRecord();
        rn.setStep(11);
        runnigRecords.add(rn);
        rn = new RunnigRecord();
        rn.setStep(12);
        runnigRecords.add(rn);
        rn = new RunnigRecord();
        rn.setStep(13);
        runnigRecords.add(rn);
        rn = new RunnigRecord();
        rn.setStep(14);
        runnigRecords.add(rn);
        rn = new RunnigRecord();
        rn.setStep(15);
        runnigRecords.add(rn);
        rn = new RunnigRecord();
        rn.setStep(16);
        runnigRecords.add(rn);
        rn = new RunnigRecord();
        rn.setStep(17);
        runnigRecords.add(rn);
        rn = new RunnigRecord();
        rn.setStep(18);
        runnigRecords.add(rn);

        Log.v(LOG_TAG,"runnigRecords.size()" + runnigRecords.size());
        ArrayList<Entry> entries = new ArrayList<>();
        int n = runnigRecords.size()/runnigRecords.size();//每隔上八个点描一次，可随意，n即表示描几次的意思
        for (int i =1;i < runnigRecords.size();i ++){
            int start = (i - 1) * n;
            int end = i * n;
            if (end >= runnigRecords.size()){
                end = runnigRecords.size()-1;
            }
            entries.add(new Entry(i*n,runnigRecords.get(i).getStep()));
        }
        LineDataSet dataSet = new LineDataSet(entries,"运动");
        LineData lineData = new LineData(dataSet);
        mLineChartView.getAxisLeft().setDrawGridLines(false);//隐藏右边坐标轴横网格线
        mLineChartView.getAxisRight().setDrawGridLines(false);//隐藏X轴竖网格线
        //取消legend
        //mLineChart.getLegend().setEnabled(false);
        mLineChartView.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        mLineChartView.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // 让x轴在下面
        mLineChartView.getXAxis().setDrawGridLines(false);
        mLineChartView.getXAxis().setGridColor(Color.BLUE);
        mLineChartView.setData(lineData);
        mLineChartView.invalidate();
    }

    //异步任务，异步任务，异步任务！！！！！！等同于子线程的一种解决方案
    private class LoadDataTask extends AsyncTask<String,Void,List<RunnigRecord>>{
        //需要的东西依次是，参数的类型，
        // 进度的类型(onProgressUpdate方法的类型)，
        // 最终返回数据的类型(也就是doInBackground里方法的返回值类型)
        //重点：除了doInBackground()方法以外其他所有方法都是在主线程执行
        @Override
        protected void onPreExecute() {
            //在doInBackground方法之前执行
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //这里就是进度
            super.onProgressUpdate(values);
        }

        @Override
        protected List<RunnigRecord> doInBackground(String... voids) {
            //写在该方法里的代码自动在子线程里执行

            //获得当前日期
            Calendar calendar = Calendar.getInstance();
            Bundle bundle = getArguments();
            type = bundle.getInt("类型");

            Log.v(LOG_TAG, "type=========================>" + type);
            if (type == 0) {                     //今日
                DatabassHelper helper = new DatabassHelper(getActivity());
                SQLiteDatabase db = helper.getReadableDatabase();
                String[] colums = {"_id", "start_time", "total_time", "distance", "step", "date"};
                String[] params = new String[1];
                params[0] = TimeUtil.getCurrentDate();
                //如果在doInback里更新UI就一定要写该方法，并在该方法里写
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                //更新UI的操作
//                    }
//                });
                //执行查询
                Cursor cursor = db.query("running_record",//需要查询的表名
                        colums,//需要查询的数组列的名称
                        "date>=?",//where子句，问号的意思是这里的内容用下面的recordID来代替，也就是params，实现自动导入id
                        params,//where子句的参数,参数只有一个就就这样写
                        null,//gruop by子句
                        null,//having 子句
                        "start_time DESC");//排序方式，正序
                ArrayList<RunnigRecord> records = new ArrayList<>();
                while (cursor.moveToNext()) {
                    RunnigRecord rec = new RunnigRecord();
                    rec.setId(cursor.getInt(0));
                    rec.setStartTime(cursor.getString(1));
                    rec.setTotaltime(cursor.getLong(2));
                    rec.setDistance(cursor.getFloat(3));
                    rec.setStep(cursor.getInt(4));
                    rec.setDate(cursor.getString(5));
                    records.add(rec);
                }
                db.close();
                Log.v(LOG_TAG,"今天records——————————》" + records);
                return records;
            } else if (type == 1) {                           //本周
                calendar.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设定是星期一，默认是星期天
                calendar.add(Calendar.DATE, -1 * 7);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                String firstDay = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                Calendar cal = Calendar.getInstance();
                cal.setFirstDayOfWeek(Calendar.MONDAY);
                cal.add(Calendar.DATE, -1 * 7);
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                String lastDay = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
               return seekSQLite(firstDay,lastDay);
            } else if (type == 2) {
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                String firstDay = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.DATE, -1);
                String lastDay = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
               return seekSQLite(firstDay, lastDay);
            } else {                                      //最后就是type等于3的情况
                DatabassHelper helper = new DatabassHelper(getActivity());
                SQLiteDatabase db = helper.getReadableDatabase();
                String[] colums = {"_id", "start_time", "total_time", "distance", "step", "date"};
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                String params[] = new String[1];
                params[0] = year + "-";
                //执行查询
                Cursor cursor = db.query("running_record",//需要查询的表名
                        colums,//需要查询的数组列的名称
                        "date>=?",//where子句，问号的意思是这里的内容用下面的recordID来代替，也就是params，实现自动导入id
                        params,//where子句的参数
                        null,//gruop by子句
                        null,//having 子句
                        "start_time DESC");//排序方式，正序
                ArrayList<RunnigRecord> records = new ArrayList<>();
                while (cursor.moveToNext()) {
                    RunnigRecord rec = new RunnigRecord();
                    rec.setId(cursor.getInt(0));
                    rec.setStartTime(cursor.getString(1));
                    rec.setTotaltime(cursor.getLong(2));
                    rec.setDistance(cursor.getFloat(3));
                    rec.setStep(cursor.getInt(4));
                    rec.setDate(cursor.getString(5));
                    records.add(rec);
                }
                db.close();
                return records;
            }
        }

        @Override
        protected void onPostExecute(List<RunnigRecord> runnigRecords) {
            //在doInBackground方法之后执行,这个是主线程，可以在这里更新UI操作
            //接受doInBackground方法返回的数据类型，这里接受的runningRecords就是doInback最后返回的records
            super.onPostExecute(runnigRecords);
            Log.v(LOG_TAG,"onPostExecute--------------->" + type);
            if (type == 0){
                mTodayView.setText("今日:");
                mMonthView.setText(TimeUtil.getCurrentMonthAndDay());
                int sumStep = 0;
                float sumDis = 0;
                long sumTime = 0;
                for (RunnigRecord run :runnigRecords){
                    int steps = run.getStep();
                    float distance = run.getDistance();
                    long time = run.getTotaltime();
                    sumStep += steps;
                    sumDis += distance;
                    sumTime += time;
                }
                mStepView.setText(sumStep+"");//今日总步数
                mDistanceView.setText("" + sumDis);//今日总距离
                mTimeView.setText("" + sumTime/1000/60);
                mHourOrMinuteView.setText("时间\n(分钟)");
            }else if (type == 1){
                mTodayView.setText("上周:");
                mMonthView.setText("" +getFirstDayAndLastdayOfLastWeek());
                int sumStep = 0;
                float sumDis = 0;
                long sumTime = 0;
                for (RunnigRecord run:runnigRecords){
                    int steps = run.getStep();
                    float distance = run.getDistance();
                    long time = run.getTotaltime();
                    sumStep += steps;
                    sumDis += distance;
                    sumTime += time;
                }
                mStepView.setText(sumStep + "");
                mDistanceView.setText("" + sumDis);
                float time = sumTime/1000/60/60f;
                mTimeView.setText("" + (float)(Math.round(time*100))/100);
            }else if (type == 2){
                mTodayView.setText("上月:");
                mMonthView.setText("" +getFirstdayAndLastdayOfLastMonth());
                int sumStep = 0;
                float sumDis = 0;
                long sumTime = 0;
                for (RunnigRecord run:runnigRecords){
                    int steps = run.getStep();
                    float distance = run.getDistance();
                    long time = run.getTotaltime();
                    sumStep += steps;
                    sumDis += distance;
                    sumTime += time;
                }
                mDistanceView.setText("" + sumDis);
                float time = sumTime/1000/60/60f;
                mTimeView.setText("" + (float)(Math.round(time*100))/100);
                mStepView.setText("" + sumStep);
            }else if (type == 3){
                mTodayView.setText("今年:");
                mMonthView.setText(Calendar.getInstance().get(Calendar.YEAR) + "年");
                int sumStep = 0;
                float sumDis = 0;
                long sumTime = 0;
                for (RunnigRecord run:runnigRecords){
                    int steps = run.getStep();
                    float distance = run.getDistance();
                    long time = run.getTotaltime();
                    sumStep += steps;
                    sumDis += distance;
                    sumTime += time;
                }
                mDistanceView.setText("" + sumDis);
                float time = sumTime/1000/60/60f;
                mTimeView.setText("" + (float)(Math.round(time*100))/100);
                mStepView.setText("" + sumStep);
            }
        }
    }

    private String getFirstDayAndLastdayOfLastWeek(){//获取上周第一天和最后一天
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设定是星期一，默认是星期天
        calendar.add(Calendar.DATE,-1*7);
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        String firstDay = new SimpleDateFormat("MM月dd日").format(calendar.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.add(Calendar.DATE,-1*7);
        cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        String lastDay = new SimpleDateFormat("MM月dd日").format(cal.getTime());
        return firstDay+"—" +lastDay;
    }

    private String getFirstdayAndLastdayOfLastMonth(){//获取上月第一天和最后一天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        String firstDay = new SimpleDateFormat("MM月dd日").format(calendar.getTime());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.add(Calendar.DATE,-1);
        String lastDay = new SimpleDateFormat("MM月dd日").format(cal.getTime());
        return firstDay+"—"+lastDay;
    }

    public ArrayList<RunnigRecord> seekSQLite (String firstDay,String lastDay){//查询SQLite
        DatabassHelper helper = new DatabassHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] colums = { "_id","start_time","total_time","distance","step","date"};
        String params[] = new String[2];
        params[0] = firstDay;
        params[1] = lastDay;
        //执行查询
        Cursor cursor = db.query("running_record",//需要查询的表名
                colums,//需要查询的数组列的名称
                //where子句，问号的意思是这里的内容用下面的recordID来代替，也就是params，实现自动导入id,这里是查找一个范围的写法
                "date>=? and date<=?",
                new String[]{params[0],params[1]},//where子句的参数,这里也可以写成parmas
                null,//gruop by子句
                null,//having 子句
                "start_time DESC");//排序方式，正序
        ArrayList<RunnigRecord> records = new ArrayList<>();
        while (cursor.moveToNext()){
            RunnigRecord rec = new RunnigRecord();
            rec.setId(cursor.getInt(0));
            rec.setStartTime(cursor.getString(1));
            rec.setTotaltime(cursor.getLong(2));
            rec.setDistance(cursor.getFloat(3));
            rec.setStep(cursor.getInt(4));
            rec.setDate(cursor.getString(5));
            records.add(rec);
        }
        db.close();
        Log.v(LOG_TAG,"records---------->" + records);
        return records;
    }


}
