package com.example.esha.quizapp;

import java.util.List;

public class Questions {
    String question;
    String choice1;
    String choice2;
    String choice3;
    String choice4;
    String correctAnswer;

    // List<String> choices;
    public Questions() {

    }

    public Questions(String questionToPut, String choice1ToPut, String choice2ToPut, String choice3ToPut, String choice4ToPut, String answerToPut) {
        question = questionToPut;
        choice1 = choice1ToPut;
        choice2 = choice2ToPut;
        choice3 = choice3ToPut;
        choice4 = choice4ToPut;
        correctAnswer = answerToPut;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

}
