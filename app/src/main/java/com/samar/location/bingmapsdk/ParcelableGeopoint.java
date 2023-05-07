package com.samar.location.bingmapsdk;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableGeopoint implements Parcelable {
    private final double latitude;
    private final double longitude;

    public ParcelableGeopoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected ParcelableGeopoint(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<ParcelableGeopoint> CREATOR = new Creator<ParcelableGeopoint>() {
        @Override
        public ParcelableGeopoint createFromParcel(Parcel in) {
            return new ParcelableGeopoint(in);
        }

        @Override
        public ParcelableGeopoint[] newArray(int size) {
            return new ParcelableGeopoint[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
