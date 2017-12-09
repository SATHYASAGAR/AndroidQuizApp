package com.example.esha.quizapp;

/**
 * Created by esha on 5/4/17.
 */

public class ProfessorDetails {
    public ProfessorDetails() {
    }

    public String getNameOfProfessor() {
        return nameOfProfessor;
    }

    public ProfessorDetails(String nameToPut) {
        nameOfProfessor = nameToPut;
    }

    public void setNameOfProfessor(String nameOfProfessor) {
        this.nameOfProfessor = nameOfProfessor;
    }

    String nameOfProfessor;
}
