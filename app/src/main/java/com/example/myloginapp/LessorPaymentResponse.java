package com.example.myloginapp;

import android.os.Parcel;
import android.os.Parcelable;


public class LessorPaymentResponse implements Parcelable {
    private String objectId;
    private String lessorname, lesseename, status, email, date, screenshot;

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
    protected LessorPaymentResponse(Parcel in) {
        objectId = in.readString();
        email = in.readString();
        screenshot = in.readString(); // Read the screenshot as a String
        date = in.readString();
        lessorname = in.readString();
        status = in.readString();
        lesseename = in.readString();
        date = in.readString();
    }

    public static final Creator<PaymentResponse> CREATOR = new Creator<PaymentResponse>() {
        @Override
        public PaymentResponse createFromParcel(Parcel in) {
            return new PaymentResponse(in);
        }

        @Override
        public PaymentResponse[] newArray(int size) {
            return new PaymentResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objectId);
        dest.writeString(email);
        dest.writeString(screenshot);
        dest.writeString(lessorname);
        dest.writeString(status);
        dest.writeString(lesseename);
        dest.writeString(date);


    }
    public String getObjectId(){return objectId;}
    public String getImage() {
        return screenshot;
    }
    public String getEmail(){return email;}

    public String getLessor(){return lessorname;}

    public String getStatus(){return status;}

    public String getLessee(){return lesseename;}

    public String getDate(){return date;}
}