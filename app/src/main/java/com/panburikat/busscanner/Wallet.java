package com.panburikat.busscanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Wallet extends Fragment {

    private TextView bal, empty;
    private LinearLayout layout;

    private LinearLayout layoutT;
    private String id;

    private void getWallet() {
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
                PutData putData = new PutData("https://jamora.leon.svdphs.ph/getBalance.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("Error: Database connection") || result.equals("No accountID")) {
                            bal.setText("Error: Please Restart the App");
                        } else {
                            bal.setText("₱ " + result);
                        }
                    }
                }
            }
        }); //End Write and Read data with URL
    }

    private void getTransactions() {
        Gson gson = new Gson();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[2];
                field[0] = "id";
                field[1] = "limit";
                //Creating array for data
                String[] data = new String[2];
                data[0] = id;
                data[1] = "5";
                PutData putData = new PutData("https://jamora.leon.svdphs.ph/getTransaction.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Error: Database connection") && !result.equals("No accountID")) {
                            if (!result.equals("No Results")) {
                                layout.removeAllViews();
                                TransactionList[] tl = gson.fromJson(result, TransactionList[].class);
                                for (int x = 0; x < tl.length; x++) {
                                    addItem(tl[x].getTransactionID(), tl[x].getTransactionType(), tl[x].getDate(), tl[x].getAmount());
                                }
                            }
                        }

                    }
                }
            }
        }); //End Write and Read data with URL
    }

    private void getTicket() {
        Gson gson = new Gson();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[2];
                field[0] = "id";
                field[1] = "limit";
                //Creating array for data
                String[] data = new String[2];
                data[0] = id;
                data[1] = "1";
                PutData putData = new PutData("https://jamora.leon.svdphs.ph/getTicket.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Error: Database connection") && !result.equals("No accountID")) {
                            if (!result.equals("No Results")) {
                                layoutT.removeAllViews();
                                TicketList[] tl = gson.fromJson(result, TicketList[].class);

                                addItemT(tl[0].getTicketID(), tl[0].getStop(), tl[0].getQuantity(), tl[0].getDateOfPurchase(), tl[0].getStatus());

                            }
                        }

                    }
                }
            }
        }); //End Write and Read data with URL
    }


    private void addItem(String id, String type, String date, String amount) {
        View view = getLayoutInflater().inflate(R.layout.recent_transaction, null);
        TextView tid = view.findViewById(R.id.t_id);
        TextView ttype = view.findViewById(R.id.t_type);
        TextView tdate = view.findViewById(R.id.t_date);
        TextView tamount = view.findViewById(R.id.t_amount);

        tid.setText(id);
        ttype.setText(type);
        tdate.setText(date);
        tamount.setText(amount);
        layout.addView(view);
    }

    private void addItemT(String id, String stop, String quantity, String dateOfPurchase, String status) {
        View view = getLayoutInflater().inflate(R.layout.recent_tickets, null);
        TextView t_id = view.findViewById(R.id.t_id);
        TextView t_stop = view.findViewById(R.id.t_stop);
        TextView t_dop = view.findViewById(R.id.t_dop);
        TextView t_quantity = view.findViewById(R.id.t_quantity);

        t_id.setText(id);
        t_stop.setText(stop);
        t_dop.setText(dateOfPurchase);
        t_quantity.setText(quantity);
        layoutT.addView(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        id = this.getArguments().getString("accID");
        layout = view.findViewById(R.id.r_transaction);
        layoutT = view.findViewById(R.id.r_ticket);

        bal = view.findViewById(R.id.bal);
        empty = view.findViewById(R.id.empty);
        Button cashin = view.findViewById(R.id.cashin);
        Button transfer = view.findViewById(R.id.transfer);

        cashin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CashIn.class);
                intent.putExtra("accID", id);
                startActivity(intent);
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Transfer.class);
                intent.putExtra("accID", id);
                startActivity(intent);
            }
        });

        SwipeRefreshLayout sw = view.findViewById(R.id.swipe);
        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWallet();
                getTransactions();
                getTicket();
                sw.setRefreshing(false);
            }
        });

        getWallet();
        getTransactions();
        getTicket();

        // Inflate the layout for this fragment
        return view;
    }
}