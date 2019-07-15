package com.example.nick.wordsgame;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText team1Name;
    private EditText team2Name;
    private Spinner time;
    private Spinner score;
    private Button startGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        team1Name = findViewById(R.id.team1);
        team2Name = findViewById(R.id.team2);
        time = findViewById(R.id.times);
        score = findViewById(R.id.score);
        startGame = findViewById(R.id.start_game);


        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameProperties gameProperties = new GameProperties();
                gameProperties.setTeam1Name(team1Name.getText().toString());
                gameProperties.setTeam2Name(team2Name.getText().toString());
                gameProperties.setTime(String.valueOf(time.getSelectedItem()));
                gameProperties.setScore(String.valueOf(score.getSelectedItem()));

                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });




    }
}

