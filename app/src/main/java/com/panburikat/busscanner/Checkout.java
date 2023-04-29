package com.panburikat.busscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Checkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        TextView textView = findViewById(R.id.tv);

        String mess = getIntent().getStringExtra("mess");
        try {
            JSONObject details = new JSONObject(mess);
            textView.setText(String.valueOf(details.getInt("age")));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}