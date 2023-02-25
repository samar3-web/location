package com.samar.location.models;

import java.util.List;

public class Owner_Model {

    String name , gender, address , email , phone , password , accountType , profileUrl;
   /* List<String> houseList;*/

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Owner_Model() {
    }

    public Owner_Model(String name, String gender, String address, String email, String phone, String password, String accountType, List<String> houseList) {
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.accountType = accountType;
//        this.houseList = houseList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

//    public List<String> getHouseList() {
//        return houseList;
//    }

//    public void setHouseList(List<String> houseList) {
//        this.houseList = houseList;
//    }
}
