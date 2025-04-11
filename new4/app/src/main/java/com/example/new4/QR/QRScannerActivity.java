package com.example.new4.QR;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.example.new4.databinding.QrScannerCodeBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRScannerActivity extends AppCompatActivity {

    private QrScannerCodeBinding binding;
    private final String userAddress = "0x15d34AAf54267DB7D7c367839AAf71A00a2C6A65";
    private final String userName = "Jay Patil";
    private Bitmap qrCodeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = QrScannerCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set user details
        binding.tvUserName.setText(userName);
        binding.tvAccountNo.setText(userAddress);

        // Generate QR code for the wallet address
        generateQRCode(userAddress);

        // Share button click listener
        binding.btnShare.setOnClickListener(v -> shareQRCode());
    }

    private void generateQRCode(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            // Create a bit matrix for the QR code
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);

            // Create a bitmap from the bit matrix
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            // Fill the bitmap with the QR code pattern
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrCodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            // Set the QR code to the ImageView
            binding.imageView2.setImageBitmap(qrCodeBitmap);

        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareQRCode() {
        if (qrCodeBitmap == null) {
            Toast.makeText(this, "No QR code to share", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Save bitmap to cache directory
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            File imageFile = new File(cachePath, "shared_qr_code.png");

            FileOutputStream stream = new FileOutputStream(imageFile);
            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            // Get content URI via FileProvider
            String authority = getPackageName() + ".fileprovider";
            android.net.Uri contentUri = FileProvider.getUriForFile(this, authority, imageFile);

            // Create share intent
            Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("image/png")
                    .setStream(contentUri)
                    .setChooserTitle("Share QR Code")
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(shareIntent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error sharing QR code", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}