package com.example.thinkpaduser.loverunning;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ThinkPad User on 2016/8/12.
 */
public class Point implements Parcelable {
    private double lat;
    private double lng;

    public Point(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int hashCode() {
        byte var1 = 31;
        byte var2 = 1;
        long var3 = Double.doubleToLongBits(this.lat);
        int var5 = var1 * var2 + (int)(var3 ^ var3 >>> 32);
        var3 = Double.doubleToLongBits(this.lng);
        var5 = var1 * var5 + (int)(var3 ^ var3 >>> 32);
        return var5;
    }

    public boolean equals(Object var1) {
        if(this == var1) {
            return true;
        } else if(!(var1 instanceof Point)) {
            return false;
        } else {
            Point var2 = (Point)var1;
            return Double.doubleToLongBits(this.lat) == Double.doubleToLongBits(var2.lat) && Double.doubleToLongBits(this.lng) == Double.doubleToLongBits(var2.lng);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
    }

    protected Point(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
    }

    public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel source) {
            return new Point(source);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };
}
