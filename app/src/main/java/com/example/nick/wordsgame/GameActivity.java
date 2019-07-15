package com.example.nick.wordsgame;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static boolean team1IsWinner = false;
    public static boolean team2IsWinner = false;
    private boolean isteam1Turn = true;

    public TextView team1Score;
    public TextView team2Score;
    private TextView word;
    private Button falseButton;
    private Button trueButton;


    private long START_TIME_IN_MILLIS = Integer.parseInt(GameProperties.getTime()) * 1000;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;

    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MediaPlayer mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.true_sound);
        setContentView(R.layout.activity_game);
        TextView team1Name = findViewById(R.id.team1_Score);
        TextView team2Name = findViewById(R.id.team2_score);
        team1Score = findViewById(R.id.team1_Score_number);
        team2Score = findViewById(R.id.team2_Score_number);
        falseButton = findViewById(R.id.false_button);
        trueButton = findViewById(R.id.true_button);
        word = findViewById(R.id.word);
        team1Name.setText(GameProperties.getTeam1Name());
        team2Name.setText(GameProperties.getTeam2Name());


        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause);


        mTextViewCountDown.setText(GameProperties.getTime());
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWord();
                startTimer();
            }
        });

        updateCountDownText();

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWord();
            }
        });
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
                mediaPlayer.start();
                setScore();
                changeWord();
            }
        });
    }


    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                mButtonStartPause.setVisibility(View.INVISIBLE);
                falseButton.setVisibility(View.VISIBLE);
                trueButton.setVisibility(View.VISIBLE);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mButtonStartPause.setVisibility(View.VISIBLE);
                falseButton.setVisibility(View.INVISIBLE);
                trueButton.setVisibility(View.INVISIBLE);


                resetTimer();

                isteam1Turn = !isteam1Turn;

                if (!team1IsWinner && !team2IsWinner) {
                    AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                    alertDialog.setTitle("");
                    if (isteam1Turn) {
                        alertDialog.setMessage(GameProperties.getTeam1Name() + " turn !");
                    } else {
                        alertDialog.setMessage(GameProperties.getTeam2Name() + " turn !");
                    }

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(GameActivity.this, MainActivity.class));
                        }
                    });
                    alertDialog.show();
                }

                guessWinner();
            }
        }.start();


    }


    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }

    @SuppressLint("SetTextI18n")
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        if (mTimeLeftInMillis / 1000 >= 60) {
            String timeLeftFormatted = String.format(Locale.getDefault(), "%2d:%02d", minutes, seconds);
            mTextViewCountDown.setText(timeLeftFormatted);

        } else {
            String timeLeftFormatted = String.format(Locale.getDefault(), "%2d", seconds);
            mTextViewCountDown.setText("   " + timeLeftFormatted);

        }

    }

    private void setScore() {
        if (isteam1Turn) {
            team1Score.setText(String.valueOf(Integer.parseInt(team1Score.getText().toString()) + 1));

        } else {
            team2Score.setText(String.valueOf(Integer.parseInt(team2Score.getText().toString()) + 1));

        }

    }

    private void changeWord() {
        Random random = new Random();
        int randInt = random.nextInt(GameProperties.words.length);
        word.setText(GameProperties.words[randInt]);

    }

    private void guessWinner() {
        if (Integer.parseInt(GameProperties.getScore()) <= Integer.parseInt(team1Score.getText().toString())
                && isteam1Turn
                && Integer.parseInt(team1Score.getText().toString()) > Integer.parseInt(team2Score.getText().toString())) {
            team1IsWinner = true;
        } else if (Integer.parseInt(GameProperties.getScore()) <= Integer.parseInt(team2Score.getText().toString())
                && Integer.parseInt(team2Score.getText().toString()) > Integer.parseInt(team1Score.getText().toString())) {
            team2IsWinner = true;

        }

        if (team1IsWinner || team2IsWinner) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Winner");
            alert.setCancelable(false);
            if (team1IsWinner) {
                alert.setMessage(GameProperties.getTeam1Name());
            } else
                alert.setMessage(GameProperties.getTeam2Name());

            alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.this.finish();
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.finish();
                }
            });
            alert.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(GameActivity.this, MainActivity.class));
                }
            });
            alert.show();

            team1IsWinner = false;
            team2IsWinner = false;
        }
    }
}

