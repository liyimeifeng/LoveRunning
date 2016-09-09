package com.example.thinkpaduser.loverunning.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thinkpaduser.loverunning.DatabassHelper;
import com.example.thinkpaduser.loverunning.R;
import com.example.thinkpaduser.loverunning.adapter.HistoryAdapter;
import com.example.thinkpaduser.loverunning.myclass.RunnigRecord;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HistoryFragment extends Fragment {
    private final static String LOG_TAG = "HistoryFragment";
    @BindView(R.id.fragment_history_tv)
    TextView mTextView;
    private HistoryAdapter mAdapter;
    @BindView(R.id.fragment_history)
    RecyclerView mRecyclerView;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new HistoryAdapter();
        mRecyclerView.setAdapter(mAdapter);
//        ArrayList<RunnigRecord> runnigRecords = new ArrayList<>();
//        for (int i = 0;i <5;i++){
//           RunnigRecord rn = new RunnigRecord();
//            rn.setDate("20210");
//            runnigRecords.add(rn);
//        }
//        mAdapter.addAll(runnigRecords);
//        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onActivityCreated走没有走？？？？");
        super.onActivityCreated(savedInstanceState);
        DatabassHelper helper = new DatabassHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();//查询方式，以只读的方式的打开
        String[] colums = {"_id", "start_time", "total_time", "distance", "step", "date"};
        Cursor cursor = db.query("running_record",
                colums,//需要查询的数据列的名称的数组
                null,//where字句
                null,//where字句的参数
                null,//group by字句
                null,//having 字句
                "start_time ASC");//排序,DESC表示降序排列，ASC升序排列
        Log.v(LOG_TAG, "cursor--------->" + cursor);
        //遍历数组
        ArrayList<RunnigRecord> runnigRecords = new ArrayList<>();
        while (cursor.moveToNext()) {
            //获取数据
            RunnigRecord rn = new RunnigRecord();
            rn.setId(cursor.getInt(0));
            Log.v(LOG_TAG, "id--------->>>>>>" + cursor.getInt(0));
            rn.setStartTime(cursor.getString(1));
            Log.v(LOG_TAG, "开始时间--------->>>>>>" + cursor.getString(1));
            rn.setTotaltime(cursor.getLong(2));
            Log.v(LOG_TAG, "时间--------->>>>>>" + cursor.getString(2));
            rn.setDistance(cursor.getFloat(3));
            rn.setStep(cursor.getInt(4));
            rn.setDate(cursor.getString(5));
            runnigRecords.add(rn);
        }
        db.close();//数据库操作完毕，一定要关闭数据库
        Log.v(LOG_TAG, "记录--------->>>>>>" + runnigRecords);
        if (runnigRecords.isEmpty()) {
            mTextView.setText("记录为空");
        }
        mAdapter.addAll(runnigRecords);
    }
}
