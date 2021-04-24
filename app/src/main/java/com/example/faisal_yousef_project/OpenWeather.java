package com.example.faisal_yousef_project;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class OpenWeather extends AppCompatActivity {

    RequestQueue rq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_weather);

        rq= Volley.newRequestQueue(this);
        rq.add(Helper.weather(this));

        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);

        EditText insert_city=findViewById(R.id.input_weather_city);
        Button submit_city=findViewById(R.id.btn_set_city);

        submit_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCityString=insert_city.getText()+"";
                if (newCityString.isEmpty()){
                    Toast.makeText(OpenWeather.this,"Please insert a city name.",Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor e=sp.edit();
                e.putString("WeatherCity",newCityString);
                e.commit();
                rq.add(Helper.weather(OpenWeather.this));
            }
        });
        rq.add(Helper.weather(this));

    }

}