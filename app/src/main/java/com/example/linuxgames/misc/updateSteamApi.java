package com.example.linuxgames.misc;

import com.google.firebase.database.FirebaseDatabase;

public class updateSteamApi {
    credentials credentials = new credentials();

    public updateSteamApi() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://linuxgames-4c9a9-default-rtdb.firebaseio.com/");
        firebaseDatabase.getReference()
                .setValue("https://api.steampowered.com/IStoreService/GetAppList/v1/?key="
                        + credentials.getSteam_api_key()
                        + "&max_results=50000");
    }
}
