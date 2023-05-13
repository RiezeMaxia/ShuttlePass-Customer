package com.panburikat.busscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.panburikat.busscanner.Util.NetworkChangeListener;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

public class Checkout extends AppCompatActivity {

    NetworkChangeListener nc = new NetworkChangeListener();

    String ID;
    String p = "", o = "", d = "", q = "", tripID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        TextView price = findViewById(R.id.price);
        TextView origin = findViewById(R.id.origin);
        TextView destination = findViewById(R.id.destination);
        TextView quantity = findViewById(R.id.quantity);
        Button confirm = findViewById(R.id.confirm);
        Button cancel = findViewById(R.id.cancel);

        String mess = getIntent().getStringExtra("mess");
        ID = getIntent().getStringExtra("accID");

        try {
            JSONObject details = new JSONObject(mess);
            p = details.getString("price");
            o = details.getString("origin");
            d = details.getString("stop");
            q = details.getString("quantity");
            tripID = details.getString("tripID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        price.setText("₱ " + p);
        origin.setText(o);
        destination.setText(d);
        quantity.setText(q);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Starting Write and Read data with URL
                        //Creating array for parameters
                        String[] field = new String[5];
                        field[0] = "accnum";
                        field[1] = "tripID";
                        field[2] = "stop";
                        field[3] = "quantity";
                        field[4] = "price";
                        //Creating array for data
                        String[] data = new String[5];
                        data[0] = ID;
                        data[1] = tripID;
                        data[2] = d;
                        data[3] = q;
                        data[4] = p;
                        PutData putData = new PutData("https://jamora.leon.svdphs.ph/purchaseTicket.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if (!result.equals("Error: Database connection") && !result.equals("All fields are required")) {
                                    if (result.equals("Balance Insufficient")) {
                                        Toast.makeText(getApplicationContext(), "You have Insufficient Balance, Please top up to purchase tickets", Toast.LENGTH_LONG).show();
                                    } else if (result.equals("Success")) {
                                        Toast.makeText(getApplicationContext(), "Ticket purchase successful!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                    }
                                    finish();
                                }
                            }
                        }
                    }
                }); //End Write and Read data with URL
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(nc, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(nc);
        super.onStop();
    }
}