package com.example.myloginapp.lessee;
import com.google.gson.annotations.SerializedName;

public class LesseeResponse {
    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("contact_number")
    private String contactnumber;

    @SerializedName("isValid")
    private boolean isValid;

    public String getId(){return id;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getContactnumber(){return contactnumber;}

    public boolean isValid() {
        return isValid;
    }
}
