package com.example.thinkpaduser.loverunning;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class OpenAPPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_open_app);
        Handler handler = new Handler();//开机画面跳转到主页
        handler.postDelayed(new OpenThread(),1500);
    }

    private class OpenThread extends Thread{
        @Override
        public void run(){
            super.run();
            Intent intent = new Intent(OpenAPPActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();//写上这句话避免每次按返回跳转到软件开始界面
    }
}
