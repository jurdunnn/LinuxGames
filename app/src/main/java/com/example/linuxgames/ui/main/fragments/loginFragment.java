package com.example.linuxgames.ui.main.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.linuxgames.R;
import com.example.linuxgames.activities.gameDetails;
import com.example.linuxgames.activities.registerActivity;
import com.example.linuxgames.ui.main.PageViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.view.View.GONE;

public class loginFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Boolean loggedIn = false;

    //views
    ImageView emailIcon, passwordIcon;
    EditText emailText, passwordText;
    MaterialCardView emailCard, passwordCard;

    TextView signupButton;

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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(loggedIn.equals(false)) {

            //email
            emailIcon = getActivity().findViewById(R.id.loginEmailIcon);
            emailText = getActivity().findViewById(R.id.loginEmailText);
            emailCard = getActivity().findViewById(R.id.loginEmailCard);
            emailText.setOnFocusChangeListener((v, hasFocus) -> {
                if(hasFocus) {
                    emailIcon.setImageResource(R.drawable.ic_baseline_mail_outline_focused);
                    emailText.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
                    emailCard.setStrokeColor(getResources().getColor(R.color.design_default_color_primary_dark));
                } else {
                    emailIcon.setImageResource(R.drawable.ic_baseline_mail_outline_24);
                    emailText.setTextColor(getResources().getColor(R.color.disabled_color));
                    emailCard.setStrokeColor(getResources().getColor(R.color.disabled_color));
                }
            });
            //password
            passwordIcon = getActivity().findViewById(R.id.loginPasswordIcon);
            passwordText = getActivity().findViewById(R.id.loginPasswordText);
            passwordCard = getActivity().findViewById(R.id.loginPasswordCard);
            passwordText.setOnFocusChangeListener((v, hasFocus) -> {
                if(hasFocus) {
                    passwordIcon.setImageResource(R.drawable.ic_baseline_lock_open_focused);
                    passwordText.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
                    passwordCard.setStrokeColor(getResources().getColor(R.color.design_default_color_primary_dark));
                } else {
                    passwordIcon.setImageResource(R.drawable.ic_baseline_lock_open_24);
                    passwordText.setTextColor(getResources().getColor(R.color.disabled_color));
                    passwordCard.setStrokeColor(getResources().getColor(R.color.disabled_color));
                }
            });

            signupButton = getActivity().findViewById(R.id.loginSignupButton);
            signupButton.setOnClickListener(v -> {
                //start new activity
                Intent intent = new Intent(getActivity(), registerActivity.class);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            });

        } else {

        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (loggedIn.equals(false)) {
            return inflater.inflate(R.layout.activity_login, container, false);
        } else {
            return inflater.inflate(R.layout.activity_user, container, false);
        }
    }
}
