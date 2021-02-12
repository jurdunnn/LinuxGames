package com.example.linuxgames.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.linuxgames.R;
import com.example.linuxgames.igdb.igdb;
import com.example.linuxgames.wine.wineResultsCount;

import java.util.concurrent.ExecutionException;

import jp.wasabeef.blurry.Blurry;

public class loadingActivity extends AppCompatActivity {
    TextView log;
    String query;

    String title;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //blur the background
        blurWallpaper();

        //get log
        log = findViewById(R.id.logText);

        //get extra
        query = getIntent().getStringExtra("query");
        //runnable to being after view has been loaded
        log.post(this::igdbTasks);
        log.post(this::wineTasks);

    }


    private void wineTasks() {
        //Declare count
        String count = null;

        //get more refined search from idgb
        String wineQuery = title.split("\\(")[0];

        //try to get number of results from wine
        try {
            count = new wineResultsCount(wineQuery).execute().get();//search wine
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        //convert string to int
        int countInt = Integer.parseInt(count);

        //log the results
        log.append("\nWine found: " + count + " result");
    }

    private void igdbTasks() {
        String titleAndUrl = null;

        //get title and url from igdb
        try {
           titleAndUrl = new igdb(query).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        //split tile and url up into seperate variables
        url = titleAndUrl.split("~")[0];
        title = titleAndUrl.split("~")[1];

        //display in log
        log.append("Searching for " + title);
        log.append("\nFetching meta data from " + url);
    }

    private void blurWallpaper() {
        //get image view
        ImageView backgroundImage = findViewById(R.id.loadingWallpaper);
        //convert wallpaper drawable to bitmap
        Bitmap wallpaper = BitmapFactory.decodeResource(this.getResources(), R.drawable.wallpaper);
        //blur wallpaper
        Blurry.with(this).
                from(wallpaper).
                into(backgroundImage);
    }
}