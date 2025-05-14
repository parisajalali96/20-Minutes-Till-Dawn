package io.github.parisajalali96.Models;

public class User {
    private String username;
    private String password;
    private String SQ;
    private String SA;
    private int totalScore;
    private int longestSurvivalScore;
    private int numOfKills;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public int getNumOfKills() {
        return numOfKills;
    }
    public void addNumOfKills(int numOfKills) {
        this.numOfKills += numOfKills;
    }
    public int getLongestSurvivalScore() {
        return longestSurvivalScore;
    }

    public void setLongestSurvivalScore(int longestSurvivalScore) {
        this.longestSurvivalScore = longestSurvivalScore;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSA() {
        return SA;
    }
    public void setSA(String SA) {
        this.SA = SA;
    }
    public String getSQ() {
        return SQ;
    }
    public void setSQ(String SQ) {
        this.SQ = SQ;
    }
    public void addTotalScore(int score) {
        totalScore += score;
    }
    public int getTotalScore() {
        return totalScore;
    }

}
