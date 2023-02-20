package com.samar.location;

public class Home {
    String name;
    String price;
    Integer imageUrl;
    String  productDescription;
    String productCategory;
    String productNumber;
    String rating;
    String restorantname;

    public Home(String name, String price, Integer imageUrl, String productDescription, String productCategory, String productNumber, String rating, String restorantname) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productNumber = productNumber;
        this.rating = rating;
        this.restorantname = restorantname;
    }

    public Home() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRestorantname() {
        return restorantname;
    }

    public void setRestorantname(String restorantname) {
        this.restorantname = restorantname;
    }
}
