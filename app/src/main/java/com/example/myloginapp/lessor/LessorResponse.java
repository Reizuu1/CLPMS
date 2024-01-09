package com.example.myloginapp.lessor;
import com.google.gson.annotations.SerializedName;

public class LessorResponse {
    @SerializedName("_id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("password1")
    private String password;

    @SerializedName("contact_number")
    private String contactnumber;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("isValid")
    private boolean isValid;

    public String getId(){return id;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public String getContactnumber(){return contactnumber;}

    public String getFullname(){return fullname;}

    public boolean isValid() {
        return isValid;
    }
}
