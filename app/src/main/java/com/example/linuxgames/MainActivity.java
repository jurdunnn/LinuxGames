package com.example.linuxgames;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;

import com.example.linuxgames.misc.steamPageDoc;
import com.example.linuxgames.steam.steamSearch;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    String steamPageUrl;
    String query;

    steamPageDoc globalDocument;

    MutableLiveData<String> backgroundData;
    ConstraintLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        EditText searchBox = findViewById(R.id.searchBox);
        ImageView clearSearchButton = findViewById(R.id.clearSearchButton);
        loadingLayout = findViewById(R.id.loadingLayout);

        backgroundData = new MutableLiveData<>();

        //get instance of global document
        globalDocument = steamPageDoc.getInstance();

        //add listener for enter button
        searchBox.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                //get query text
                query = searchBox.getText().toString();

                new task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                return true;
            }
            return false;
        });

        //wait for background search to be complete before moving to new activity.
        backgroundData.observe(this, s -> {
            //start new activity
            Intent intent = new Intent(MainActivity.this, gameDetails.class);
            intent.putExtra("steamurl", steamPageUrl);

            startActivity(intent);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        });

        //show or hide clear search button depending on searchBox text length
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

        //clear search field.
        clearSearchButton.setOnClickListener(v -> searchBox.setText(""));
    }

    private class task extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingLayout.setVisibility(VISIBLE);
            Log.i("MainActivity", "onPreExecute: " + loadingLayout.getVisibility());
        }

        @Override
        protected String doInBackground(String... strings) {
            new Thread(() -> {
                try {
                    steamPageUrl = new steamSearch(query).execute().get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                if(steamPageUrl != null) {
                    OkHttpClient client1 = new OkHttpClient();
                    //build the request

                    //build request with add cookies for age verification
                    Request request = new Request.Builder()
                            .addHeader("Cookie", "wants_mature_content=1")
                            .addHeader("Cookie", "lastagecheckage=1-0-1990")
                            .addHeader("Cookie", "birthtime=628473601")
                            .url(steamPageUrl) // The URL to send the data to
                            .build();

                    //post and get response
                    Response response = null;

                    //get web page document
                    globalDocument.setDocument(null);
                    try {
                        response = client1.newCall(request).execute(); //execute to get response
                        globalDocument.setDocument(Jsoup.parse(response.body().string(), steamPageUrl));//parse webpage into document
                        runOnUiThread(() -> backgroundData.setValue("done"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Game not found", Toast.LENGTH_SHORT).show());
                    runOnUiThread(() -> loadingLayout.setVisibility(GONE));
                }

            }).start();
            return "done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //hide loading layout
        loadingLayout.setVisibility(GONE);
    }
}