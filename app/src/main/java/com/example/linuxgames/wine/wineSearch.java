package com.example.linuxgames.wine;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class wineSearch extends AsyncTask<String, String, String> {
    final String wineURL = "https://appdb.winehq.org/objectManager.php?bIsQueue=false&bIsRejected=false&sClass=application&sTitle=Browse+Applications&iItemsPerPage=25&iPage=1&sOrderBy=appName&bAscending=true";
    String queryString;

    public wineSearch(String query) {
        queryString = query;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient searchClient = new OkHttpClient();
        //build the form body to post
        RequestBody formBody = new FormBody.Builder()
                .add("iappVersion-ratingOp", "5")
                .add("iappCategoryOp", "11")
                .add("iappVersion-licenseOp", "5")
                .add("sappVersion-ratingData", "")
                .add("iversions-idOp", "5")
                .add("sversions-idData", "")
                .add("sappCategoryData", "2")
                .add("sappVersion-licenseData", "")
                .add("iappFamily-keywordsOp", "2")
                .add("sappFamily-keywordsData", "")
                .add("iappFamily-appNameOp", "2")
                .add("sappFamily-appNameData", queryString)
                .add("ionlyDownloadableOp", "10")
                .add("sFilterSubmit", "")
                .build();

        //build the request
        Request request = new Request.Builder()
                .url(wineURL) // The URL to send the data to
                .post(formBody)
                .build();

        //post and get response
        Response searchResponse;

        //get web page document
        Document searchPage = null;
        try {
            searchResponse = searchClient.newCall(request).execute();
            searchPage = Jsoup.parse(searchResponse.body().string(), wineURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get elements
        Elements table = searchPage.select("table[class=\"whq-table whq-table-full\"]"); //get table
        Elements tbody = table.select("tbody"); //get table body
        Elements tr = tbody.select("tr"); //get first table row
        Elements td = tr.select("td"); //get first table column
        Element a = td.select("a").first(); //get the link

        //convert to string if a url link has been found
        String url = null;
        if(a != null) {
            url = a.toString();
            url = url.substring(url.indexOf("https:"), url.indexOf("\">"));
            url = url.replace("amp;", "");
        }

        Log.i("wineGet", "url: " + url);

        return url;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}