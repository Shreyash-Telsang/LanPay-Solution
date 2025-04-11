package com.example.new4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MpinVerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvPaymentInfo, tvMpinError, tvForgotMpin;
    private TextView[] mpinDigits = new TextView[4];
    private ImageButton btnBack;
    private Button[] numberButtons = new Button[10];
    private Button btnClear, btnBackspace;
    private StringBuilder enteredMpin = new StringBuilder();

    private String amount, sender, receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin_verification);

        getPaymentDetailsFromIntent();
        initializeViews();
        setupClickListeners();
        updatePaymentInfoText();
    }

    private void getPaymentDetailsFromIntent() {
        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
//        sender = intent.getStringExtra("sender");
        receiver = intent.getStringExtra("receiver");
    }

    private void initializeViews() {
        tvPaymentInfo = findViewById(R.id.tv_payment_info);
        tvMpinError = findViewById(R.id.tv_mpin_error);
        tvForgotMpin = findViewById(R.id.tv_forgot_mpin);
        btnBack = findViewById(R.id.btn_back_mpin);

        for (int i = 0; i < 4; i++) {
            int resID = getResources().getIdentifier("mpin_digit_" + (i + 1), "id", getPackageName());
            mpinDigits[i] = findViewById(resID);
        }

        for (int i = 0; i < 10; i++) {
            int resID = getResources().getIdentifier("btn_" + i, "id", getPackageName());
            numberButtons[i] = findViewById(resID);
        }

        btnClear = findViewById(R.id.btn_clear);
        btnBackspace = findViewById(R.id.btn_backspace);
    }

    private void setupClickListeners() {
        for (Button button : numberButtons) {
            button.setOnClickListener(this);
        }
        btnClear.setOnClickListener(this);
        btnBackspace.setOnClickListener(this);
        tvForgotMpin.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void updatePaymentInfoText() {
        tvPaymentInfo.setText("Enter your MPIN to complete payment of ₹" + amount + " to " + receiver);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back_mpin) {
            finish();
            return;
        }
        if (id == R.id.tv_forgot_mpin) {
            Toast.makeText(this, "Forgot MPIN? Feature coming soon!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id == R.id.btn_clear) {
            clearMpin();
            return;
        }
        if (id == R.id.btn_backspace) {
            removeLastDigit();
            return;
        }
        for (int i = 0; i < 10; i++) {
            if (id == numberButtons[i].getId()) {
                addMpinDigit(String.valueOf(i));
                return;
            }
        }
    }

    private void addMpinDigit(String digit) {
        if (enteredMpin.length() < 4) {
            enteredMpin.append(digit);
            updateMpinDisplay();
            if (enteredMpin.length() == 4) {
                verifyMpin();
            }
        }
    }

    private void removeLastDigit() {
        if (enteredMpin.length() > 0) {
            enteredMpin.deleteCharAt(enteredMpin.length() - 1);
            updateMpinDisplay();
            tvMpinError.setVisibility(View.GONE);
        }
    }

    private void clearMpin() {
        enteredMpin.setLength(0);
        updateMpinDisplay();
        tvMpinError.setVisibility(View.GONE);
    }

    private void updateMpinDisplay() {
        for (int i = 0; i < 4; i++) {
            mpinDigits[i].setText(i < enteredMpin.length() ? "*" : "");
        }
    }

    private void verifyMpin() {
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("pin", enteredMpin.toString());

        ServerRequest.sendPostRequest(this, "http://<your_server_ip>:5000/pin", jsonData, new ServerRequest.ResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.has("Success")) {
                    handleCorrectMpin();
                } else {
                    handleIncorrectMpin();
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MpinVerificationActivity.this, "🚨 PIN Verification Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleCorrectMpin() {
        Toast.makeText(this, "✅ MPIN verified successfully!", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(this::navigateToTransaction, 500);
    }

    private void handleIncorrectMpin() {
        tvMpinError.setVisibility(View.VISIBLE);
        tvMpinError.setText("❌ Incorrect MPIN! Try again.");
        new Handler().postDelayed(this::clearMpin, 1000);
    }

    private void navigateToTransaction() {
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("amount", amount);
//        jsonData.put("sender", sender);
        jsonData.put("receiver", receiver);

        ServerRequest.sendPostRequest(this, "http://<your_server_ip>:5000/transaction", jsonData, new ServerRequest.ResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.has("Success")) {
                    Toast.makeText(MpinVerificationActivity.this, "✅ Transaction successful!", Toast.LENGTH_SHORT).show();

                    // Move to Transaction Completion Page
                    Intent intent = new Intent(MpinVerificationActivity.this, TransactionActivity.class);
                    intent.putExtra("amount", amount);
//                    intent.putExtra("sender", sender);
                    intent.putExtra("receiver", receiver);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MpinVerificationActivity.this, "❌ Transaction failed: " + response.optString("error"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MpinVerificationActivity.this, "🚨 Server error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
