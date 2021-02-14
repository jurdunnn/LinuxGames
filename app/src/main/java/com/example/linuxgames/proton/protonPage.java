package com.example.linuxgames.proton;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class protonPage extends AsyncTask<String, String, String> {
    String steamId;

    public protonPage(String Id) {
        steamId = Id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        //javascript only site.
        //append url to include query
        final String url = "https://protondb.com/api/v1/reports/summaries/" + steamId + ".json";

        OkHttpClient client = new OkHttpClient();

        //build the request
        Request request = new Request.Builder()
                .url(url) // The URL to send the data to
                .build();

        //post and get response
        Response response;

        //get web page document
        Document document = null;
        try {
            response = client.newCall(request).execute(); //execute to get response
            document = Jsoup.parse(response.body().string(), url); //parse webpage into
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get search result container
        String json = document.text();
        json = json.split("\"tier\": \"")[1].split("\"")[0];
        Log.i("json", "proton: " + json);

        return json.substring(0,1).toUpperCase() + json.substring(1);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}