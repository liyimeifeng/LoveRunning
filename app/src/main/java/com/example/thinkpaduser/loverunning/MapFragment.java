package com.example.thinkpaduser.loverunning;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MapFragment extends Fragment {
    //这种找控件方式是先点击onCreateView里面的layout布局名字，右键generate就有
    private final static String LOG_TAG = "MapFragment";
    @BindView(R.id.content_main_tv_distance)
    TextView mDistanceView;
    @BindView(R.id.content_main_tv_time)
    TextView mTimeView;
    @BindView(R.id.content_main_tv_speed)
    TextView mSpeedview;
    @BindView(R.id.content_main_tv_step)
    TextView mStepView;
    @BindView(R.id.content_main_but_startbutton)
    Button startButtonView;
    @BindView(R.id.map)
    MapView mMapView;

    private Unbinder unbinder;
    private AidlPedmeter mAidlPedmeter;
    private BaiduMap mBaiduMap;

    private Handler handler3 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (isStop) {
                return true; //不管返回真还是假下面代码都不走了,相当于下面写用的else语句
            }
            mTimeView.setText(TimeUtil.getRunTime(msg.what));
            mDistanceView.setText(msg.arg2 + "米");
            mStepView.setText(msg.arg1 + "步");
//            Log.v(LOG_TAG, "打印handler距离" + msg.arg2);
//            Log.v(LOG_TAG, "打印handler里面的时间秒数" + msg.what / 1000);
            if (msg.what / 1000 == 0) {
                mSpeedview.setText("0米/秒");
            } else {
                double speed = msg.arg2 / (msg.what / 1000);
                DecimalFormat df = new DecimalFormat("0.00");
                df.format(speed);
                mSpeedview.setText(df.format(speed) + "米/秒");
            }

            List<Point> points = (List<Point>) msg.obj;
            if (points.size() >= 2){
                PolylineOptions polylineOptions = new PolylineOptions();
                List<LatLng> latLngs = new ArrayList<>();
                for (Point point : points) {
//                polylineOptions.points(new LatLng(point.getLat(), point.getLng()));
                    latLngs.add(new LatLng(point.getLat(),point.getLng()));
                }
                polylineOptions.color(Color.BLACK);
                polylineOptions.width(8);
                polylineOptions.visible(true);
                polylineOptions.points(latLngs);
                mBaiduMap.addOverlay(polylineOptions);
            }

            try {//恢复跑步状态
                Log.v(LOG_TAG,"恢复跑步状态后------------>"+ mAidlPedmeter.isRunning());
                if (mAidlPedmeter.isRunning()){
                    startButtonView.setText("停止");
                }else{
                    startButtonView.setText("开始");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return true;
        }
    });

    //第一步创建实例化对象，采用多态写法
    private AidlPedmeterCallback.Stub aidlPedmeterCallback = new AidlPedmeterCallback.Stub() {//多态写法
        @Override
        public void location(double lat, double lng) throws RemoteException {
//            Log.v(LOG_TAG, "现在的位置" + lat + "/" + lng);
            //将位置信息发送到主线程更新UI
            LatLng latLng = new LatLng(lat, lng);
            Message msg = handler.obtainMessage();
            msg.obj = latLng;
            handler.sendMessage(msg);
        }

        @Override
        public void onChange(long time, int step, double dis, List<Point> points) throws RemoteException {
            Message msg = handler3.obtainMessage();
            msg.what = (int) time;
            msg.arg1 = step;
            msg.arg2 = (int) dis;
            msg.obj = points;
            handler3.sendMessage(msg);
        }

    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            LatLng latLng = (LatLng) msg.obj;
//            Log.v(LOG_TAG,"map里面接受到的经纬度" + latLng.longitude+"----" + latLng.latitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location);
//            Log.v(LOG_TAG,"打印bitmap-----     " + bitmap);
            markerOptions.icon(bitmap);
            markerOptions.zIndex(9);
            markerOptions.draggable(true);
            markerOptions.anchor(0.5f,1f);
            Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);
//            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, bitmap);
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(latLng, mBaiduMap.getMaxZoomLevel()/(float)1.15);
//           Log.v(LOG_TAG,"打印mBaiduMap=====》》》》" + mBaiduMap);
//            Log.v(LOG_TAG,"update=====》》》》" + update);
            if (update != null&&mBaiduMap != null){
                mBaiduMap.animateMapStatus(update);
            }
            return true;
        }
    });

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //在服务已连接的时候调用
            Log.v(LOG_TAG, "服务已连接");
            startButtonView.setVisibility(View.VISIBLE);//服务连接的时候再显示按钮
            mAidlPedmeter = AidlPedmeter.Stub.asInterface(service);
            try {
                mAidlPedmeter.registCallback(aidlPedmeterCallback);
                //第二步然后这里就往service里面传入了一个aidlPedmeterCallback对象
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //在服务断开连接的时候调用
            Log.v(LOG_TAG, "服务链接已断开");
            try {
                mAidlPedmeter.unregistCallback(aidlPedmeterCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mAidlPedmeter = null;
        }
    };

    //先onCreateView返回一个view，再onViewCreated绑定控件
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //用inflater把布局转换为视图
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    //如果要找控件是在这个方法里面找控件！！！
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView.onCreate(getContext(),savedInstanceState);
        //开始定位前应该完成的步骤
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
    }

    //5.第一个activity的onStop()方法。当第一个activity处于不可见的状态，也就是全部被遮住了就会调用这个。
// 如果只遮挡一部分就不会调用这个。注意此时只是onStop而没有onDestory,也就是该activity是还存在的
    private boolean isStop = false;
    @Override
    public void onStop() {
        isStop = true;//只要页面不可见，上面handler里面就返回，下面代码不执行，就不会出现报空的情况
        //解除绑定服务，软件退出前台之后解除绑定，然后UI里就不能操作服务
        getActivity().unbindService(conn);
        super.onStop();
    }

    @Override
    public void onDestroyView() {//销毁视图
        Log.v(LOG_TAG, "打印onDestroyView");
        super.onDestroyView();
//        try {
//            mAidlPedmeter.getMapView(new Gson().toJson(mMapView));
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
        try {
            mAidlPedmeter.isMapViewDestory();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mMapView.onDestroy();
        unbinder.unbind();//销毁控件，就不存在textBView之类
    }

    @Override
    public void onDestroy() {//比onDestroyView范围大
        Log.v(LOG_TAG, "最后一步对应onCreat就是onDestory");
        super.onDestroy();
    }

    //3.当获得用户焦点的时候调用onResume()
    public void onResume() {
        Log.v(LOG_TAG, "可见之后获得焦点调用3.onResume");
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    //4.第一个activity跳到第二个activity,第一个页面调用onPause()方法,因为要调用这个来保存数据
    @Override
    public void onPause() {
        Log.v(LOG_TAG, "第一个页面调用4.onPause");
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    //1.启动一个activity时，就会先调用onCreate()
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "首先1.onCreat");
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(new Intent(getActivity(), PedometerService.class));
        getActivity().startService(intent);
    }

    //2.当这个activity被看到的时候就调用onStart()
    @Override
    public void onStart() {
        super.onStart();
        isStop = false;
        Log.v(LOG_TAG, "从不可见到可见2.onStart");
        Intent intent = new Intent(new Intent(getActivity(), PedometerService.class));
        //绑定服务，是为了能让用户直接在UI里操作服务
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        //BIND_AUTO_CREATE可以自动创建启动服务，但是会在解除绑定的时候自动停止服务
    }


    /*
    *  onSaveInstanceState是在该activity被系统回收之前，通常是在后台，内存不够时调用，用于保存想要保存的数据、状态等
    *  在onPause方法之前，在onStop方法之后
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    //    private boolean isRunning = false;
    @OnClick(R.id.content_main_but_startbutton)
    public void onClick() {
        Log.v(LOG_TAG, "点击按钮之后" + mAidlPedmeter);
        try {
            if (mAidlPedmeter == null)return;
            else if (mAidlPedmeter != null) {
                if (mAidlPedmeter.isRunning()) {//接受servic里发送来的isrunning状态，如果正在跑步
                    Log.v(LOG_TAG, "打印跑步状态为真" + mAidlPedmeter.isRunning());
                    mAidlPedmeter.stop();
                    startButtonView.setText("开始");
                } else {//一开始肯定没跑步，所以都是假，然后启动服务
                    Log.v(LOG_TAG, "打印跑步状态为假" + mAidlPedmeter.isRunning());
                    mAidlPedmeter.start();
                    startButtonView.setText("停止");
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
