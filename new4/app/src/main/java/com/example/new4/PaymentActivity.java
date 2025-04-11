package com.example.new4;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private EditText etAmount;
    private TextView tvRecipientName;
    private TextView tvRecipientDetails;
    private EditText etNote;
    private Button btnPay;
    private ImageButton btnBack;

    private double maxBalance = 10000.00; // Example balance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        // Initialize views
        etAmount = findViewById(R.id.et_amount);
        tvRecipientName = findViewById(R.id.tv_recipient_name);
        tvRecipientDetails = findViewById(R.id.tv_recipient_details);
        etNote = findViewById(R.id.et_note);
        btnPay = findViewById(R.id.btn_pay);
        btnBack = findViewById(R.id.btn_back);

        // Get data from intent (if any)
        if (getIntent().hasExtra("recipient_name")) {
            tvRecipientName.setText(getIntent().getStringExtra("recipient_name"));
        }

        if (getIntent().hasExtra("recipient_account")) {
            String account = getIntent().getStringExtra("recipient_account");
            tvRecipientDetails.setText("Account: " + maskAccountNumber(account));
        }

        // Set up listeners
        setupAmountValidation();
        setupClickListeners();
    }

    private void setupAmountValidation() {
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String amountStr = s.toString();
                    if (!amountStr.isEmpty()) {
                        double amount = Double.parseDouble(amountStr);
                        // Validate if amount is within balance
                        btnPay.setEnabled(amount > 0 && amount <= maxBalance);
                    } else {
                        btnPay.setEnabled(false);
                    }
                } catch (NumberFormatException e) {
                    btnPay.setEnabled(false);
                }
            }
        });
    }

    private void setupClickListeners() {
        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Pay button
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToMpinVerification();
            }
        });
    }

    private void proceedToMpinVerification() {
        // Get payment details
        double amount;
        try {
            amount = Double.parseDouble(etAmount.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "❌ Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format amount for display
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String formattedAmount = formatter.format(amount);

        // Get recipient name & sender/receiver
        String recipientName = tvRecipientName.getText().toString();
        String note = etNote.getText().toString().trim();
//        String sender = getIntent().getStringExtra("sender");
        String receiver = getIntent().getStringExtra("recipient_account"); // Make sure to pass this

//        if (sender == null || receiver == null) {
//            Toast.makeText(this, "⚠️ Sender or Receiver details missing!", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // Send payment confirmation request
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("amount", Double.toString(amount));

        ServerRequest.sendPostRequest(PaymentActivity.this, "http://<your_server_ip>:5000/amount", jsonData, new ServerRequest.ResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.has("Success")) {
                    Toast.makeText(PaymentActivity.this, "✅ Amount confirmed!", Toast.LENGTH_SHORT).show();

                    // Move to MPIN verification page
                    Intent intent = new Intent(PaymentActivity.this, MpinVerificationActivity.class);
//                    intent.putExtra("sender", sender);
                    intent.putExtra("receiver", receiver);
                    intent.putExtra("amount", amount);
                    startActivity(intent);
                } else {
                    Toast.makeText(PaymentActivity.this, "❌ Error: " + response.optString("error"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(PaymentActivity.this, "🚨 Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ Fix for `maskAccountNumber()`
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 4) {
            return "XXXX"; // Prevent null issues
        }

        int visibleDigits = 4;
        int maskedLength = accountNumber.length() - visibleDigits;

        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < maskedLength; i++) {
            masked.append("X");
        }

        masked.append(accountNumber.substring(maskedLength));
        return masked.toString();
    }

}