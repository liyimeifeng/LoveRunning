package com.example.thinkpaduser.loverunning;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;//v4.Fragment的兼容性更宽，4.0版本以前的也能使用，推荐使用。改成那个也没问题
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.example.thinkpaduser.loverunning.fragment.AboutFragment;
import com.example.thinkpaduser.loverunning.fragment.HelpFragment;
import com.example.thinkpaduser.loverunning.fragment.HistoryFragment;
import com.example.thinkpaduser.loverunning.fragment.SetFragment;
import com.example.thinkpaduser.loverunning.fragment.StaticsFragment;
import com.example.thinkpaduser.loverunning.fragment.WeatherFragment;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.moments.WechatMoments;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,HelpFragment.OnFragmentListener {
private final static String LOG_TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        //从这里开始启动服务
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //这是百度地图需要实现的方法!!!!!!!!!注意该方法要再setContentView方法之前实现.
        SDKInitializer.initialize(getApplicationContext());
        ShareSDK.initSDK(MainActivity.this,"sharesdk的appkey");//shareSDK初始化
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView dateView = (TextView)headerView.findViewById(R.id.nav_header_main_tv_date);
        dateView.setText(TimeUtil.getCurrentMonthAndDay());
        TextView timeView = (TextView)headerView.findViewById(R.id.nav_header_main_tv_time);
        timeView.setText(TimeUtil.getTime(System.currentTimeMillis()));
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceMapFragment(new WeatherFragment());
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        checkAndRequestPermission();
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setSilent(false);//

    //实现分享之后回调，比如分享完成之后跳出对话框询问你是否留在当前页面
    oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
    @Override
    public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
        if (platform.getName().equalsIgnoreCase(WechatMoments.NAME)){//忽略大小写如果相等
            paramsToShare.setShareType(Platform.SHARE_IMAGE);
            //实现分享完成之后跳出一张图片
            paramsToShare.setImageUrl("http://sweetystory.com/Public/ttwebsite/theme1/style/img/special-1.jpg");
        }
    }
});
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("英雄联盟");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://lol.qq.com/main.shtml");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我分享给你看");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    private void checkAndRequestPermission(){
        //用来存4个权限
        ArrayList<String> permissions = new ArrayList<>();
        //检查外存储权限是否已经授权
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        //检查精确定位权限是否已经授权
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        //检查混合定位权限是否已经授权
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        //检查查看手机状态权限是否已经授权
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissions.size() > 0){//有权限需要授权
            //将权限集合转换为数组
            String[] a = new String[permissions.size()];//记住只能这样写！！！！
            a = permissions.toArray(a);
            ActivityCompat.requestPermissions(this, a,0);
        }else{//没有权限需要授权,显示视图
            addMapFragment();
        }
    }
    private void addMapFragment(){
        //1、获得FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        //2、获得FragmentTransaction对象
        FragmentTransaction ft = fm.beginTransaction();
        //3、添加Fragment
        ft.add(R.id.content_main_container,new MapFragment());
        ft.commitAllowingStateLoss();//用这个方法就可以避免第一次运行fragment出现addFragment报错！！！！！
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 0:
                boolean isGranted = true;
                //遍历权限申请结果的数组
                for (int i:grantResults){
                    if(i != PackageManager.PERMISSION_GRANTED){
                        isGranted = false;//现在变成false了，下面就不执行了
                    }
                }
                if (isGranted){//已经授权
                    addMapFragment();
                }else{//存在未授权的权限
                    MainActivity.this.finish();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
//        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
//        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
//        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.share) {
            Log.v(LOG_TAG,"点击分享");
            Toast.makeText(getApplicationContext(),"正在分享",Toast.LENGTH_SHORT).show();
            showShare();
//            QQ.ShareParams sp = new QQ.ShareParams();
//            sp.setText("测试分享的文本");
////            sp.setImagePath("/mnt/sdcard/location.jpg”);
//
//            Platform weibo = ShareSDK.getPlatform(QZone.NAME);
////            weibo.setPlatformActionListener(paListener); // 设置分享事件回调
//// 执行图文分享
//            weibo.share(sp);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            replaceMapFragment(new MapFragment());//t跳转到主页
            getSupportActionBar().setTitle("LoveRunning");
        } else if (id == R.id.nav_history) {
            replaceMapFragment(new HistoryFragment());
            Log.v(LOG_TAG,"历史界面启动");
            getSupportActionBar().setTitle("历史记录");
        } else if (id == R.id.nav_statistics) {
            replaceMapFragment(new StaticsFragment());//点击之后跳转到统计
            getSupportActionBar().setTitle("数据统计");
        } else if (id == R.id.nav_about) {
            replaceMapFragment(new AboutFragment());
            getSupportActionBar().setTitle("关于");
        } else if (id == R.id.nav_help) {
            replaceMapFragment(new HelpFragment());
            getSupportActionBar().setTitle("帮助");
        } else if (id == R.id.nav_manage) {
            replaceMapFragment(new SetFragment());
            getSupportActionBar().setTitle("设置");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);//实现点击之后侧边栏自动缩回
        return true;
    }

    private void replaceMapFragment(Fragment fragment){
        //1、获得FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        //2、获得FragmentTransaction对象
        FragmentTransaction ft = fm.beginTransaction();
        //3、添加Fragment
        ft.replace(R.id.content_main_container,fragment);
        ft.commit();
    }

//  所有就得让mainAct控制HelpFragment，以实现点击html地址中的任意页面再返回返回的是上一次目录
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v(LOG_TAG,"-----------onKeyDown");
        //如果自己处理对应按键的处理方式，则返回true
        if(keyCode == KeyEvent.KEYCODE_BACK){//如果点击的是home旁边的返回键
            if (mListener != null){
                if (mListener.canGoBack()){
                    mListener.back();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //2、在Activity中（最上面）实现Fragment里声明的接口和方法，这就是第一次回调
    private HelpFragment.OnBackKeyClickListener mListener;
    @Override
    public void regist(HelpFragment.OnBackKeyClickListener listener) {
        Log.v(LOG_TAG,"----------->regist");
        mListener = listener;
    }

}

