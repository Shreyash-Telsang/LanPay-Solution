package com.example.new4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.new4.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.example.new4.Validation.AESUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int CAMERA_PERMISSION_REQUEST = 1001;
    private GmsBarcodeScanner scanner;
    private final String AES_KEY = "YourSecretKey1234567890123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize QR scanner
        scanner = GmsBarcodeScanning.getClient(this);

        // Set up bottom navigation
        BottomNavigationView navView = binding.navView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Set up button click listeners
        setupButtonClickListeners();
    }

    private void setupButtonClickListeners() {
        Button qrScannerButton = binding.QRScanner;
        qrScannerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QRScannerActivity.class);
            startActivity(intent);
        });

        Button transactionButton = binding.TransactionHistory;
        transactionButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
            startActivity(intent);
        });

        Button payAccountButton = binding.PayAccountNo;
        payAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PayAccountActivity.class);
            startActivity(intent);
        });

        Button cameraButton = binding.camera2;
        cameraButton.setOnClickListener(v -> {
            // Directly start the QR scanning
            checkCameraPermissionAndScan();
        });
    }

    private void checkCameraPermissionAndScan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startQRScan();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQRScan();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startQRScan() {
        scanner.startScan()
                .addOnSuccessListener(barcode -> {
                    String rawValue = barcode.getRawValue();

                    if (rawValue == null || rawValue.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        Map<String, String> jsonData = new HashMap<>();
                        jsonData.put("to", rawValue);
//                        jsonData.put("from", senderAccount);

                        ServerRequest.sendPostRequest(MainActivity.this, "http://<your_server_ip>:5000/qr", jsonData, new ServerRequest.ResponseCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                if (response.has("response")) {
                                    Toast.makeText(MainActivity.this, "✅ Account verified!", Toast.LENGTH_SHORT).show();

                                    // Move to the amount entry page
                                    Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
//                                    intent.putExtra("sender", senderAccount);
                                    intent.putExtra("receiver", rawValue);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(MainActivity.this, "❌ Error: " + response.optString("error"), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(String error) {
                                Toast.makeText(MainActivity.this, "🚨 Connection error: " + error, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "⚠️ Scan failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("QRScanner", "Error processing QR code", e);
                    }
                })
                .addOnCanceledListener(() ->
                        Toast.makeText(MainActivity.this, "🚫 Scanning cancelled", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "❌ Scanning failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("QRScanner", "Scanning error", e);
                });
    }

}