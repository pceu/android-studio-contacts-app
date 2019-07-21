package com.example.aaceu.project1;


public class Contact {
    // userID is the same id used in database and this is for updating database
    // sometimes we can have the same name thus using id is the error clean
    private int userID;
    private String name;        // contact name
    private String phoneNum;    // contact person phone number
    private String email;       // contact person email address
    private String company;     // contact person company name
    // contact person image, there are two images in this project user can select one
    // image is stored as string for storing objects from this class to database efficiently
    // we can refer to the image in drawable folder according to the image name set in here
    private String image;

    // constructor of Contact class, assign parameters to appropriate property in this class
    public Contact(int userID, String name, String phoneNum, String email, String company, String image) {
        this.userID = userID;
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.company = company;
        this.image = image;
    }

    // getter and setter methods for each attribute/property in this class
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
