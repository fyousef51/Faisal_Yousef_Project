package com.example.faisal_yousef_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HomePage extends AppCompatActivity {


    SharedPreferences sp;
    RequestQueue rq;
    ImageView weatherImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rq= Volley.newRequestQueue(this);
        final Button button_go_firebase=findViewById(R.id.button_go_fb);
        final Button button_go_weather=findViewById(R.id.button_go_w);
        final Button button_go_sqlite =findViewById(R.id.button_go_sl);
        final ImageView Firebaseimg=findViewById(R.id.imageView3);
        final ImageView WeatherAPI=findViewById(R.id.imageView4);
        final ImageView SQLITE = findViewById(R.id.imageView5);


        sp= PreferenceManager.getDefaultSharedPreferences(this);

        button_go_sqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,SQLITEActivity.class) );
            }
        });
        button_go_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,FireBase.class));
            }
        });
        button_go_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,OpenWeather.class) );
            }
        });
        SQLITE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,SQLITEActivity.class) );
            }
        });
        WeatherAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,OpenWeather.class) );
            }
        });
        Firebaseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,FireBase.class) );
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        rq.add(Helper.weather(this));
    }
}