package com.example.faisal_yousef_project;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;



public class Helper {


    public static ImageView addWeatherIcon(AppCompatActivity a, @Nullable View.OnClickListener clickListener){

        ImageView iv=new ImageView(a);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(a);

        String icon=sp.getString("icon","");
        Glide.with(a).load(icon).into(iv);

        ActionBar actionBar = a.getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams( Gravity.END
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        iv.setLayoutParams(layoutParams);
        actionBar.setCustomView(iv);

        if (clickListener ==null){
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String city=sp.getString("WeatherCity","Riyadh");
                    float temp=sp.getFloat("temp",-1);
                    float humidity=sp.getFloat("humidity",-1);
                    int clouds=sp.getInt("clouds",-1);
                    AlertDialog.Builder dialog=new AlertDialog.Builder(a);
                    dialog.setTitle("Weather for "+city);
                    String message="Temperature: "+ temp +"C\n"+
                            "Humidity: "+humidity+"% \n"+"" +
                            "clouds: "+clouds;
                    dialog.setMessage(message);

                    dialog.show();
                }
            });
        }else{
            iv.setOnClickListener(clickListener);
        }


        return iv;
    }

    public static JsonObjectRequest weather(AppCompatActivity a) {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(a);
        String city=sp.getString("WeatherCity","Riyadh");
        //String url ="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=468d0b72ee9afffaed6350d0bb7aa659&units=metric";
        String url ="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=2c46198362a049592870ce9da62623c5&units=metric";

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject weather = response.getJSONObject("main");
                    String iconName= response.getJSONArray("weather").getJSONObject(0).getString("icon");
                    String iconURL="https://openweathermap.org/img/w/"+iconName+".png";

                    SharedPreferences.Editor e=sp.edit();
                    e.putFloat("humidity",(float)weather.getDouble("humidity"));
                    e.putInt("clouds",response.getJSONObject("clouds").getInt("all"));
                    e.putString("icon",iconURL);
                    e.putFloat("temp",(float)weather.getDouble("temp"));
                    e.commit();

                    addWeatherIcon(a,null);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(a,"A problem has occurred please try again "+error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        return jsonObj;

    }

}
