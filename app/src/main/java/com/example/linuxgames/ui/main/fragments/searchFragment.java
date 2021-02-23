package com.example.linuxgames.ui.main.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.linuxgames.MainActivity;
import com.example.linuxgames.R;
import com.example.linuxgames.gameDetails;
import com.example.linuxgames.misc.steamPageDoc;
import com.example.linuxgames.steam.steamSearch;
import com.example.linuxgames.ui.main.PageViewModel;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class searchFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    String steamPageUrl;
    String query;

    steamPageDoc globalDocument;

    MutableLiveData<String> backgroundData;

    //views
    ConstraintLayout loadingLayout;
    EditText searchBox;
    ImageView clearSearchButton;
    TabLayout tabs;

    public static searchFragment newInstance(int index) {
        searchFragment fragment = new searchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

        backgroundData = new MutableLiveData<>();

        //get instance of global document
        globalDocument = steamPageDoc.getInstance();

        //wait for background search to be complete before moving to new activity.
        backgroundData.observe(this, s -> {
            //start new activity
            Intent intent = new Intent(getActivity(), gameDetails.class);
            intent.putExtra("steamurl", steamPageUrl);

            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            loadingLayout.setVisibility(GONE);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchBox = getView().findViewById(R.id.searchBox);
        loadingLayout = getView().findViewById(R.id.loadingLayout);
        clearSearchButton = getView().findViewById(R.id.clearSearchButton);
        tabs = getActivity().findViewById(R.id.tabs);

        //add listener for enter button
        searchBox.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                tabs.setVisibility(GONE);
                //get query text
                query = searchBox.getText().toString();

                new task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                return true;
            }
            return false;
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

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
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

                if (steamPageUrl != null) {
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
                        getActivity().runOnUiThread(() -> backgroundData.setValue("done"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    getActivity().runOnUiThread(() -> loadingLayout.setVisibility(GONE));
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Game not found", Toast.LENGTH_SHORT).show());
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
    public void onResume() {
        super.onResume();
        tabs.setVisibility(VISIBLE);
    }
}