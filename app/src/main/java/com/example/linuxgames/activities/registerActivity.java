package com.example.linuxgames.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.linuxgames.R;
import com.google.android.material.card.MaterialCardView;
import com.r0adkll.slidr.Slidr;

public class registerActivity extends AppCompatActivity {

    ImageView emailIcon, usernameIcon, steamIcon, password1Icon, password2Icon, backButton;
    EditText emailText, usernameText, steamText, password1Text, password2Text;
    MaterialCardView emailCard, usernameCard, steamCard, password1Card, password2Card;
    Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //email
        emailIcon = findViewById(R.id.registerEmailIcon);
        emailText = findViewById(R.id.registerEmailText);
        emailCard = findViewById(R.id.registerEmailCard);
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

        //username
        usernameIcon = findViewById(R.id.registerUsernameIcon);
        usernameText = findViewById(R.id.registerUsernameText);
        usernameCard = findViewById(R.id.registerUsernameCard);
        usernameText.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                usernameIcon.setImageResource(R.drawable.ic_baseline_person_outline_focused);
                usernameText.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
                usernameCard.setStrokeColor(getResources().getColor(R.color.design_default_color_primary_dark));
            } else {
                usernameIcon.setImageResource(R.drawable.ic_baseline_person_outline_24);
                usernameText.setTextColor(getResources().getColor(R.color.disabled_color));
                usernameCard.setStrokeColor(getResources().getColor(R.color.disabled_color));
            }
        });

        //steam
        steamIcon = findViewById(R.id.registerSteamIDIcon);
        steamText = findViewById(R.id.registerSteamIDText);
        steamCard = findViewById(R.id.registerSteamIDCard);
        steamText.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                steamIcon.setImageResource(R.drawable.ic_baseline_steam_focused);
                steamText.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
                steamCard.setStrokeColor(getResources().getColor(R.color.design_default_color_primary_dark));
            } else {
                steamIcon.setImageResource(R.drawable.ic_baseline_steam_24);
                steamText.setTextColor(getResources().getColor(R.color.disabled_color));
                steamCard.setStrokeColor(getResources().getColor(R.color.disabled_color));
            }
        });

        //password
        password1Icon = findViewById(R.id.registerPasswordIcon);
        password1Text = findViewById(R.id.registerPasswordText);
        password1Card = findViewById(R.id.registerPasswordCard);
        password1Text.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                password1Icon.setImageResource(R.drawable.ic_baseline_lock_open_focused);
                password1Text.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
                password1Card.setStrokeColor(getResources().getColor(R.color.design_default_color_primary_dark));
            } else {
                password1Icon.setImageResource(R.drawable.ic_baseline_lock_open_24);
                password1Text.setTextColor(getResources().getColor(R.color.disabled_color));
                password1Card.setStrokeColor(getResources().getColor(R.color.disabled_color));
            }
        });

        //password confirm
        password2Icon = findViewById(R.id.registerConfirmPasswordIcon);
        password2Text = findViewById(R.id.registerConfirmPasswordText);
        password2Card = findViewById(R.id.registerConfirmPasswordCard);
        password2Text.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                password2Icon.setImageResource(R.drawable.ic_baseline_lock_open_focused);
                password2Text.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
                password2Card.setStrokeColor(getResources().getColor(R.color.design_default_color_primary_dark));
            } else {
                password2Icon.setImageResource(R.drawable.ic_baseline_lock_open_24);
                password2Text.setTextColor(getResources().getColor(R.color.disabled_color));
                password2Card.setStrokeColor(getResources().getColor(R.color.disabled_color));
            }
        });

        createAccountButton = findViewById(R.id.registerButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
    }
}