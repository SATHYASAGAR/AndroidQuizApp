package com.example.esha.quizapp;

/**
 * Created by esha on 5/4/17.
 */

public class ClassStatus {
    String name;
    Boolean studentAdded;
    String dateRequested;
    String dateAddedOrRejected;

    public ClassStatus() {

    }

    public ClassStatus(String nameToPut, Boolean studentAddedToPut, String dateAddedOrRejectedToPut) {
        name = nameToPut;
        studentAdded = studentAddedToPut;
        dateAddedOrRejected = dateAddedOrRejectedToPut;
    }

    public ClassStatus(String nameToPut, Boolean studentAddedToPut, String dateRequestedToPut, String dateAddedOrRejectedToPut) {
        name = nameToPut;
        studentAdded = studentAddedToPut;
        dateRequested = dateRequestedToPut;
        dateAddedOrRejected = dateAddedOrRejectedToPut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStudentAdded() {
        return studentAdded;
    }

    public void setStudentAdded(Boolean studentAdded) {
        this.studentAdded = studentAdded;
    }

    public String getDateRequested() {
        return dateRequested;
    }

    public void setdateRequested(String dateRequested) {
        this.dateRequested = dateRequested;
    }

    public String getdateAddedOrRejected() {
        return dateAddedOrRejected;
    }

    public void setdateAddedOrRejected(String dateAddedOrRejected) {
        dateAddedOrRejected = dateAddedOrRejected;
    }
}
