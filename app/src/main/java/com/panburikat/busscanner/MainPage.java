package com.panburikat.busscanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.panburikat.busscanner.databinding.ActivityMainPageBinding;

public class MainPage extends AppCompatActivity {

    ActivityMainPageBinding bind;
    String ID;
    int tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ID = getIntent().getStringExtra("accID");
        bind = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        bind.bottomNavView.setBackground(null);
        for (int i = 0; i < bind.bottomNavView.getMenu().size(); i++) {
            MenuItem menuItem = bind.bottomNavView.getMenu().getItem(i);
            View view = bind.bottomNavView.findViewById(menuItem.getItemId());
            TooltipCompat.setTooltipText(view, "");
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // your long click listener code here
                    return true;
                }
            });
        }
        replaceFragment(new Wallet());
        tag = 0;
        bind.bottomNavView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.wallet:
                    replaceFragment(new Wallet());
                    tag = 0;
                    break;
                case R.id.inbox:
                    replaceFragment(new Inbox());
                    tag = 1;
                    break;
                case R.id.transactions:
                    replaceFragment(new Transactions());
                    tag = 2;
                    break;
                case R.id.profile:
                    replaceFragment(new Profile());
                    tag = 3;
                    break;
            }
            return true;

        });

        bind.bottomNavView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
            }
        });

        FloatingActionButton qr = findViewById(R.id.qr);

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, QR.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Log Out");
        dialog.setMessage("Are you sure you want to Log out?");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bund = new Bundle();
        bund.putString("accID", ID);
        fragment.setArguments(bund);
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }
}