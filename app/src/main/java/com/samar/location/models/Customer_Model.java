package com.samar.location.models;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Customer_Model implements Map<String, Object> {

    String name , lastName , age , gender, address, profileUrl,email, phone, password, accountType;
     //To Store the information of house in which tenant is staying
      House house;

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Customer_Model() {
    }

    public Customer_Model(String name, /*String age*/String gender, String address, String profileUrl, String email, String phone, String password, String accountType) {
        this.name = name;
       // this.age = age;
        this.gender = gender;
        this.address = address;
        this.profileUrl = profileUrl;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey( Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }



    @Override
    public Object get(Object key) {
        return null;
    }


    @Override
    public Object put(String key, Object value) {
        return null;
    }


    @Override
    public Object remove( Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {

    }

    @Override
    public void clear() {

    }


    @Override
    public Set<String> keySet() {
        return null;
    }


    @Override
    public Collection<Object> values() {
        return null;
    }


    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }
}
