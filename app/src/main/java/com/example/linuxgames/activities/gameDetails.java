package com.example.linuxgames.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.linuxgames.R;
import com.example.linuxgames.igdb.igdb;
import com.example.linuxgames.wine.wineResultsCount;

import java.util.concurrent.ExecutionException;

import jp.wasabeef.blurry.Blurry;

public class gameDetails extends AppCompatActivity {
    TextView log;
    String query;

    String titleAndUrl;
    String title;
    String url;

    //threads
    Thread1 t1 = new Thread1();
    Thread2 t2 = new Thread2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //get log
        log = findViewById(R.id.logText);

        //get extra
        query = getIntent().getStringExtra("query");
        //runnable to being after view has been loaded

        //start thread1 - this begins the search for app data.
        t1.start();

    }

    //thread 2 background for searching for repositories of script applications to play windows games.
    public class Thread2 extends Thread {
        public void run() {
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

            //inform user of wine results that have been found
            //todo: print rating with wine
            int finalCount = Integer.parseInt(count);
            if(finalCount > 0) {
                runOnUiThread(() -> log.append("\nWine - Yes"));
            } else {
                runOnUiThread(() -> log.append("\nWine - No"));
            }

            //todo: search for proton

            //try
                //new protonSearch(wineQuery).execute().get();

            //print rating with proton
        }
    }

    //thread 1 for found game data.
    public class Thread1 extends Thread {
        public void run() {
            //TODO: if native linux then stop thread 2.
            //inform user search is underway
            runOnUiThread(() -> log.append("\nSearching for: " + query));

            //try and find a game matching the search query in igdb
            try {
                titleAndUrl = new igdb(query).execute().get();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            //split tile and url up into seperate variables
            url = titleAndUrl.split("~")[0];
            title = titleAndUrl.split("~")[1];

            //inform user that game has been found
            runOnUiThread(() -> log.append("\nFound: " + title));

            //start thread2
            t2.start();

            //try to join thread 2
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //hide progress bar
            runOnUiThread(() -> hideProgress());
        }
    }

    //misc methods
    private void hideProgress() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }
}