package com.example.tab_layout;

public class ContactModel {
    //Initialize variable
    private String name,number;

    public ContactModel()
    {}

    public ContactModel(String name, String number) {
        this.name = name;
        this.number = number;
    }
    //Generate getter and setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
