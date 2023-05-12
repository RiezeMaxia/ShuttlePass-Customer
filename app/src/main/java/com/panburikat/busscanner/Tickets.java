package com.panburikat.busscanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Tickets extends Fragment {

    private LinearLayout layout;
    private String id;

    private void getTickets() {
        Gson gson = new Gson();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[1];
                field[0] = "id";
                //Creating array for data
                String[] data = new String[1];
                data[0] = id;
                PutData putData = new PutData("https://jamora.leon.svdphs.ph/getTicket.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Error: Database connection") && !result.equals("No accountID")) {
                            if (!result.equals("No Results")) {
                                layout.removeAllViews();
                                TicketList[] tl = gson.fromJson(result, TicketList[].class);
                                for (int x = 0; x < tl.length; x++) {
                                    addItem(tl[x].getTicketID(), tl[x].getStop(), tl[x].getQuantity(), tl[x].getDateOfPurchase());
                                }
                            }
                        }

                    }
                }
            }
        }); //End Write and Read data with URL
    }

    private void addItem(String id, String stop, String quantity, String dop) {
        View view = getLayoutInflater().inflate(R.layout.recent_tickets, null);
        TextView tid = view.findViewById(R.id.t_id);
        TextView tstop = view.findViewById(R.id.t_stop);
        TextView tquantity = view.findViewById(R.id.t_quantity);
        TextView tdop = view.findViewById(R.id.t_dop);

        tid.setText(id);
        tstop.setText(stop);
        tquantity.setText(quantity);
        tdop.setText(dop);
        layout.addView(view);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tickets, container, false);
        id = this.getArguments().getString("accID");
        layout = view.findViewById(R.id.ticketHistoryField);


        SwipeRefreshLayout sw = view.findViewById(R.id.swipe);
        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTickets();
                sw.setRefreshing(false);
            }
        });

        getTickets();
        return view;

    }
}