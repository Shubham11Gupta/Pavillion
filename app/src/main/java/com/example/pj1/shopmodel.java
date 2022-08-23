package com.example.pj1;

import java.io.Serializable;

public class shopmodel implements Serializable {
    shopmodel(){}
    String Name,Address,Landmark,Category,Email,Image1,Image2,Phone,UID;
    public shopmodel(String name, String address, String landmark,
                     String category, String email, String image1, String image2, String phone,String uid) {
        Name = name;
        Address = address;
        UID=uid;
        Landmark = landmark;
        Category = category;
        Email = email;
        Image1 = image1;
        Image2 = image2;
        Phone = phone;
    }
    public String getUID() {
        return UID;
    }
    public void setUID(String UID) {
        this.UID = UID;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
    public String getLandmark() {
        return Landmark;
    }
    public void setLandmark(String landmark) {
        Landmark = landmark;
    }
    public String getCategory() {
        return Category;
    }
    public void setCategory(String category) {
        Category = category;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public String getImage1() {
        return Image1;
    }
    public void setImage1(String image1) {
        Image1 = image1;
    }
    public String getImage2() {
        return Image2;
    }
    public void setImage2(String image2) {
        Image2 = image2;
    }
    public String getPhone() {
        return Phone;
    }
    public void setPhone(String phone) {
        Phone = phone;
    }
}