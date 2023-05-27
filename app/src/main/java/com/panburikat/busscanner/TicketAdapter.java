package com.panburikat.busscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TicketAdapter extends ArrayAdapter<TicketList> {
    private static final String TAG = "TicketAdapter";
    private Context mContext;

    int mResource;

    public TicketAdapter(Context context, int resource, ArrayList<TicketList> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String ticketID = getItem(position).getTicketID();
        String stop = getItem(position).getStop();
        String quantity = getItem(position).getQuantity();
        String dateOfPurchase = getItem(position).getDateOfPurchase();
        String status = getItem(position).getStatus();

        TicketList ticketList = new TicketList(ticketID, stop, quantity, dateOfPurchase, status);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView t_id = (TextView) convertView.findViewById(R.id.t_id);
        TextView t_stop = (TextView) convertView.findViewById(R.id.t_stop);
        TextView t_dop = (TextView) convertView.findViewById(R.id.t_dop);
        TextView t_quantity = (TextView) convertView.findViewById(R.id.t_quantity);

        t_id.setText(ticketID);
        t_stop.setText(stop);
        t_dop.setText(dateOfPurchase);
        t_quantity.setText(quantity);

        return convertView;
    }
}
