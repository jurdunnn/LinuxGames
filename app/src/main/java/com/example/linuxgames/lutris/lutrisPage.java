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

public class lutrisPage extends AsyncTask<String, String, String> {
    String pageUrl;

    public lutrisPage(String url) {
        pageUrl = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        //build the request
        assert pageUrl!=null;
        Request request = new Request.Builder()
                .url(pageUrl) // The URL to send the data to
                .build();

        //post and get response
        Response response = null;

        //get web page document
        Document document = null;
        try {
            response = client.newCall(request).execute(); //execute to get response
            document = Jsoup.parse(response.body().string(), pageUrl); //parse webpage into
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements installerList = document.select("div.installer-list");
        Elements methods = installerList.select("ul");

        int count = 0;
        for (Element element : methods) {
            count++;
        }

        Log.i("LUTRIS", "rating : " + count);

        switch (count) {
            case 1:
                return "Bronze";
            case 2:
                return "Silver";
            case 3:
                return "Gold";
        }

        if(count > 3) {
            return "Platinum";
        } else {
            return "Garbage";
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}