// AidlPedmeterCallback.aidl
package com.example.thinkpaduser.loverunning;
import com.example.thinkpaduser.loverunning.Point;
// Declare any non-default types here with import statements

interface AidlPedmeterCallback {
 void location(double lat,double lng);
// void onChange(long time,float distance,int step);
void onChange(long time,int step,double dis,in List<Point> points);
}
