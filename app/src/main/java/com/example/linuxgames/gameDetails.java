package com.example.linuxgames;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.linuxgames.lutris.lutrisPage;
import com.example.linuxgames.lutris.lutrisSearch;
import com.example.linuxgames.misc.steamPageDoc;
import com.example.linuxgames.proton.protonPage;
import com.example.linuxgames.wine.winePage;
import com.example.linuxgames.wine.wineSearch;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

public class gameDetails extends AppCompatActivity {
    //terminal log
    TextView log;

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
    ProgressBar lutrisProgressBar;
    TextView lutrisProgressText;
    int lutrisProgress = 0;

    steamPageDoc globalDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_details);

        Slidr.attach(this);
        steamPageUrl = getIntent().getStringExtra("steamurl");
        //get log
        log = findViewById(R.id.logText);

        //wine progress
        wineProgressBar = findViewById(R.id.wineProgressBar);
        wineProgressText = findViewById(R.id.wineProgressText);

        //proton progress
        protonProgressBar = findViewById(R.id.protonProgressBar);
        protonProgressText = findViewById(R.id.protonProgressText);

        //lutris progress
        lutrisProgressBar = findViewById(R.id.lutrisProgressBar);
        lutrisProgressText = findViewById(R.id.lutrisProgressText);

        //init native linux bool
        linuxNative = false;

        //get the game data document.
        globalDocument = steamPageDoc.getInstance();

        if (globalDocument != null) {
            populateUI();
            mainThread.start();
        }
    }

    private void populateUI() {
        try {
            title = globalDocument.getDocument().select("div.apphub_AppName").text();

            TextView titleText = findViewById(R.id.titleText);
            titleText.setText(title);

            TextView dateText = findViewById(R.id.dateText);
            dateText.setText(globalDocument.getDocument().select("div.date").text());

            TextView authorText = findViewById(R.id.authorText);
            authorText.setText(globalDocument.getDocument().select("div.dev_row").select("a").first().text());

            ImageView background = findViewById(R.id.loadingWallpaper);
            String imageUrl = globalDocument.getDocument().select("div.screenshot_holder").select("a.highlight_screenshot_link").attr("href");
            Log.i("Image", "populateUISteam: " + imageUrl);
            Picasso.get().load(imageUrl).fit().centerCrop().into(background);

            //game details - hidden
            if (globalDocument.getDocument().select("div.sysreq_tabs").text().contains("Linux")) {
                linuxNative = true;
            }

            //get app id
            steamAppId = steamPageUrl.split("app/")[1].split("/")[0];
        } catch (RuntimeException e) {
            Toast.makeText(this, "Steam age check block", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //separate threads for different sites
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
                    runOnUiThread(() -> log.append("\nWine - Failed"));

                    //indicate that the progress has halted.
                    wineProgress = 100;
                    updateWineProgressBar(0);
                    runOnUiThread(() -> wineProgressText.setText("Garbage"));
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
                    protonProgress = 100;
                    updateProtonProgress(0);
                    runOnUiThread(() -> protonProgressText.append("\nGarbage"));
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
                //update progress
                updateLutrisProgress(25);

                //search lutris
                String lutrisUrl = new lutrisSearch(title).execute().get();

                //update progress
                updateLutrisProgress(25);

                //if lutris has found a likely url
                if (lutrisUrl != null) {
                    //get lutris ratings
                    lutrisRating = new lutrisPage(lutrisUrl).execute().get();

                    //update progress
                    updateLutrisProgress(50);

                    //prompt rating
                    runOnUiThread(() -> log.append("\nLutris rating - " + lutrisRating)); //terminal
                    runOnUiThread(() -> lutrisProgressText.setText(lutrisRating)); //UI
                } else {

                    //prompt did not find game page.
                    runOnUiThread(() -> lutrisProgressText.append("\nGarbage"));

                    //indicate that the progress has halted.
                    lutrisProgress = 100;
                    updateLutrisProgress(0);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();

                //prompt error with getting wine data.
                runOnUiThread(() -> log.append("\nUnable to access lutris"));
            }
        }
    }

    //thread 1 for found game data.
    public class mainNetworkThread extends Thread {
        public void run() {
            //if steam url has not successfully been retrieved then try idgb
            //if that also failed, inform the user.

            //run threads if it is not a linux native
            if (steamPageUrl != null || igdbPageUrl != null) {
                if (linuxNative.equals(false)) {
                    //PUT THREAD START IN HERE.
                    wine.start();
                    proton.start();
                    lutrist.start();
                } else {
                    runOnUiThread(() -> log.append("\nNative Linux Game"));
                }
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

    private void updateLutrisProgress(int progress) {
        lutrisProgress += progress;
        String lutrisProgressStr = lutrisProgress + "%";
        runOnUiThread(() -> lutrisProgressText.setText(lutrisProgressStr));
        lutrisProgressBar.setProgress(lutrisProgress);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}