package com.example.linuxgames.ui.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.linuxgames.R;
import com.example.linuxgames.ui.main.PageViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class userFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Boolean loggedIn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PageViewModel pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 0;

        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        pageViewModel.setIndex(index);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            loggedIn = false;
        } else {
            loggedIn = true;
            user = mAuth.getCurrentUser();
        }
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (loggedIn.equals(false)) {
            return inflater.inflate(R.layout.activity_register, container, false);
        } else {
            return inflater.inflate(R.layout.activity_user, container, false);
        }
    }
}