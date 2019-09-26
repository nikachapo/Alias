package com.example.nick.wordsgame;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    DBHelper dbHelper = new DBHelper(this);
    ArrayList<String> wordsArrayList = new ArrayList<>();

    public static boolean team1IsWinner = false;
    public static boolean team2IsWinner = false;
    private boolean isteam1Turn = true;


    public TextView team1ScoreTextView, team2ScoreTextView;
    private TextView word;
    private Button falseButton, trueButton;


    private long START_TIME_IN_MILLIS = Integer.parseInt(GameProperties.getTime()) * 1000;
    private TextView mTextViewCountDown;
    private Button mButtonStart;

    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordsArrayList = dbHelper.getAllWords();
        final MediaPlayer mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.true_sound);
        setContentView(R.layout.activity_game);
        TextView team1Name = findViewById(R.id.team1_Score);
        TextView team2Name = findViewById(R.id.team2_score);
        team1ScoreTextView = findViewById(R.id.team1_Score_number);
        team2ScoreTextView = findViewById(R.id.team2_Score_number);
        falseButton = findViewById(R.id.false_button);
        trueButton = findViewById(R.id.true_button);
        word = findViewById(R.id.word);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonStart = findViewById(R.id.button_start_pause);

        team1Name.setText(GameProperties.getTeam1Name());
        team2Name.setText(GameProperties.getTeam2Name());
        mTextViewCountDown.setText(GameProperties.getTime());

        mButtonStart.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onStop() {
        super.onStop();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                mTimeLeftInMillis = millisUntilFinished;
                mButtonStart.setVisibility(View.INVISIBLE);
                falseButton.setVisibility(View.VISIBLE);
                trueButton.setVisibility(View.VISIBLE);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mButtonStart.setVisibility(View.VISIBLE);
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
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Restart",
                            new DialogInterface.OnClickListener() {
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
            team1ScoreTextView.setText(String.valueOf
                    (Integer.parseInt(team1ScoreTextView.getText().toString()) + 1));

        } else {
            team2ScoreTextView.setText(String.valueOf
                    (Integer.parseInt(team2ScoreTextView.getText().toString()) + 1));

        }

    }


    private void changeWord() {
        if (wordsArrayList.size() == 0) {
            wordsArrayList = dbHelper.getAllWords();
        }
        Random random = new Random();
        int randInt = random.nextInt(wordsArrayList.size());
        word.setText(wordsArrayList.get(randInt));
        wordsArrayList.remove(randInt);


    }

    private void guessWinner() {
        if (Integer.parseInt(GameProperties.getScore()) <=
                Integer.parseInt(team1ScoreTextView.getText().toString())
                && isteam1Turn
                && Integer.parseInt(team1ScoreTextView.getText().toString()) >
                Integer.parseInt(team2ScoreTextView.getText().toString())) {
            team1IsWinner = true;
        } else if (Integer.parseInt(GameProperties.getScore()) <=
                Integer.parseInt(team2ScoreTextView.getText().toString())
                && Integer.parseInt(team2ScoreTextView.getText().toString()) >
                Integer.parseInt(team1ScoreTextView.getText().toString())) {
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
                    finish();
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

