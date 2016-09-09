package com.example.thinkpaduser.loverunning.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.media.session.MediaController;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.thinkpaduser.loverunning.R;
import com.baidu.mapapi.map.MapView;
import java.util.ArrayList;
import java.util.List;


public class RecordTrackFragment extends Fragment {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private final static String LOG_TAG = "RecordTrackFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_track,container,false);
//        mMapView = (MapView)view.findViewById(R.id.fragment_track);
        mMapView = (MapView)view.findViewById(R.id.fragment_track);
        mMapView.onCreate(getActivity(),savedInstanceState);
        mBaiduMap = mMapView.getMap();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //添加轨迹点
        PolylineOptions options = new PolylineOptions();
        options.width(8);
        options.color(Color.BLACK);
        Bundle bundle = getArguments();
        Log.v(LOG_TAG,"打印bundle------》" + bundle);
        ArrayList<LatLng> latLngs = bundle.getParcelableArrayList("latlng");//拿存在数据库里的经纬度
        Log.v(LOG_TAG,"latLngs------》" + latLngs);
         latLngs = new ArrayList<>();//这些都是模拟数据
        LatLng latLng = new LatLng(30.670, 104.061);
        latLngs.add(latLng);
        latLng = new LatLng(30.676, 104.062);
        latLngs.add(latLng);
        latLng = new LatLng(30.673, 104.065);
        latLngs.add(latLng);
        latLng = new LatLng(30.674, 104.060);
        latLngs.add(latLng);
        options.points(latLngs);
        if (latLngs.size() >= 2){
            options.points(latLngs);
            mBaiduMap.addOverlay(options);//画折线图
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();//
        for (LatLng i : latLngs) {
            builder.include(i);
        }
        LatLngBounds bounds = builder.build();//生成边界信息
        //获取屏幕区域的大小
        DisplayMetrics dm = new DisplayMetrics();
        Log.v(LOG_TAG,"dm-------->" + dm);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //创建一个CameraUpdate，指定其可视区域的范围
        Log.v(LOG_TAG,"边界信息bounds==>" + bounds + "dm.widthPixels==>" + dm.widthPixels +"dm.heightPixels==>" + dm.heightPixels);
        MapStatusUpdate cameraUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds, dm.widthPixels/200, dm.heightPixels/200);
        mMapView.getMap().animateMapStatus(MapStatusUpdateFactory.zoomTo(4));
// MapStatusUpdate cameraUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
        mBaiduMap.animateMapStatus(cameraUpdate);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
