package com.example.nick.wordsgame;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

public class WordsList extends AppCompatActivity {

    private EditText wordEditText;
    private Button addButton;
    private SwipeMenuListView wordList;
    private TextView wordsCountEditText;

    DBHelper dbHelper = new DBHelper(this);

    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> allWords = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list);
        wordEditText = findViewById(R.id.word_edittext);
        addButton = findViewById(R.id.add_button);
        wordList = findViewById(R.id.wordList);
        wordsCountEditText = findViewById(R.id.words_count_edittext);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(R.drawable.delete_word_button_style);
                // set item width
                deleteItem.setWidth(100);
                // set a icon
                deleteItem.setIcon(R.drawable.delete_word);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        wordList.setMenuCreator(creator);


        wordList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        dbHelper.deleteWord(idList.get((position)));
                        Toast.makeText(getApplicationContext(), "Word deleted",
                                Toast.LENGTH_SHORT).show();
                        updateListViewData();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        updateListViewData();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!wordEditText.getText().toString().equals("") &&
                        !allWords.contains(wordEditText.getText().toString())){
                    dbHelper.insertData(wordEditText.getText().toString());
                    Toast.makeText(getApplicationContext(), "Word added",
                            Toast.LENGTH_SHORT).show();
                    updateListViewData();
                } else if(wordEditText.getText().toString().isEmpty())
                        Toast.makeText(getApplicationContext(), "Enter Word",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Word already exists",
                            Toast.LENGTH_SHORT).show();
                }
        });



//        wordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                dbHelper.deleteWord(idList.get((position)));
//                Toast.makeText(getApplicationContext(), "Word deleted",
//                        Toast.LENGTH_SHORT).show();
//                updateListViewData();
//            }
//        });

    }



    @SuppressLint("SetTextI18n")
    private void updateListViewData() {

        allWords = dbHelper.getAllWords();
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, R.layout.list_black_text,R.id.list_content,
                        allWords);

        wordList.setAdapter(arrayAdapter);
        wordEditText.getText().clear();
        idList = dbHelper.getAllID();
        wordsCountEditText.setText(dbHelper.getNumberOfWords()+" Words");


    }
}
