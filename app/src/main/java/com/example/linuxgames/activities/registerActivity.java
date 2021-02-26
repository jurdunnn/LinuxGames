package com.example.linuxgames.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.linuxgames.R;
import com.example.linuxgames.tabbedActivity;
import com.example.linuxgames.ui.main.fragments.loginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.r0adkll.slidr.Slidr;

public class registerActivity extends AppCompatActivity {

    ImageView emailIcon, usernameIcon, steamIcon, password1Icon, password2Icon, backButton;
    EditText emailText, usernameText, steamText, password1Text, password2Text;
    MaterialCardView emailCard, usernameCard, steamCard, password1Card, password2Card;
    Button createAccountButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

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
        createAccountButton.setOnClickListener(v -> registerAccount());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
    }

    private void registerAccount() {
        if(checkEmail() && checkUsername() && checkPassword()) {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            //firebase register

            mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), password1Text.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Log.i("registerAccount", "onComplete: success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(registerActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.i("registerAccount", "onComplete: failure", task.getException());
                        Toast.makeText(registerActivity.this, "Autentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private Boolean checkEmail(){
        String email = emailText.getText().toString();
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        Log.i("registerAccount", "checkEmail: " + email.matches(regex));

        if(email.matches(regex)) {
            return true;
        } else {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private Boolean checkUsername(){
        String username = usernameText.getText().toString();
        String regex = "^[A-Za-z]\\w{5,29}$";
        Log.i("registerAccount", "checkUsername: " + username.matches(regex));

        if(username.matches(regex)) {
            return true;
        } else {
            Toast.makeText(this, "Invalid username format", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private Boolean checkPassword() {
        String password1 = password1Text.getText().toString();
        String password2 = password2Text.getText().toString();

        if(password1.equals(password2)) {
            return true;
        } else {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}