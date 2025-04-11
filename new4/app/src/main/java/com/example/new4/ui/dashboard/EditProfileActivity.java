package com.example.new4.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.new4.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private UserProfile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentProfile = (UserProfile) getIntent().getSerializableExtra("profile");
        if (currentProfile == null) {
            Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        populateFields();
        setupListeners();
    }

    private void populateFields() {
        binding.etName.setText(currentProfile.getName());
        binding.etPhone.setText(currentProfile.getPhone());
        binding.etRationCard.setText(currentProfile.getRationCard());
        binding.etEmail.setText(currentProfile.getEmail());
        binding.etMipn.setText(String.valueOf(currentProfile.getPin()));
    }

    private void setupListeners() {
        binding.btnSave.setOnClickListener(v -> saveProfile());
        binding.btnCancel.setOnClickListener(v -> finish());
    }

    private void saveProfile() {
        // Validate inputs
        if (binding.etName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Update profile
            currentProfile.setName(binding.etName.getText().toString());
            currentProfile.setPhone(binding.etPhone.getText().toString());
            currentProfile.setRationCard(binding.etRationCard.getText().toString());
            currentProfile.setEmail(binding.etEmail.getText().toString());
            currentProfile.setPin(Integer.parseInt(binding.etMipn.getText().toString()));

            // Return result
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedProfile", currentProfile);
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid PIN format", Toast.LENGTH_SHORT).show();
        }
    }
}