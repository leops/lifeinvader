package com.lifeinvader.tracker.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Group implements Parcelable {
    public String name;
    public List<String> users;

    public Group() {
        // NOOP
    }

    public Group(String name, List<String> users) {
        this.name = name;
        this.users = users;
    }

    protected Group(Parcel in) {
        name = in.readString();
        users = new ArrayList<>();
        in.readStringList(users);
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeStringList(users);
    }
}
