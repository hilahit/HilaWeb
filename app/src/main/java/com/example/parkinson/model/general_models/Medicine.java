package com.example.parkinson.model.general_models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Medicine implements Parcelable {
    private String id;
//    private String categoryId;

    private String name;
    private Double value;
    List<Time> hoursArr;

    public Medicine() {
    }

    public Medicine(String id,String name) {
        this.id = id;
        this.name = name;
    }

    protected Medicine(Parcel in) {
        id = in.readString();
        name = in.readString();
        value = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public List<Time> getHoursArr() {
        return hoursArr;
    }

    public void setHoursArr(List<Time> hoursArr) {
        this.hoursArr = hoursArr;
    }

    public String dosageString() {
        if (value == 0.25) {
            return "רבע כדור";
        } else if (value == 0.50) {
            return "חצי כדור";
        } else if (value == 0.75) {
            return "שלושת רבעי כדור";
        } else if (value == 1.00) {
            return "כדור בודד";
        } else if (value == 1.25) {
            return "כדור ורבע";
        } else if (value == 1.50) {
            return "כדור וחצי";
        } else if (value == 1.75) {
            return "כדור ושלושת רבעי";
        } else if (value == 2.00) {
            return "שני כדורים";
        }
        return "";
    }

}
