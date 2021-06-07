package com.example.demoserver;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * FileName    :
 * Description :
 */
public class SnapMessage implements Parcelable {
    public String type;
    public String params;


    public SnapMessage(String type, String params) {
        this.type = type;
        this.params = params;
    }


    protected SnapMessage(Parcel in) {
        type = in.readString();
        params = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(params);
    }

    public void readFromParcel(Parcel parcel) {
        type = parcel.readString();
        params = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SnapMessage> CREATOR = new Creator<SnapMessage>() {
        @Override
        public SnapMessage createFromParcel(Parcel in) {
            return new SnapMessage(in);
        }

        @Override
        public SnapMessage[] newArray(int size) {
            return new SnapMessage[size];
        }
    };

    @Override
    public String toString() {
        return "SnapMessage{" +
                "type='" + type + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
