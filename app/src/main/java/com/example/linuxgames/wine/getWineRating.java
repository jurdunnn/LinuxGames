package com.example.linuxgames.wine;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class getWineRating extends AsyncTask<String, String, String> {
    String url;
    String rating;

    public getWineRating(String getUrl) {
        url = getUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        //append url to include query

        Log.i("getWinePage", "url: " + url);
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

        //get all the rating elements
        Elements platinum = document.getElementsByClass("Platinum");
        Elements gold = document.getElementsByClass("Gold");
        Elements silver = document.getElementsByClass("Silver");
        Elements bronze = document.getElementsByClass("Bronze");
        Elements garbage = document.getElementsByClass("Garbage");

        //check if there are rows with ratings present
        if(platinum.size() > 0) {
            rating = "Platinum";
        } else if (gold.size() > 0) {
            rating = "Gold";
        } else if(silver.size() > 0) {
            rating = "Silver";
        } else if(bronze.size() > 0) {
            rating = "Bronze";
        } else if (garbage.size() > 0) {
            rating = "Garbage";
        }

        //return best rating found
        return rating;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}