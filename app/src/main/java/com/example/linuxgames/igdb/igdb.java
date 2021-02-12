package com.example.linuxgames.igdb;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class igdb extends AsyncTask<String, String, String> {
    String queryURL = "https://www.igdb.com/search?type=1&q=";
    String queryString;
    String returnCount;

    public igdb(String query) {
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

        Log.i("igdb", "url: " + queryURL);
        //build the request
        Request request = new Request.Builder()
                .url(queryURL) // The URL to send the data to
                .build();

        //post and get response
        Response response = null;

        //get web page document
        Document document = null;
        try {
            response = client.newCall(request).execute(); //execute to get response
            document = Jsoup.parse(response.body().string(), queryURL); //parse webpage into
        } catch (IOException e) {
            e.printStackTrace();
        }

        //grab elements
        Elements jsonElements = document.getElementsByAttributeStarting("data-json");

        //Elements to string
        String json = jsonElements.toString();

        //clean up json
        json = json.replace("&quot", "");
        json = json.replace(";", "");
        json = json.replace("<div data-json=\"", "");
        json = json.replace(":", "");

        //extract the first element. Most relevant
        json = json.substring(json.indexOf("{"), json.indexOf("}"));

        //split by url - url in array[0], url start from 0 to first comma
        String[] splitJsonObject = json.split("url");

        //start at 0, end at first comma
        String url = splitJsonObject[1].substring(0, splitJsonObject[1].indexOf(","));

        //make full url
        url = "https://www.igdb.com" + url;

        //log full url
        Log.i("igdb", "game url: " + url);

        //TODO: in future only the URL will be needed. Remove all below

        //get webpage
        OkHttpClient client2 = new OkHttpClient();

        //build the request
        Request request2 = new Request.Builder()
                .url(url) // The URL to send the data to
                .build();

        //post and get response
        Response response2 = null;

        //get web page document
        Document document2 = null;
        try {
            response2 = client2.newCall(request2).execute(); //execute to get response
            document2 = Jsoup.parse(response2.body().string(), url); //parse webpage into
        } catch (IOException e) {
            e.printStackTrace();
        }

        //return title and url
        return url + "~" + document2.title();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}