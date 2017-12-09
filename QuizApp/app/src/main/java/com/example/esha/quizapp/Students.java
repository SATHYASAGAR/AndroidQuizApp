package com.example.esha.quizapp;

/**
 * Created by esha on 4/28/17.
 */

public class Students {
    String name;
    int Redid;
    int year;
    String major;

    public Students() {

    }

    public Students(String nameToPut, int redidToPut, int yearToPut, String majorToPut, String emailToPut, String passwordToPut) {
        name = nameToPut;
        Redid = redidToPut;
        year = yearToPut;
        major = majorToPut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRedid() {
        return Redid;
    }

    public void setRedid(int redid) {
        Redid = redid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
