package com.panburikat.busscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter<TransactionList> {
    private static final String TAG = "TransactionAdapter";
    private Context mContext;

    int mResource;

    public TransactionAdapter(Context context, int resource, ArrayList<TransactionList> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String transactionID = getItem(position).getTransactionID();
        String referenceNum = getItem(position).getReferenceNum();
        String transactionType = getItem(position).getTransactionType();
        String description = getItem(position).getDescription();
        String date = getItem(position).getDate();
        String amount = getItem(position).getAmount();

        TransactionList transactionList = new TransactionList(transactionID, referenceNum, transactionType, description, date, amount);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView type = (TextView) convertView.findViewById(R.id.type);
        TextView datetime = (TextView) convertView.findViewById(R.id.datetime);
        TextView t_amount = (TextView) convertView.findViewById(R.id.t_amount);

        id.setText(transactionID);
        type.setText(transactionType);
        datetime.setText(date);
        t_amount.setText(amount);

        return convertView;
    }
}
