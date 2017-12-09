package com.example.esha.quizapp;

/**
 * Created by esha on 4/30/17.
 */

public class AvailableTest {
    private String dateScheduled;
    private int score;

    public String getDateScheduled() {
        return dateScheduled;
    }

    public AvailableTest(String dateScheduledInput, int scoreInput, Boolean testTakenInput) {
        dateScheduled = dateScheduledInput;
        score = scoreInput;
        testTaken = testTakenInput;
    }

    public void setDateScheduled(String dateScheduled) {
        this.dateScheduled = dateScheduled;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Boolean getTestTaken() {
        return testTaken;
    }

    public void setTestTaken(Boolean testTaken) {
        this.testTaken = testTaken;
    }

    private Boolean testTaken;

    public AvailableTest() {

    }
}
