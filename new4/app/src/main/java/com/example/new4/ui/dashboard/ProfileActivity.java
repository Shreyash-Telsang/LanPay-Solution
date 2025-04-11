package com.example.new4.ui.dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.new4.databinding.ActivityProfileBinding;
import com.example.new4.ui.dashboard.DashboardViewModel;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private DashboardViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use view binding
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        profileViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Set up profile information observers
        setupProfileDataObservers();

        // Set up button click listeners
        setupButtonClickListeners();
    }

    private void setupProfileDataObservers() {
        // Observe user name
        profileViewModel.getName().observe(this, name -> {
            binding.tvUserName.setText(name);
        });

        // Observe account number
        profileViewModel.getAccountNo().observe(this, accountNo -> {
            binding.tvAccountNo.setText(accountNo);
        });

        // Observe private key
        profileViewModel.getPrivateKey().observe(this, privateKey -> {
            binding.tvPrivateKey.setText(privateKey);
        });

        // Observe balance
        profileViewModel.getBalance().observe(this, balance -> {
            binding.tvBalance.setText(balance);
        });

        // Observe phone number
        profileViewModel.getPhoneNo().observe(this, phoneNo -> {
            binding.tvPhoneNo.setText(phoneNo);
        });

        // Observe ration card
        profileViewModel.getRationCard().observe(this, rationCard -> {
            binding.tvRationCard.setText(rationCard);
        });

        // Observe email
        profileViewModel.getEmail().observe(this, email -> {
            binding.tvEmail.setText(email);
        });

        // Observe MIPN
        profileViewModel.getMipn().observe(this, mipn -> {
            binding.tvMipn.setText(mipn);
        });
    }

    private void setupButtonClickListeners() {
        // Edit Profile button
        binding.btnEditProfile.setOnClickListener(v -> {
            // Navigation to an Edit Profile activity
            Toast.makeText(this, "Edit Profile feature coming soon", Toast.LENGTH_SHORT).show();

            // Example of how you would navigate to an edit profile activity:
            // Intent intent = new Intent(this, EditProfileActivity.class);
            // startActivity(intent);
        });

        // Change Password button
        binding.btnChangePassword.setOnClickListener(v -> {
            // Navigation to a Change Password activity
            Toast.makeText(this, "Change Password feature coming soon", Toast.LENGTH_SHORT).show();

            // Example of how you would navigate to a change password activity:
            // Intent intent = new Intent(this, ChangePasswordActivity.class);
            // startActivity(intent);
        });
    }
}