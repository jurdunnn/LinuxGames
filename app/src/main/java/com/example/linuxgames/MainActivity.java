package com.example.linuxgames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.linuxgames.activities.loadingActivity;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity {

    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //blur wallpaper
        blurWallpaper();

        //init search box
        searchBox = findViewById(R.id.searchBox);
        //set search as enter button
        searchBox.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
        //add listener for enter button
        searchBox.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                Intent intent = new Intent(this, loadingActivity.class);
                intent.putExtra("query", searchBox.getText().toString());
                startActivity(intent);
            }
            return false;
        });
    }

    private void blurWallpaper() {
        //get image view
        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        //convert wallpaper drawable to bitmap
        Bitmap wallpaper = BitmapFactory.decodeResource(this.getResources(), R.drawable.wallpaper);
        //blur wallpaper
        Blurry.with(this).
                from(wallpaper).
                into(backgroundImage);
    }
}