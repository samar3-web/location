package com.samar.location.models;

public class Tenant {
    String name , lastName, email , phone ,amtPaid , paymentDate , shiftingDate;


    public Tenant() {
    }

    public Tenant(String name, String email, String phone, String amtPaid) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.amtPaid = amtPaid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getAmtPaid() {
        return amtPaid;
    }

    public void setAmtPaid(String amtPaid) {
        this.amtPaid = amtPaid;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getShiftingDate() {
        return shiftingDate;
    }

    public void setShiftingDate(String shiftingDate) {
        this.shiftingDate = shiftingDate;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", amtPaid='" + amtPaid + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", shiftingDate='" + shiftingDate + '\'' +
                '}';
    }
}

