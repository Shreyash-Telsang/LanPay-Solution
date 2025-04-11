package com.example.new4.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.new4.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up profile information observers
        setupProfileDataObservers();

        // Set up button click listeners
        setupButtonClickListeners();

        return root;
    }

    private void setupProfileDataObservers() {
        // Observe user name
        dashboardViewModel.getName().observe(getViewLifecycleOwner(), name -> {
            binding.tvUserName.setText(name);
        });

        // Observe account number
        dashboardViewModel.getAccountNo().observe(getViewLifecycleOwner(), accountNo -> {
            binding.tvAccountNo.setText(accountNo);
        });

        // Observe private key
        dashboardViewModel.getPrivateKey().observe(getViewLifecycleOwner(), privateKey -> {
            binding.tvPrivateKey.setText(privateKey);
        });

        // Observe balance
        dashboardViewModel.getBalance().observe(getViewLifecycleOwner(), balance -> {
            binding.tvBalance.setText(balance);
        });

        // Observe phone number
        dashboardViewModel.getPhoneNo().observe(getViewLifecycleOwner(), phoneNo -> {
            binding.tvPhoneNo.setText(phoneNo);
        });

        // Observe ration card
        dashboardViewModel.getRationCard().observe(getViewLifecycleOwner(), rationCard -> {
            binding.tvRationCard.setText(rationCard);
        });

        // Observe email
        dashboardViewModel.getEmail().observe(getViewLifecycleOwner(), email -> {
            binding.tvEmail.setText(email);
        });

        // Observe MIPN
        dashboardViewModel.getMipn().observe(getViewLifecycleOwner(), mipn -> {
            binding.tvMipn.setText(mipn);
        });
    }

    private void setupButtonClickListeners() {
        // Edit Profile button
        binding.btnEditProfile.setOnClickListener(v -> {
            // You can replace this with a navigation to an Edit Profile activity
            Toast.makeText(requireContext(), "Edit Profile feature coming soon", Toast.LENGTH_SHORT).show();

            // Example of how you would navigate to an edit profile activity:
            // Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            // startActivity(intent);
        });

        // Change Password button
        binding.btnChangePassword.setOnClickListener(v -> {
            // You can replace this with a navigation to a Change Password activity
            Toast.makeText(requireContext(), "Change Password feature coming soon", Toast.LENGTH_SHORT).show();

            // Example of how you would navigate to a change password activity:
            // Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
            // startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}