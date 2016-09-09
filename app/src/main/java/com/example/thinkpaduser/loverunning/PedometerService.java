package com.example.thinkpaduser.loverunning;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.thinkpaduser.loverunning.Step.StepDetector;
import com.example.thinkpaduser.loverunning.Step.StepListener;
import com.google.gson.Gson;

import java.sql.Time;
import java.util.ArrayList;
/**
 * Created by ThinkPad User on 2016/8/9.
 */
public class PedometerService extends Service {
    private Notification mNotification;
    private RemoteViews mRemoteView;
    private LocationClient mLocationClient;//已经换成了百度
    private long mStartTime;
    private final static String LOG_TAG = "PedometerService";
    private AidlPedmeterCallback mAidlPedmeterCallback;
    private int mTotalStep = 0;
    private SensorManager mSensorManager;
    private StepDetector mStepDetector;
    private boolean isRunning = false;
    private DistanceUtil mDistanceUtil;
    private ArrayList<Point> mPoint = new ArrayList<>();

    private Handler handler2 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //更新通知栏上的数据
                long time = System.currentTimeMillis() - mStartTime;
                mRemoteView.setCharSequence(R.id.notifi_running_tv_time,"setText","时间" +"\n" + TimeUtil.getRunTime(time));
//            Log.v(LOG_TAG,"打印一下时间" + TimeUtil.getRunTime(time));
                mRemoteView.setCharSequence(R.id.notifi_running_tv_distance,"setText","距离" +"\n" + mTotalDiatance);
//                Log.v(LOG_TAG,"打印一下距离" + mTotalDiatance);
                mRemoteView.setCharSequence(R.id.notifi_running_tv_stepcount,"setText","步数" +"\n" + mTotalStep);
                NotificationManagerCompat.from(PedometerService.this).notify(1,mNotification);
                //在服务控件里添加内容采用此方法，一定要写此方法，不然没显示！！！
                //下面接收的就是从startFreground()方法里发出来的1
            if (mAidlPedmeterCallback != null) try {
                mAidlPedmeterCallback.onChange(time,mTotalStep,mTotalDiatance,mPoint);
                //调用onchange方法之后数据就会传到fragment里面的onchange方法里，回调
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (isRunning)handler2.sendEmptyMessageDelayed(0,1000);
            //延迟一秒发送给自己的handler2对象就会造成一秒一次计时的情况
            return true;
        }
    });
/**
 * 2.实现AIDL所对应的stub接口
 */
    private AidlPedmeter.Stub binder= new AidlPedmeter.Stub() {//IPedmeter.Stub是匿名内部类，并且继承自IBinder,这里是多态写法
        @Override
        public void start() throws RemoteException {
            //启动数据记录
            isRunning = true;//跑步状态为真
            //获得跑步的起始时间
            mStartTime = System.currentTimeMillis();
            //开始统计跑步的步数使用传感器
            startStepStatistics();
            handler2.sendEmptyMessage(0);
        }
        @Override
        public void stop() throws RemoteException {
            //停止数据记录
            isRunning = false;//跑步状态为false
            long time = System.currentTimeMillis()-mStartTime;
            //停止记录跑步步数的设计
            stopStepStatist();
            //将跑步数据存入数据库，采用SQLite方法
            //1.创建DatabassHelper对象
            DatabassHelper databassHelper = new DatabassHelper(PedometerService.this);
            //2.打开数据库
            SQLiteDatabase db = databassHelper.getWritableDatabase();
            //3.构建需要存储的数据，存入数据
            ContentValues cv = new ContentValues();
            cv.put("start_time", TimeUtil.getTime(mStartTime));
//            Log.v(LOG_TAG,"开始的时间" + TimeUtil.getTime(mStartTime));//几点几分
            cv.put("total_time",time);
//            Log.v(LOG_TAG,"跑步时长" + TimeUtil.getRunTime(time));
            cv.put("distance",mTotalDiatance);
            cv.put("step",mTotalStep);
            cv.put("date",TimeUtil.getCurrentDate());
//            Log.v(LOG_TAG,"跑步日期" + TimeUtil.getDate(mStartTime));//几几年几月几号
            long rowID = db.insert("running_record",null,cv);//表名
            for (Point point:mPoint){
                cv = new ContentValues();
                cv.put("lat",point.getLat());
                cv.put("lng",point.getLng());
                cv.put("record_id",rowID);
                db.insert("record_lnglat",null,cv);
            }
            //5.一定要关闭数据库
            db.close();
        }

    @Override
    public boolean isRunning() throws RemoteException {
        return isRunning;
    }

    @Override
        public void registCallback(AidlPedmeterCallback callback) throws RemoteException{
            mAidlPedmeterCallback = callback;
            //第三步，接受来自fragment传过来的mAidlPedmeterCallback对象
        }
        @Override
        public void unregistCallback(AidlPedmeterCallback callback) throws RemoteException{
            mAidlPedmeterCallback = null;
    }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //绑定服务的时候就会调用该方法
        return binder;
    }
    private void startStepStatistics(){
        mTotalStep = 0;
              //1.获得传感器对象
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //2.定义一个传感器数据监听器
        mStepDetector = new StepDetector();
        mStepDetector.addStepListener(new StepListener() {
            @Override
            public void onStep() {
                ++ mTotalStep;
            }
        });
        mSensorManager.registerListener(mStepDetector,//传感器数据监听
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),//传感器类型
                SensorManager.SENSOR_DELAY_NORMAL);//传感器采集数据的频率
//        SensorEventListener listener = new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent event) {
//                float[] values = event.values;
//                Log.v(LOG_TAG,"----->" + values[0] + "/" + values[1] + "/" + values[2]);
//            }
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int accuracy) {
//            }
//        };
    }
    private void stopStepStatist(){
        mSensorManager.unregisterListener(mStepDetector);
    }

    private LatLng mLastLatlng;
    private double mTotalDiatance = 0;
    private BDLocationListener mLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
//            Log.v(LOG_TAG,"位置监听开启");
            double lat = bdLocation.getLatitude();//计算经纬度
            double lng = bdLocation.getLongitude();
//            Log.v(LOG_TAG,"打印我现在的位置" + lat + "/" + lng);
            if (isRunning){
                mPoint.add(new Point(lat,lng));
            }
            if (mAidlPedmeterCallback != null){
                try {
                    mAidlPedmeterCallback.location(lat,lng);

//                    location方法在fragment里面才能运行
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            LatLng latLng = new LatLng(lat,lng);
            if (mLastLatlng != null && isRunning){//如果上一个经纬度对象存在并且正在跑，计算距离
//                Log.v(LOG_TAG,mLastLatlng+ "<<<+++++>>>" +latLng);
                if (mLastLatlng != latLng && mLastLatlng.latitude != latLng.latitude && mLastLatlng.longitude != latLng.longitude){
                    float distance = (float)mDistanceUtil.getDistance(latLng,mLastLatlng);//计算两个经纬度之间的直线距离
                    mTotalDiatance += distance;//他变成了总距离
                }else{
//                    Log.v(LOG_TAG,"两个点相等");
                    return;
                }
            }
            mLastLatlng = latLng;//它就变成了上一次的经纬度
        }
    };

    private void turnAsForeground(){//调用此方法就可以把这个服务变成前台进程，避免被系统回收,自己写的
        Log.v(LOG_TAG,"服务要开始了");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);//创建通知
        builder.setSmallIcon(R.mipmap.ic_launcher);         //这里就是开始设置通知板块
        builder.setContentTitle("运动记录中");
        builder.setContentText("运动数据");

        mRemoteView = new RemoteViews(getApplication().getPackageName(),R.layout.notifi_running);
        //一定要写getApplication()，不写没显示框！！！
        builder.setContent(mRemoteView);

        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,
                0,      //requestcode
                intent,//Intent未来将要打开的Activity
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        //生成通知对象
        mNotification = builder.build();
        //启用前台进程，提升服务的级别为前台进程级别,也就是说提升服务等级是在服务进程里！
        startForeground(1,mNotification);
    }

    @Override
    public void onCreate() {//每次来服务都先调用onCreat方法！！！
        Log.v(LOG_TAG,"服务里的onCreate方法");
        super.onCreate();
        turnAsForeground();//就是上面写的方法
        mLocationClient = new LocationClient(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // 在服务被解除绑定时调用
        if (!isRunning){//非跑步状态，停止服务
            stopForeground(true);//停止将服务转变为前台进程
            if (mLocationClient != null){//停止定位
                mLocationClient.stop();
                mLocationClient = null;
            }
            stopSelf();//停止服务自身,很关键！!一定要在服务里的东西关闭之后在关闭自身！
        }
        return true;
    }

//    @Override
//    public void unbindService(ServiceConnection conn) {
//        super.unbindService(conn);
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //在每次startService()启动服务的时候会调用该方法,当然是先走完onCreate
        startLocationListen();
        Log.v(LOG_TAG,"开始定位");
        return START_REDELIVER_INTENT;

    }
/*
*开启位置监听
 */
    private void startLocationListen(){
        Log.v(LOG_TAG,"打印自带定位方式");
        //初始化定位
        mLocationClient = new LocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.registerLocationListener(mLocationListener);
        //初始化定位参数
       LocationClientOption mLocationOption = new LocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
       //设置坐标系，一定要写这句话不然会定偏！！！！！
        // 坐标类型分为三种：国测局经纬度坐标系(gcj02)，百度墨卡托坐标系(bd09)，百度经纬度坐标系(bd09ll)。
        mLocationOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        mLocationOption.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationOption.setOpenGps(true);//可选，默认false,设置是否使用gps
        mLocationOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mLocationOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mLocationOption.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        mLocationOption.setIsNeedAddress(false);//设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setOpenGps(true);//可选，默认false,设置是否使用gps
//        mLocationOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        mLocationOption.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        mLocationOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(mLocationOption);
        mLocationClient.start();
    }

}
