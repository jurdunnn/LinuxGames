package com.example.linuxgames.lutris;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class lutrisSearch extends AsyncTask<String, String, String> {
    String searchQuery;

    public lutrisSearch(String query) {
        searchQuery = query;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        //lutris url
        String url = "https://lutris.net/games?q=";

        //ensure that the query is formatted correctly - replaces spaces for +'s and "'" with %27
        searchQuery = searchQuery.replace(" ", "+").replace("'", "%27");

        //append url to include query
        url += searchQuery;

        //build the request
        assert url!=null;
        Request request = new Request.Builder()
                .url(url) // The URL to send the data to
                .build();

        //post and get response
        Response response = null;

        //get web page document
        Document document = null;
        try {
            response = client.newCall(request).execute(); //execute to get response
            document = Jsoup.parse(response.body().string(), url); //parse webpage into
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements resultCol = document.select("div.col-sm-9");
        Element links = resultCol.select("a").first();
        String href = links.attr("href");

        Log.i("LUTRIS", "doInBackground: " + href);
        //return best rating found
        return "https://lutris.net" + href;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}