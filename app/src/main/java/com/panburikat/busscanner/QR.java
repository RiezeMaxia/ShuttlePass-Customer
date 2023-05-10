package com.panburikat.busscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class QR extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        ID = getIntent().getStringExtra("accID");

        CodeScannerView scannerView = findViewById(R.id.scanner);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.getText().contains("SHUTTLEpass")) {
                            String json = result.getText().substring(12);
                            Intent intent = new Intent(QR.this, Checkout.class);
                            intent.putExtra("accID", ID);
                            intent.putExtra("mess", json);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(QR.this);
                            dialog.setTitle("QR Code not Recognized");
                            dialog.setMessage("You have scanned an invalid QR code, please purchase tickets at our Kiosk.");
                            dialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mCodeScanner.startPreview();
                                }
                            }).show();
                        }
                    }
                });
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 100);
        }
        mCodeScanner.startPreview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Permission Required");
                dialog.setMessage("Camera Permission is needed in order to use the QR Scanner");
                dialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(QR.this, new String[]{android.Manifest.permission.CAMERA}, 100);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
            }
        }
    }
}