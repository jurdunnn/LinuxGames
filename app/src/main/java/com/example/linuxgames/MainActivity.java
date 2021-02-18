package com.example.linuxgames;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.linuxgames.igdb.igdbSearch;
import com.example.linuxgames.steam.steamSearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    String steamPageUrl;
    String query;

    //threads
    networkThread network;
    steamNetworkThread steamThread;

    Document document;

    steamPageDoc globalDocument;

    ConstraintLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        EditText searchBox = findViewById(R.id.searchBox);
        ImageView clearSearchButton = findViewById(R.id.clearSearchButton);
        loadingLayout = findViewById(R.id.loadingLayout);



        //get instance of global document
        globalDocument = steamPageDoc.getInstance();

        //add listener for enter button
        searchBox.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                //show loading layout
                loadingLayout.setVisibility(View.VISIBLE);

                //get query text
                query = searchBox.getText().toString();

                //start main thread
                network = new networkThread();
                network.start();
                try {
                    //once thread is complete, join.
                    network.join();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(steamPageUrl != null) {
                    Intent intent = new Intent(this, gameDetails.class);
                    intent.putExtra("steamurl", steamPageUrl);
                    //start the game page activity.
                    startActivity(intent);

                    //animations for loading activity
                    overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                    return true;
                } else {
                    Toast.makeText(this, "Game not found", Toast.LENGTH_SHORT).show();
                    loadingLayout.setVisibility(GONE);
                }
            }
            return false;
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchBox.getText().length() > 0) {
                    clearSearchButton.setVisibility(View.VISIBLE);
                } else {
                    clearSearchButton.setVisibility(GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        clearSearchButton.setOnClickListener(v -> searchBox.setText(""));
    }

    public class networkThread extends Thread {
        public void run() {
            //search for game on steam and get url
            steamThread = new steamNetworkThread();
            steamThread.start();
            try {
                steamThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class steamNetworkThread extends Thread {
        public void run() {
            try {
                steamPageUrl = new steamSearch(query).execute().get();
                if (steamPageUrl != null) {
                    steamGameData();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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

                globalDocument.setDocument(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //hide loading layout
        loadingLayout.setVisibility(GONE);
    }
}