package com.example.new4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class PaymentSuccessActivity extends AppCompatActivity {

    private TextView tvSuccessAmount;
    private TextView tvSuccessRecipient;
    private TextView tvSuccessDate;
    private TextView tvSuccessTransactionId;
    private TextView tvSuccessNote;
    private LinearLayout layoutNote;
    private Button btnShareReceipt;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        initializeViews();
        populateData();
        setupListeners();
    }

    private void initializeViews() {
        tvSuccessAmount = findViewById(R.id.tv_success_amount);
        tvSuccessRecipient = findViewById(R.id.tv_success_recipient);
        tvSuccessDate = findViewById(R.id.tv_success_date);
        tvSuccessTransactionId = findViewById(R.id.tv_success_transaction_id);
        tvSuccessNote = findViewById(R.id.tv_success_note);
        layoutNote = findViewById(R.id.layout_note);
        btnShareReceipt = findViewById(R.id.btn_share_receipt);
        btnDone = findViewById(R.id.btn_done);
    }

    private void populateData() {
        // Get data from intent
        Intent intent = getIntent();

        // Set amount
        String amount = intent.getStringExtra("amount");
        if (amount != null) {
            tvSuccessAmount.setText(amount);
        }

        // Set recipient
        String recipient = intent.getStringExtra("recipient");
        if (recipient != null) {
            tvSuccessRecipient.setText(recipient);
        }

        // Set date (current date)
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        tvSuccessDate.setText(currentDate);

        // Set transaction ID (generate a random one)
        String transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        tvSuccessTransactionId.setText(transactionId);

        // Set note if available
        String note = intent.getStringExtra("note");
        if (note != null && !note.isEmpty()) {
            tvSuccessNote.setText(note);
            layoutNote.setVisibility(View.VISIBLE);
        } else {
            layoutNote.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        btnShareReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareReceipt();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to home screen
                Intent homeIntent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
            }
        });
    }

    private void shareReceipt() {
        // Create receipt text
        StringBuilder receiptBuilder = new StringBuilder();
        receiptBuilder.append("Payment Receipt\n\n");
        receiptBuilder.append("Amount: ").append(tvSuccessAmount.getText()).append("\n");
        receiptBuilder.append("To: ").append(tvSuccessRecipient.getText()).append("\n");
        receiptBuilder.append("Date: ").append(tvSuccessDate.getText()).append("\n");
        receiptBuilder.append("Transaction ID: ").append(tvSuccessTransactionId.getText()).append("\n");

        if (layoutNote.getVisibility() == View.VISIBLE) {
            receiptBuilder.append("Note: ").append(tvSuccessNote.getText()).append("\n");
        }

        // Create share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Payment Receipt");
        shareIntent.putExtra(Intent.EXTRA_TEXT, receiptBuilder.toString());

        // Start activity
        startActivity(Intent.createChooser(shareIntent, "Share Receipt"));
    }
}