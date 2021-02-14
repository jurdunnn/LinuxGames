package com.example.linuxgames.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.linuxgames.R;
import com.example.linuxgames.igdb.igdbSearch;
import com.example.linuxgames.lutris.lutrisSearch;
import com.example.linuxgames.lutris.lutrisPage;
import com.example.linuxgames.proton.protonPage;
import com.example.linuxgames.steam.steamSearch;
import com.example.linuxgames.wine.winePage;
import com.example.linuxgames.wine.wineSearch;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class gameDetails extends AppCompatActivity {
    //terminal log
    TextView log;

    //user query
    String query;

    //split refined game title
    String title;

    //igdb url
    String igdbPageUrl;

    //steam
    String steamPageUrl;
    String steamAppId;

    //ratings
    String wineRating, protonRating, lutrisRating;

    //native to linux check
    Boolean linuxNative;

    //threads
    mainNetworkThread mainThread = new mainNetworkThread(); //gets the game database data
    wineThread wine = new wineThread(); //get rating from wine
    protonThread proton = new protonThread(); //get rating from proton
    lutrisThread lutrist = new lutrisThread(); //get rating from game on linux

    //progress bars
    //wine
    ProgressBar wineProgressBar;
    TextView wineProgressText;
    int wineProgress = 0;
    //proton
    ProgressBar protonProgressBar;
    TextView protonProgressText;
    int protonProgress = 0;
    //Lutris

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_details);

        //get log
        log = findViewById(R.id.logText);

        //wine progress
        wineProgressBar = findViewById(R.id.wineProgressBar);
        wineProgressText = findViewById(R.id.wineProgressText);

        //proton progress
        protonProgressBar = findViewById(R.id.protonProgressBar);
        protonProgressText = findViewById(R.id.protonProgressText);

        //init native linux bool
        linuxNative = false;

        //get extra
        query = getIntent().getStringExtra("query");

        //start thread1 - this begins the search for app data.
        mainThread.start();

    }

    //thread 2 for searching wine
    public class wineThread extends Thread {
        public void run() {
            //if the title contains a date, remove the date
            String wineQuery = title.split("\\(")[0];
            //todo: if title is null then user original query

            //try to get wine game page, and get the rating
            try {
                String wineUrl = new wineSearch(wineQuery).execute().get();//search wine
                //if wine has found a likely url
                if (wineUrl != null) {

                    //got the wine url update the progress bar
                    updateWineProgressBar(50);

                    //local variable rating
                    String rating = new winePage(wineUrl).execute().get();

                    //got the rating update progress bar
                    updateWineProgressBar(25);

                    //global rating variable
                    wineRating = rating;

                    //prompt rating
                    runOnUiThread(() -> log.append("\nWine rating - " + wineRating));
                    runOnUiThread(() -> wineProgressText.setText(wineRating));
                } else {
                    //prompt did not find game page.
                    runOnUiThread(() -> log.append("\nWine - No"));

                    //indicate that the progress has halted.
                    wineProgress = 0;
                    updateWineProgressBar(0);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                //prompt error with getting wine data.
                runOnUiThread(() -> log.append("\nUnable to access wine"));
            }
        }
    }

    public class protonThread extends Thread {
        public void run() {
            try {
                updateProtonProgress(25);
                String rating = new protonPage(steamAppId).execute().get();//search proton

                //if wine has found a likely url
                if (rating != null) {
                    protonRating = rating;
                    updateProtonProgress(50);
                    //prompt rating
                    runOnUiThread(() -> log.append("\nProton rating - " + protonRating));
                    updateProtonProgress(25);
                    runOnUiThread(() -> protonProgressText.setText(protonRating));
                } else {
                    //prompt did not find game page.
                    protonProgress = 0;
                    updateProtonProgress(0);
                    runOnUiThread(() -> log.append("\nProton - No"));
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                //prompt error with getting wine data.
                runOnUiThread(() -> log.append("\nUnable to access proton"));
            }
        }
    }

    public class lutrisThread extends Thread {
        public void run() {
            try {
                String lutrisUrl = new lutrisSearch(title).execute().get();//search wine
                Log.i("lutris url", "LUTRIS URL" + lutrisUrl);
                //if wine has found a likely url
                if (lutrisUrl != null) {
                    lutrisRating = new lutrisPage(lutrisUrl).execute().get();
                    //got the wine url update the progress bar
                    //updateWineProgressBar(50);

                    //local variable rating
                    //String rating = new winePage(lutrisUrl).execute().get();

                    //got the rating update progress bar
                    //updateWineProgressBar(25);

                    //global rating variable
                    //wineRating = rating;

                    //prompt rating
                    //runOnUiThread(() -> log.append("\nWine rating - " + wineRating));
                    //runOnUiThread(() -> wineProgressText.setText(wineRating));
                } else {
                    //prompt did not find game page.
                    //runOnUiThread(() -> log.append("\nWine - No"));

                    //indicate that the progress has halted.
                    //wineProgress = 0;
                    //updateWineProgressBar(0);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                //prompt error with getting wine data.
                //runOnUiThread(() -> log.append("\nUnable to access wine"));
            }
        }
    }

    //thread 1 for found game data.
    public class mainNetworkThread extends Thread {
        Document document;

        public void run() {
            //inform user search is underway
            runOnUiThread(() -> log.append("\nSearching for: " + query));

            //try and find a game matching the search query in steam
            try {
                //search for game on steam and get url
                steamPageUrl = new steamSearch(query).execute().get();
                //if failed to get steam url use igdb instead
                if (steamPageUrl == null) {
                    igdbPageUrl = new igdbSearch(query).execute().get();
                    runOnUiThread(() -> log.append("\nNo such game on steam..."));
                }

            } catch (ExecutionException | InterruptedException e) {
                //inform user of error
                runOnUiThread(() -> log.append("\nUnable to access igdb"));
                e.printStackTrace();
            }

            //if steam url has not successfully been retrieved then try idgb
            //if that also failed, inform the user.
            if (steamPageUrl != null) {
                steamGameData(); //get game data from steam

                //prompt found game
                runOnUiThread(() -> log.append("\nFound: " + title));

                //run threads if it is not a linux native
                if (linuxNative.equals(false)) {
                    //PUT THREAD START IN HERE.
                    wine.start();
                    proton.start();
                    lutrist.start();
                } else {
                    runOnUiThread(() -> log.append("\nNative Linux Game"));
                }

                populateUISteam(); //populate steam UI
            } else if (igdbPageUrl != null) {
                igdbGameData();
                wine.start();
                proton.start();
                lutrist.start();
                populateUIIgdb();
            } else {
                runOnUiThread(() -> log.append("\nNo such game found anywhere!"));
            }

            //try to join the threads
            if (steamPageUrl != null || igdbPageUrl != null) {
                //try to join thread 2
                try {
                    wine.join();
                    proton.join();
                    lutrist.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //hide progress bar
            runOnUiThread(gameDetails.this::hideProgress);
        }

        //get the game data from the steam page
        private void steamGameData() {
            OkHttpClient client = new OkHttpClient();
            //build the request
            Request request = new Request.Builder()
                    .url(steamPageUrl) // The URL to send the data to
                    .build();

            //post and get response
            Response response = null;

            //get web page document
            document = null;
            try {
                response = client.newCall(request).execute(); //execute to get response
                document = Jsoup.parse(response.body().string(), steamPageUrl); //parse webpage into document
            } catch (IOException e) {
                e.printStackTrace();
            }

            title = document.select("div.apphub_AppName").text();

            if (document.select("div.sysreq_tabs").text().contains("Linux")) {
                linuxNative = true;
            }

            //get app id
            steamAppId = steamPageUrl.split("app/")[1].split("/")[0];
            Log.i("steam", "steam app id " + steamAppId);
        }

        //get the game data from the igdb page
        private void igdbGameData() {
            OkHttpClient client = new OkHttpClient();
            //build the request
            Request request = new Request.Builder()
                    .url(igdbPageUrl) // The URL to send the data to
                    .build();

            //post and get response
            Response response = null;

            //get web page document
            document = null;
            try {
                response = client.newCall(request).execute(); //execute to get response
                document = Jsoup.parse(response.body().string(), igdbPageUrl); //parse webpage into
            } catch (IOException e) {
                e.printStackTrace();
            }
            title = document.title();
        }

        //populate the UI whilst the other threads are working

        //populate using idgb
        private void populateUIIgdb() {
        }

        //populate using steam
        private void populateUISteam() {
            //elements on screen
            TextView titleText = findViewById(R.id.titleText);
            TextView dateText = findViewById(R.id.dateText);
            TextView authorText = findViewById(R.id.authorText);

            ImageView backgroundImage = findViewById(R.id.loadingWallpaper);

            //run changes in UI thread.
            runOnUiThread(() -> {
                //set title
                try {
                    titleText.setText(title);
                    dateText.setText(document.select("div.date").text());
                    authorText.setText(document.select("div.dev_row").select("a").first().text());
                    //get image container
                    String imageUrl = document.select("div.screenshot_holder").select("a.highlight_screenshot_link").attr("href");
                    Picasso.get().load(imageUrl).fit().centerCrop().into(backgroundImage);
                } catch (Exception e) {
                    runOnUiThread(() -> log.append("\nError retrieving steam data..."));
                }
            });
        }
    }

    //misc methods
    private void hideProgress() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    private void updateWineProgressBar(int progress) {
        wineProgress += progress;
        String wineProgressStr = wineProgress + "%";
        runOnUiThread(() -> wineProgressText.setText(wineProgressStr));
        wineProgressBar.setProgress(wineProgress);
    }
    private void updateProtonProgress(int progress) {
        protonProgress += progress;
        String protonProgressStr = protonProgress + "%";
        runOnUiThread(() -> protonProgressText.setText(protonProgressStr));
        protonProgressBar.setProgress(protonProgress);
    }

}