package com.example.linuxgames.ui.main.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.linuxgames.R;
import com.example.linuxgames.activities.registerActivity;
import com.example.linuxgames.ui.main.PageViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class loginFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //views
    ImageView emailIcon, passwordIcon, usernameIcon, steamIDIcon;
    EditText emailText, passwordText;
    TextView usernameText, steamIDText;
    MaterialCardView emailCard, passwordCard, usernameCard, steamIDcard;

    TextView signupButton;
    Button loginButton;

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
        user = mAuth.getCurrentUser();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (user != null) {
            populateUI();
        } else {
            //email
            emailIcon = getActivity().findViewById(R.id.loginEmailIcon);
            emailText = getActivity().findViewById(R.id.loginEmailText);
            emailCard = getActivity().findViewById(R.id.loginEmailCard);
            emailText.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
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
                if (hasFocus) {
                    passwordIcon.setImageResource(R.drawable.ic_baseline_lock_open_focused);
                    passwordText.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
                    passwordCard.setStrokeColor(getResources().getColor(R.color.design_default_color_primary_dark));
                } else {
                    passwordIcon.setImageResource(R.drawable.ic_baseline_lock_open_24);
                    passwordText.setTextColor(getResources().getColor(R.color.disabled_color));
                    passwordCard.setStrokeColor(getResources().getColor(R.color.disabled_color));
                }
            });

            loginButton = getActivity().findViewById(R.id.loginButton);
            loginButton.setOnClickListener(v -> login());

            signupButton = getActivity().findViewById(R.id.loginSignupButton);
            signupButton.setOnClickListener(v -> {
                //start new activity
                Intent intent = new Intent(getActivity(), registerActivity.class);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            });
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (user != null) {
            return inflater.inflate(R.layout.activity_user, container, false);
        } else {
            return inflater.inflate(R.layout.activity_login, container, false);
        }
    }

    private void login() {
        mAuth.signInWithEmailAndPassword(emailText.getText().toString(),passwordText.getText().toString()).addOnSuccessListener(getActivity(), authResult -> {
            user = mAuth.getCurrentUser();
            getActivity().recreate();
        }).addOnFailureListener(getActivity(), e -> Toast.makeText(getActivity(), "username or password incorrect", Toast.LENGTH_SHORT).show());
    }

    private void populateUI() {
        populateChangeAccountInformation();
        populateSteamProton();
    }

    private void populateChangeAccountInformation() {
        Button userConfirmChanges = getActivity().findViewById(R.id.userChangeButton);

        usernameText = getActivity().findViewById(R.id.userUsernameText);
        steamIDText = getActivity().findViewById(R.id.userSteamText);

        String username = user.getDisplayName().split("#")[0];
        String steamID = user.getDisplayName().split("#")[1];

        String steamURL = "https://steamcommunity.com/id/" + steamID;
        usernameText.setText(username);
        steamIDText.setText(steamURL);

        usernameCard = getActivity().findViewById(R.id.userUsernameCard);
        steamIDcard = getActivity().findViewById(R.id.userSteamCard);

        usernameIcon = getActivity().findViewById(R.id.userUsernameImage);
        steamIDIcon = getActivity().findViewById(R.id.userSteamImage);

        usernameText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                usernameIcon.setImageResource(R.drawable.ic_baseline_person_outline_focused);
                usernameText.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
                usernameCard.setStrokeColor(getResources().getColor(R.color.design_default_color_primary_dark));
            } else {
                usernameIcon.setImageResource(R.drawable.ic_baseline_person_outline_24);
                usernameText.setTextColor(getResources().getColor(R.color.disabled_color));
                usernameCard.setStrokeColor(getResources().getColor(R.color.disabled_color));
            }
        });

        steamIDText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                steamIDIcon.setImageResource(R.drawable.ic_baseline_steam_focused);
                steamIDText.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
                steamIDcard.setStrokeColor(getResources().getColor(R.color.design_default_color_primary_dark));
            } else {
                steamIDIcon.setImageResource(R.drawable.ic_baseline_steam_24);
                steamIDText.setTextColor(getResources().getColor(R.color.disabled_color));
                steamIDcard.setStrokeColor(getResources().getColor(R.color.disabled_color));
            }
        });

        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userConfirmChanges.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        steamIDText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userConfirmChanges.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        userConfirmChanges.setOnClickListener(v -> {
            //do stuff
        });

        TextView accountInformationTextView = getActivity().findViewById(R.id.accountinformationText);
        ConstraintLayout accountInformationView = getActivity().findViewById(R.id.accountInformationContainer);

        accountInformationTextView.setOnClickListener(v -> {
            if(accountInformationView.getVisibility() == View.VISIBLE) {
                accountInformationView.setVisibility(View.GONE);
            } else {
                accountInformationView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void populateSteamProton() {
        TextView steamProtonTextView = getActivity().findViewById(R.id.steamProtonText);
        ConstraintLayout steamProtonView = getActivity().findViewById(R.id.steamProtonContainer);

        steamProtonTextView.setOnClickListener(v -> {
            if(steamProtonView.getVisibility() == View.VISIBLE) {
                steamProtonView.setVisibility(View.GONE);
            } else {
                steamProtonView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        user = mAuth.getCurrentUser();
    }
}
