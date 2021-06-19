package com.olegro.cristianpostcards.mvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.olegro.cristianpostcards.R;
import com.olegro.cristianpostcards.data.repository.Repository;
import com.olegro.cristianpostcards.mvvm.view.adapter.RVAdapter;
import com.olegro.cristianpostcards.utility.UniqueID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    RecyclerView recyclerView;
    RVAdapter rVAdapter;

    FloatingActionButton fab;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, getString(R.string.ads_app_id));
        Fabric.with(this, new Crashlytics());

       // startActivity(new Intent(this, Main2Activity.class));
        if (!isNetworkExist()) {
            Toast.makeText(this, getString(R.string.toast_internet_on), Toast.LENGTH_LONG).show();
        }

        prefs = getSharedPreferences("FirstRunApp", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true)) {
            new Repository(this).authorization(UniqueID.getUniqueID(this), "");
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));
//        getSupportActionBar().setTitle("Открытки");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        rVAdapter = new RVAdapter(this);
        recyclerView.setAdapter(rVAdapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.putExtra("favorite", "favorite");
            startActivity(intent);
        });


    }

    private boolean isNetworkExist() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
