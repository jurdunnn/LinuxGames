package com.example.linuxgames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;

import com.example.linuxgames.activities.gameDetails;

public class MainActivity extends AppCompatActivity {

    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init search box
        searchBox = findViewById(R.id.searchBox);
        //set search as enter button
        searchBox.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
        //add listener for enter button
        searchBox.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                Intent intent = new Intent(this, gameDetails.class);
                intent.putExtra("query", searchBox.getText().toString());
                startActivity(intent);
            }
            return false;
        });
    }
}