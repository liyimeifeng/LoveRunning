package com.example.thinkpaduser.loverunning.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.thinkpaduser.loverunning.R;
import com.example.thinkpaduser.loverunning.RecordDetailActivity;
import com.example.thinkpaduser.loverunning.TimeUtil;
import com.example.thinkpaduser.loverunning.fragment.HistoryFragment;
import com.example.thinkpaduser.loverunning.myclass.RunnigRecord;

import java.sql.Time;
import java.util.ArrayList;
import java.util.TreeMap;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ThinkPad User on 2016/8/16.
 */
public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String LOG_TAG = "HistoryAdapter";

    //历史记录数据
//    private ArrayList<RunnigRecord> records = new ArrayList<>();
    private TreeMap<String, ArrayList<RunnigRecord>> mData = new TreeMap<>();

    public void addAll(ArrayList<RunnigRecord> records) {
        for (RunnigRecord record : records) { //记录有很多条，然后一条一条来遍历
            String date = record.getDate();
            //把日期作为key，记录中的所有东西都是value值
            if (mData.containsKey(date)) {//containKey得到的是boolean值，表示包不包括这个key值，包括就是真
                ArrayList<RunnigRecord> values = mData.get(date);
                //如果包括这个值，就是同一个key，也就是说是同一天，然后直接往里面添加value值，也就是数据
                values.add(record);
                //然后value在添加到记录当中
            } else {//如果没有这个key值，也就是可能是第二天，天数要重新开始，他就是一个新的key值，value也就属于新的key值
                ArrayList<RunnigRecord> values = new ArrayList<>();
                values.add(record);//然后添加到记录当中
                mData.put(date, values);//date就是一个新的key值，在加入上面的value值全部添加到treeMap集合中
            }
        }
        notifyDataSetChanged();//通知显示
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.item_history_date, parent, false);
            return new DateViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_history_record, parent, false);
            return new RecordViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == 1) {
            DateViewHolder vh = (DateViewHolder) holder;
            int sum = 0;
            for (String key:mData.keySet()){
                if (position == sum){
                    vh.mDateView.setText(key);
                    Log.v(LOG_TAG,"key值也就是日期----------->" + key);
                    break;
                }
                sum++;
                sum += mData.get(key).size();
            }
        } else {
            final RecordViewHolder vh = (RecordViewHolder) holder;
            int sum = 0;
            boolean isFinish = false;
            for (String key : mData.keySet()){//这是什么循环遍历？？
                //mData.keySet()得到的是所有key值的集合，string key就是一个一个来遍历这个集合
                sum++;
                for (final RunnigRecord record:mData.get(key)){
                    //mData.get(key)得到的是每一个key值对应的所哟value值集合，record就是把这个value集合一个一个遍历
                    if (position == sum){
                        vh.mCalorieView.setText("消耗热量  " + "345");
                        vh.mDistanceView.setText( record.getDistance()+"米");
                        vh.mStartTimeView.setText("" + record.getStartTime());
                        Log.v(LOG_TAG,"跑步开始时间---------》" + record.getStartTime());
                        vh.mTotalTimeView.setText("跑步时长  " + TimeUtil.getRunTime(record.getTotaltime()));
                        Log.v(LOG_TAG,"跑步总时长----------》" + record.getTotaltime());
                        //实现点击记录之后适配器带着数据跳转
                        vh.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.v(LOG_TAG,"打印点击事件------->");
                                Intent intent = new Intent(v.getContext(), RecordDetailActivity.class);
//                                intent.putExtra("id",record.getId());
                                intent.putExtra("record",record);//直接发送记录就行
                                Log.v(LOG_TAG,"打印record------->"+record);
                                v.getContext().startActivity(intent);
                            }
                        });
                        isFinish = true;
                        break;
                    }
                    sum ++;
                }
                if (isFinish)break;
            }
        }
    }

    @Override
    public int getItemCount() {
        //返回数据的条目，遍历数据集合
        int sum = 0;
        for (String key : mData.keySet()) {
            sum++;
            sum += mData.get(key).size();
        }
        return sum;
    }

    @Override
    public int getItemViewType(int position) {
        //根据位置确定视图的类型
        int type = 0;//如果最后返回的是0类型，就是指record视图
        int sum = 0;
        for (String key : mData.keySet()) {
            if (position == sum) {//如果位置跟当前计算出的数据一样,0等于0，就是date所在的位置
                type = 1;//这里返回的就是date视图，类型是1，上面就接受相应的类型
                break;//跳出循环
            }
            sum++;//sum++一直到position相等
            sum += mData.get(key).size();
        }
        return type;
    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_history_date)
        TextView mDateView;
        public DateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class RecordViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_history_record_distance)
        TextView mDistanceView;
        @BindView(R.id.item_history_record_total_time)
        TextView mTotalTimeView;
        @BindView(R.id.item_history_record_calorie)
        TextView mCalorieView;
        @BindView(R.id.item_history_record_start_time)
        TextView mStartTimeView;
        public RecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
