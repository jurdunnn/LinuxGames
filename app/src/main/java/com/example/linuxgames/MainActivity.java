package com.example.linuxgames;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

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
                Log.i("SEARCH", "onKey: enter key pressed");

                //search wine
                new searchWine(searchBox.getText().toString()).execute();
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