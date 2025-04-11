package com.example.new4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.new4.Validation.AESUtils;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRScannerActivity extends AppCompatActivity {
    static final int CAMERA_PERMISSION_REQUEST = 1001;
    private GmsBarcodeScanner scanner;
    private final String AES_KEY = "YourSecretKey1234567890123456";
    private TextView textView;
    private EditText etInputText;
    private ImageView ivQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner_code);

        // Initialize UI elements
        textView = findViewById(R.id.textView);
        Button scanButton = findViewById(R.id.camera2);
        etInputText = findViewById(R.id.etInputText);
        Button btnGenerateQR = findViewById(R.id.btnGenerateQR);
        ivQRCode = findViewById(R.id.ivQRCode);

        // Initialize QR scanner
        scanner = GmsBarcodeScanning.getClient(this);

        // Check if we have a scanned result from MainActivity
        String scannedResult = getIntent().getStringExtra("SCANNED_RESULT");
        if (scannedResult != null && !scannedResult.isEmpty()) {
            textView.setText("Result: " + scannedResult);
            etInputText.setText(scannedResult);
        }

        // Set click listeners
        scanButton.setOnClickListener(v -> checkCameraPermissionAndScan());
        btnGenerateQR.setOnClickListener(v -> generateEncryptedQR());
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
                    try {
                        String decryptedText = AESUtils.decrypt(rawValue, AES_KEY);
                        textView.setText("Decrypted: " + decryptedText);
                    } catch (Exception e) {
                        textView.setText("Scanned: " + rawValue);
                    }
                })
                .addOnCanceledListener(() ->
                        Toast.makeText(this, "Scanning cancelled", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Scanning failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("QRScanner", "Scanning error", e);
                });
    }

    private void generateEncryptedQR() {
        String inputText = etInputText.getText().toString().trim();
        if (!inputText.isEmpty()) {
            try {
                String encryptedText = AESUtils.encrypt(inputText, AES_KEY);
                generateQRCode(encryptedText);
                Toast.makeText(this, "Encrypted QR generated", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error generating QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter text first", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateQRCode(String text) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ivQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "QR generation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}