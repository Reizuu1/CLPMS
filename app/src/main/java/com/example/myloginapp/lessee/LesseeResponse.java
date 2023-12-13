package com.example.myloginapp.lessee;
import com.google.gson.annotations.SerializedName;

public class LesseeResponse {
    @SerializedName("_id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("contact_number")
    private String contactnumber;

    @SerializedName("isValid")
    private boolean isValid;

    public String getId(){return id;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public String getContactnumber(){return contactnumber;}

    public boolean isValid() {
        return isValid;
    }
}
