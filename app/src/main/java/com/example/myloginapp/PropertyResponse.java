package com.example.myloginapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

public class PropertyResponse implements Parcelable {

    private String userEmail;
    private String propertyname, lessor, status, barangay, street, floors, floorOcu, lessee, unit;
    private ImageData image;

    public static class ImageData implements Parcelable {
        private int Subtype;
        private String Data;

        protected ImageData(Parcel in) {
            Subtype = in.readInt();
            Data = in.readString();
        }
        public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
            @Override
            public ImageData createFromParcel(Parcel in) {
                return new ImageData(in);
            }

            @Override
            public ImageData[] newArray(int size) {
                return new ImageData[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(Subtype);
            dest.writeString(Data);
        }
        @Override
        public int describeContents() {
            return 0;
        }

        // Getter and Setter for Data field
        public String getData() {
            return Data;
        }

        public void setData(String data) {
            Data = data;
        }
    }
    protected PropertyResponse(Parcel in) {
        userEmail = in.readString();
        image = in.readParcelable(ImageData.class.getClassLoader());
        propertyname = in.readString();
        lessor = in.readString();
        status = in.readString();
        barangay = in.readString();
        street = in.readString();
        floors = in.readString();
        floorOcu = in.readString();
        lessee = in.readString();
        unit = in.readString();
    }

    public static final Creator<PropertyResponse> CREATOR = new Creator<PropertyResponse>() {
        @Override
        public PropertyResponse createFromParcel(Parcel in) {
            return new PropertyResponse(in);
        }

        @Override
        public PropertyResponse[] newArray(int size) {
            return new PropertyResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userEmail);
        dest.writeParcelable(image, flags);
        dest.writeString(propertyname);
        dest.writeString(lessor);
        dest.writeString(status);
        dest.writeString(barangay);
        dest.writeString(street);
        dest.writeString(floors);
        dest.writeString(floorOcu);
        dest.writeString(lessee);
        dest.writeString(unit);

    }
    public ImageData getImage(){return image;}

    public String getPropertyname(){return propertyname;}

    public String getLessor(){return lessor;}

    public String getStatus(){return status;}

    public String getBarangay(){return barangay;}
    public String getStreet(){return street;}

    public String getUserEmail(){return userEmail;}

    public String getFloors(){return floors;}

    public String getFloorOcu(){return floorOcu;}

    public String getLessee(){return lessee;}

    public String getUnit(){return unit;}
}