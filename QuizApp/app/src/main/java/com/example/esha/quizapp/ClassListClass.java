package com.example.esha.quizapp;

import java.util.Date;

/**
 * Created by esha on 5/2/17.
 */

public class ClassListClass {
    public ClassListClass() {

    }

    public Boolean getRequestaccepted() {
        return requestaccepted;
    }

    public void setRequestaccepted(Boolean requestaccepted) {
        this.requestaccepted = requestaccepted;
    }

    public Boolean getRequestRejected() {
        return requestRejected;
    }

    public void setRequestRejected(Boolean requestRejected) {
        this.requestRejected = requestRejected;
    }


    Boolean requestaccepted;
    Boolean requestRejected;

    public String getAcceptRejectDate() {
        return acceptRejectDate;
    }

    public void setAcceptRejectDate(String acceptRejectDate) {
        this.acceptRejectDate = acceptRejectDate;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    String acceptRejectDate;
    String requestDate;

    public String getDateClassFormed() {
        return dateClassFormed;
    }

    public ClassListClass(Boolean requestacceptedToPut, Boolean requestRejectedToPut, String acceptRejectDateToPut, String requestDateToPut) {
        requestaccepted = requestacceptedToPut;
        requestRejected = requestRejectedToPut;
        acceptRejectDate = acceptRejectDateToPut;
        requestDate = requestDateToPut;
    }

    public void setDateClassFormed(String dateClassFormed) {
        this.dateClassFormed = dateClassFormed;
    }

    String dateClassFormed;

}
