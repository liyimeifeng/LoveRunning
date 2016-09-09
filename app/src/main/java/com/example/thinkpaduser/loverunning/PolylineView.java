package com.example.thinkpaduser.loverunning;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad User on 2016/8/18.
 *
 * 这是为了自己画图，自己画图，自己画图，自己画图。
 *
 */
public class PolylineView extends View {//自己画图的方式！！！！！麻烦！！！！
    private List<LatLng> latLngs = new ArrayList<>();
    private Paint mPaint = new Paint();
    private int lineWidth;
    private float mSpeed;
    private float pixel;
    public PolylineView(Context context) {
        this(context,null);
    }

    public PolylineView(Context context, AttributeSet attrs) {//如果想让视图在布局中显示就一定要写该方法！！！！
        super(context, attrs);
    }

    public PolylineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//设置抗锯齿
       lineWidth = (int)(getResources().getDisplayMetrics().density*2);
        //设直线条宽度为2dp
        mPaint.setStrokeWidth(lineWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //用于视图的大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        pixel = getResources().getDisplayMetrics().density;//1dp有多少像素
        int width = 0;//定义一个变量表示视图最终的宽度
        if (widthMode == MeasureSpec.AT_MOST){//相当于wrap_content
            //视图可以使任意指定的大小
            width = widthSize;
        }else if (widthMode == MeasureSpec.EXACTLY){
            //父容器对其大小进行了限制，最大值只能是限制的大小
            width = (int)(250*pixel);
            if (width > widthSize){
                width = widthSize;
            }
        }else if ( widthMode == MeasureSpec.UNSPECIFIED){
            //父容器对其没有任何限制，可以是任意的大小
            width = (int)(250*pixel);
        }

        int heightMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);
       pixel = getResources().getDisplayMetrics().density;//1dp有多少像素
        int height = 0;//定义一个变量表示视图最终的高度
        if (heightMode == MeasureSpec.AT_MOST){//相当于wrap_content
            //视图可以使任意指定的大小
            height = heightSize;
        }else if (heightMode == MeasureSpec.EXACTLY){
            //父容器对其大小进行了限制，最大值只能是限制的大小
            height = (int)(180*pixel);
            if (height > widthSize){
                height = heightSize;
            }
        }else if ( heightMode == MeasureSpec.UNSPECIFIED){
            //父容器对其没有任何限制，可以是任意的大小
            height = (int)(180*pixel);
        }
            setMeasuredDimension(width,height);//计算出视图的大小
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas画布，可以使用其方法绘制图形
        //绘制背景
        canvas.drawColor(Color.WHITE);
        mPaint.setColor(Color.BLACK);
        int width = getWidth();
        int height = getHeight();
        int l = 48;//左边的边距
        int r = 48;//右边的边距
        int t = 48;//上部的边距
        int b = 48;//下部的边距
        //绘制X轴和y轴
        canvas.drawLine(l,height-b,width-r,height-b,mPaint);
        canvas.drawLine(l,height-b,l,t,mPaint);
        //绘制一个x,y轴上的三角形
        float m = pixel*8;
        float n = pixel*2;
        Path path = new Path();
        path.moveTo(m,0);
        path.lineTo(0,-n);
        path.lineTo(0,n);
        path.close();
        //设置画笔模式为填充，就可以画出填充的三角形
        mPaint.setStyle(Paint.Style.FILL);
        //通过画布变换的方式，绘制x坐标的三角形
        canvas.save();//保存当前画布的状态
        canvas.translate(width-r,height-b);
        canvas.drawPath(path,mPaint);
        canvas.restore();//恢复原来的状态
        //绘制y坐标的三角形
        canvas.save();
        canvas.translate(l,t);
        canvas.rotate(-90);//翻转90度
        canvas.drawPath(path,mPaint);
        canvas.restore();

        //设置x轴方向上平分8份
        int xCount = 8;
        //计算轨迹点的总数
        int size = latLngs.size();
        size = 3800;//测试使用
        //计算总时间
        if (latLngs == null||latLngs.size() == 0){
            return;
        }
        int time = size*1;
        //每一份的时间，按分钟数计算
        int it = time /60 /xCount;
        //每一分的宽度
        int iw = (width - l - r)/xCount;
        //坐标原点的x，y
        int xo = 1;
        int yo = height - b;
        //刻度线的高度
        float sw = 4*pixel;
        mPaint.setTextSize(8 * pixel);
        for (int i = 1;i < xCount;i ++){
            //绘制刻度线
            canvas.drawLine(l + i * iw,yo,yo+i*iw,yo - sw,mPaint);
            //绘制刻度线下的文字
            String text = i * it + "";
            //测量文字的宽度
            float w = mPaint.measureText(text);
            canvas.drawText(text,xo + i*iw - w/2,yo + mPaint.getTextSize(),mPaint);
        }
        //绘制x方向刻度
        String text = "时间(分)";
        float w = mPaint.measureText(text);
        canvas.drawText(text, xo + xCount * iw - w / 2, yo + mPaint.getTextSize(), mPaint);

        int ycount = 4;
        //绘制Y轴上的文字和刻度
        int ih = (height - t - b) / 4;
        for (int i = 1; i < 4; i++) {
            canvas.drawLine(xo,yo-ih*i,xo+sw,yo-ih*i,mPaint);
            text = i+"";
            //测量文字的宽度
            w = mPaint.measureText(text);
            canvas.drawText(text,xo-w-8,yo-ih*i+mPaint.getTextSize()/2,mPaint);
        }
        //绘制y方向的单位
        text = "速度";
        w = mPaint.measureText(text);
        canvas.drawText(text,xo -w-8,t,mPaint);
        text="m/s";
        w = mPaint.measureText(text);
//        StringBuilder sb = new StringBuilder();
//        sb.append("平均速度");
//        sb.append(mSpeed);
//        sb.append("km/H");
//        mPaint.setTextSize(pixel*16);
//        //获得文本的宽度
//        float w = mPaint.measureText(new String(sb));
        canvas.drawText(text,xo-w-8,t+mPaint.getTextSize(),mPaint);
        //绘制折线点
        //根据份数计算品均速度
        Path linePath = new Path();
        List<Float> aveSpeed = new ArrayList<>();
        int il = size/xCount;
        float sum = 0;
        int ni = 0;
        for (int i = 1; i < size; i ++){
            sum += DistanceUtil.getDistance(latLngs.get(i-1),latLngs.get(i));
            ni ++;
            if (ni == il){
                //计算平均速度
                aveSpeed.add(sum/il);
                sum = 0;
                ni = 0;
            }
        }


    }
    public void setLatLngs(LatLng latLngs){

    }
}
