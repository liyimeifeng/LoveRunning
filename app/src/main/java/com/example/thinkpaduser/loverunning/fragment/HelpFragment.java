package com.example.thinkpaduser.loverunning.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.thinkpaduser.loverunning.MainActivity;
import com.example.thinkpaduser.loverunning.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {
    private final static String LOG_TAG = "HelpFragment";
    private WebView webView;
    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView =(WebView)view.findViewById(R.id.fragment_help_WebView);
        webView.loadUrl("http://www.baidu.com");//加载网址，这里实验
        //加载assets目录下的html文件
//        webView.loadUrl("file:///android_asset/help.html");
        //加载assets目录下的html文件
//       webview.loadUrl("file:///mnt/sdcard/indext.html" );
        webView.setWebViewClient(new MyWebViewClient());//设置了之后网址就会在wenView中显示，而不是跳转到其他浏览器显示
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //如果需要在WebView里面处理（比如加载百度页面），则返回false，如果要跳转都其他浏览器之类的，返回true
            //在实际环境中，可以根据不同的请求地址设置不同的方式
            return false;
        }
    }
//    为了将数据从Fragment传入到Activity，以实现点击html地址中的任意页面再返回返回的是上一次目录
//    1、定义一个接口并定义一个方法声明需要传递的数据的类型
    private OnBackKeyClickListener listener = new OnBackKeyClickListener() {//这是我们想传入activity的参数类型

        @Override
        public void onBackKeyClick() {
            Toast.makeText(getActivity(),"点击返回按钮",Toast.LENGTH_SHORT).show();
        }
        @Override
        public boolean canGoBack() {
            return webView.canGoBack();
        }

        @Override
        public void back() {
            webView.goBack();
        }
    };

    public interface OnBackKeyClickListener{//理论上这个定义成一个类也是可以的，只是实现起来复杂，默认使用接口的方式重写类里的方法
        public void onBackKeyClick();
        public boolean canGoBack();
        public void back();
    }

    public interface OnFragmentListener{
        public void regist(OnBackKeyClickListener listener);
    }

    //需要将对象传到Activity，如何将数据传递到Activity
    //3、获得上面创建的接口对象
    private OnFragmentListener mListener;
    @Override
    public void onAttach(Context context) {//context在这里表示MainActivity
        Log.v(LOG_TAG,"---------->onAttach");
        super.onAttach(context);
        //一旦activity里面实现了上面这个接口，就需要把context也就是mainActivity转换成接口类型
        mListener = (OnFragmentListener) context;
        //调用接口方法将数据传入到Activity
        mListener.regist(listener);
    }

}
