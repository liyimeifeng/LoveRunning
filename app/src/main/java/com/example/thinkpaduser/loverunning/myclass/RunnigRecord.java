package com.example.thinkpaduser.loverunning.myclass;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by ThinkPad User on 2016/8/16.
 */
public class RunnigRecord implements Parcelable {
    private int id;
    private String startTime;
    private Long totaltime;
    private int step;
    private float distance;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Long getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(Long totaltime) {
        this.totaltime = totaltime;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RunnigRecord() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.startTime);
        dest.writeLong(this.totaltime);
        dest.writeInt(this.step);
        dest.writeFloat(this.distance);
        dest.writeString(this.date);
    }

    protected RunnigRecord(Parcel in) {
        this.id = in.readInt();
        this.startTime = in.readString();
        this.totaltime = in.readLong();
        this.step = in.readInt();
        this.distance = in.readFloat();
        this.date = in.readString();
    }

    public static final Creator<RunnigRecord> CREATOR = new Creator<RunnigRecord>() {
        @Override
        public RunnigRecord createFromParcel(Parcel source) {
            return new RunnigRecord(source);
        }

        @Override
        public RunnigRecord[] newArray(int size) {
            return new RunnigRecord[size];
        }
    };
}
