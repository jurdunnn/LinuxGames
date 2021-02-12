package com.example.linuxgames;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class searchWine extends AsyncTask<String, String, String> {
    final String wineURL = "https://appdb.winehq.org/objectManager.php?bIsQueue=false&bIsRejected=false&sClass=application&sTitle=Browse+Applications&iItemsPerPage=25&iPage=1&sOrderBy=appName&bAscending=true";
    String queryString;

    public searchWine(String query) {
        queryString = query;
        Log.i("SCRAPE", "searchWine: " + queryString);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
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

        Log.i("SCRAPE", "formBody: " + formBody.toString());

        //build the request
        Request request = new Request.Builder()
                .url(wineURL) // The URL to send the data to
                .post(formBody)
                .build();

        Log.i("SCRAPE", "request: " + request.toString());

        //post and get response
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //debugging logging responses
        Log.i("SCRAPE", "response: " + response.message());
        Log.i("SCRAPE", "response: " + response.body().toString());
        Log.i("SCRAPE", "response: " + response.toString());
        Log.i("SCRAPE", "response: " + response.cacheControl().toString());
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}