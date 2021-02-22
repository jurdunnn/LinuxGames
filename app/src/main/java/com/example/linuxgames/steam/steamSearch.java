package com.example.linuxgames.steam;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class steamSearch extends AsyncTask<String, String, String> {
    String queryURL = "https://store.steampowered.com/search/?term=";
    String queryString;

    public steamSearch(String query) {
        queryString = query.replace(" ", "+");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
        //append url to include query
        queryURL = queryURL + queryString;

        Log.i("steam", "url: " + queryURL);

        //build the request
        Request request = new Request
                .Builder()
                .url(queryURL) // The URL to send the data to
                .build();

        //post and get response
        Response response;

        //get web page document
        Document document = null;
        try {
            response = client.newCall(request).execute(); //execute to get response
            document = Jsoup.parse(response.body().string(), queryURL); //parse webpage into
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements searchResultContainer;
        Element appid = null;
        //get search result container
        try {
            searchResultContainer = document.select("div#search_result_container");
            appid = searchResultContainer.select("a").first();
        } catch (RuntimeException runtimeException) {
            Log.i("Exception", "error: possible network error.");
        }


        //construct url
        String url = null;
        if (appid != null) {
            String appidStr = appid.toString();
            appidStr = appidStr.split("appid=\"")[1];
            appidStr = appidStr.split("\"")[0];
            url = "https://store.steampowered.com/app/" + appidStr + "/";
        }


        //log full url
        Log.i("igdb", "game url: " + url);
        //return title and url
        return url;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}