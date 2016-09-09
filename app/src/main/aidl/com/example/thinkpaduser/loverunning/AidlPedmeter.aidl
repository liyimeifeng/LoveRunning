// IPedmeter.aidl
package com.example.thinkpaduser.loverunning;
import com.example.thinkpaduser.loverunning.AidlPedmeterCallback;

// Declare any non-default types here with import statements

interface AidlPedmeter {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    boolean isRunning();
    void start();
    void stop();

    void registCallback(AidlPedmeterCallback callback);//注册回调
    void unregistCallback(AidlPedmeterCallback callback);//非注册回调
    void isMapViewDestory();
}
