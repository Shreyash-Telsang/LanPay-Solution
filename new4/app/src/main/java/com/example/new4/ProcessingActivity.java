package com.example.new4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProcessingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView statusText;
//    private Button btnRetry;

    private String sender, receiver, amount;

    private static final int PROCESSING_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        progressBar = findViewById(R.id.progressBar);
        statusText = findViewById(R.id.statusText);
//        btnRetry = findViewById(R.id.btnRetry);

        // Extract payment details
        Intent intent = getIntent();
        sender = intent.getStringExtra("sender");
        receiver = intent.getStringExtra("receiver");
        amount = intent.getStringExtra("amount");

//        btnRetry.setOnClickListener(view -> {
////            btnRetry.setVisibility(View.GONE);
//            statusText.setText("Retrying transaction...");
//            progressBar.setVisibility(View.VISIBLE);
//            sendTransactionRequest();
//        });

        sendTransactionRequest();
    }

    private void sendTransactionRequest() {
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("amount", amount);
        jsonData.put("sender", sender);
        jsonData.put("receiver", receiver);

        ServerRequest.sendPostRequest(this, Constants.BASE_URL +"/transaction", jsonData, new ServerRequest.ResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.has("Success")) {
                    String txHash = response.optString("Success");
                    statusText.setText("✅ Transaction successful!");
                    new Handler().postDelayed(() -> {
                        Intent successIntent = new Intent(ProcessingActivity.this, PaymentSuccessActivity.class);
                        successIntent.putExtra("sender", sender);
                        successIntent.putExtra("receiver", receiver);
                        successIntent.putExtra("amount", amount);
                        successIntent.putExtra("txHash", txHash);
                        Toast.makeText(ProcessingActivity.this, txHash, Toast.LENGTH_SHORT).show();
                        startActivity(successIntent);
                        finish();
                    }, 1000);
                } else {
                    showFailure("❌ Transaction failed: " + response.optString("error", "Unknown error"));
                }
            }

            @Override
            public void onFailure(String error) {
                showFailure("🚨 Server Error: " + error);
            }
        });

        // Timeout handler
        new Handler().postDelayed(() -> {
            if (!isFinishing()) {
                showFailure("⚠️ Transaction timed out. Try again.");
            }
        }, PROCESSING_TIMEOUT);
    }

    private void showFailure(String message) {
        statusText.setText(message);
        progressBar.setVisibility(View.GONE);
//        btnRetry.setVisibility(View.VISIBLE);
    }
}
