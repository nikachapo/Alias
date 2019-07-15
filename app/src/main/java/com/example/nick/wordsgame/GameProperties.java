package com.example.nick.wordsgame;

import java.util.ArrayList;

public class GameProperties {
    private static String team1Name;
    private static String team2Name;
    private static String time;
    private static String score;

    static String [] words= {"სკამი","მაგიდა","ჭერი","დანა","ღრუბელი","წვიმა"};




    static String getTeam1Name() {
        return team1Name;
    }

    void setTeam1Name(String team1Name) {
        GameProperties.team1Name = team1Name;
    }

    static String getTeam2Name() {
        return team2Name;
    }

    void setTeam2Name(String team2Name) {
        GameProperties.team2Name = team2Name;
    }

    public static String getTime() {
        return time;
    }

    public void setTime(String time) {
        GameProperties.time = time;
    }

    public static String getScore() {
        return score;
    }

    public void setScore(String score) {
        GameProperties.score = score;
    }
}
