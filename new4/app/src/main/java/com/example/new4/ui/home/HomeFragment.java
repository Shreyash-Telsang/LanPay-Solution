package com.example.new4.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.new4.Constants;
import com.example.new4.PayAccountActivity;
import com.example.new4.PaymentActivity;
//import com.example.new4.QR.QRScannerActivity;
import com.example.new4.ServerRequest;
import com.example.new4.TransactionActivity;
import com.example.new4.databinding.FragmentHomeBinding;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final int CAMERA_PERMISSION_REQUEST = 1001;
    private GmsBarcodeScanner scanner;
    private String senderAccount = "user123"; // Replace with actual user account

    private final String senderAddress = "0x15d34AAf54267DB7D7c367839AAf71A00a2C6A65";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize QR scanner
        scanner = GmsBarcodeScanning.getClient(requireActivity());

        // Set up button click listeners
        setupButtonClickListeners();

        return root;
    }

    private void setupButtonClickListeners() {

        // Camera Button - Direct QR scanning
        binding.camera2.setOnClickListener(v -> {
            checkCameraPermissionAndScan();
        });
    }

    private void checkCameraPermissionAndScan() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startQRScan();
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
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
                Toast.makeText(requireContext(), "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startQRScan() {
        try {
            scanner.startScan()
                    .addOnSuccessListener(barcode -> {
                        try {
                            Log.d("BV", "AAt ja");
                            String rawValue = barcode.getRawValue();
                            if (rawValue == null || rawValue.isEmpty()) {
                                Toast.makeText(requireContext(), "Invalid QR Code", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String receiverAddress = rawValue;
                            Log.d("Scanned", "QR Code scanned: " + rawValue);

                            // Make sure context is still valid
                            if (isAdded() && getContext() != null) {
                                Map<String, String> data = new HashMap<>();
                                data.put("from", senderAddress);
                                data.put("to", receiverAddress);

                                ServerRequest.sendPostRequest(requireContext(), Constants.BASE_URL+"/qr", data, new ServerRequest.ResponseCallback() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        try {
                                            Log.d("QRScan", "Server response: " + response.toString());
                                            if (response.has("Success")) {
                                                Toast.makeText(requireContext(), "Accounts verified", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(requireContext(), PaymentActivity.class);
                                                intent.putExtra("from", senderAddress);
                                                intent.putExtra("to", receiverAddress);
                                                startActivity(intent);
                                            } else if (response.has("error")) {
                                                Toast.makeText(requireContext(), response.optString("error"), Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception e) {
                                            Log.e("QRScan", "Error in onSuccess callback", e);
                                            Toast.makeText(requireContext(), "Processing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        Log.e("QRScan", "Server request failed: " + error);
                                        Toast.makeText(requireContext(), "Network Error 23: " + error, Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Log.e("QRScan", "Fragment no longer attached to context");
                            }
                        } catch (Exception e) {
                            Log.e("QRScan", "Error processing scanned barcode", e);
                            Toast.makeText(requireContext(), "Processing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnCanceledListener(() -> {
                        Log.d("QRScan", "Scanning cancelled by user");
                        Toast.makeText(requireContext(), "Scanning cancelled", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("QRScan", "Scanning failed", e);
                        Toast.makeText(requireContext(), "Scanning failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            Log.e("QRScan", "Error starting QR scan", e);
            Toast.makeText(requireContext(), "Failed to start scanner: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}

//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//    private static final int CAMERA_PERMISSION_REQUEST = 1001;
//    private GmsBarcodeScanner scanner;
//    private static final String TAG = "HomeFragment"; // Consistent log tag
//    private final String senderAddress = "0x15d34AAf54267DB7D7c367839AAf71A00a2C6A65";
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        // Initialize QR scanner
//        scanner = GmsBarcodeScanning.getClient(requireActivity());
//
//        // Set up button click listeners
//        setupButtonClickListeners();
//
//        return root;
//    }
//
//    private void setupButtonClickListeners() {
//        binding.TransactionHistory.setOnClickListener(v -> {
//            startActivity(new Intent(requireActivity(), TransactionActivity.class));
//        });
//
//        binding.PayAccountNo.setOnClickListener(v -> {
//            startActivity(new Intent(requireActivity(), PayAccountActivity.class));
//        });
//
//        binding.camera2.setOnClickListener(v -> {
//            Log.d(TAG, "QR scan button clicked");
//            checkCameraPermissionAndScan();
//        });
//    }
//
//    private void checkCameraPermissionAndScan() {
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, "Camera permission granted, starting scan");
//            startQRScan();
//        } else {
//            Log.d(TAG, "Requesting camera permission");
//            ActivityCompat.requestPermissions(requireActivity(),
//                    new String[]{Manifest.permission.CAMERA},
//                    CAMERA_PERMISSION_REQUEST);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAMERA_PERMISSION_REQUEST) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "Camera permission granted after request");
//                startQRScan();
//            } else {
//                Log.d(TAG, "Camera permission denied");
//                Toast.makeText(requireContext(), "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void startQRScan() {
//        Log.d(TAG, "Starting QR scan");
//        scanner.startScan()
//                .addOnSuccessListener(barcode -> {
//                    Log.d(TAG, "QR scan successful");
//                    String rawValue = barcode.getRawValue();
//                    if (rawValue == null || rawValue.isEmpty()) {
//                        Log.d(TAG, "Empty QR code scanned");
//                        Toast.makeText(requireContext(), "Invalid QR Code", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    Log.d(TAG, "Scanned QR content: " + rawValue);
//                    String receiverAddress = rawValue;
//
//                    Map<String, String> data = new HashMap<>();
//                    data.put("from", senderAddress);
//                    data.put("to", receiverAddress);
//
//                    Log.d(TAG, "Sending verification request to server");
//                    ServerRequest.sendPostRequest(requireContext(),
//                            Constants.BASE_URL+"/qr",
//                            data,
//                            new ServerRequest.ResponseCallback() {
//                                @Override
//                                public void onSuccess(JSONObject response) {
//                                    Log.d(TAG, "Server response: " + response.toString());
//                                    try {
//                                        if (response.has("Success")) {
//                                            Log.d("Account", "Verified ");
//                                            Toast.makeText(requireContext(), "Accounts verified", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(requireContext(), PaymentActivity.class);
//                                            intent.putExtra("from", senderAddress);
//                                            intent.putExtra("to", receiverAddress);
//                                            startActivity(intent);
//                                        } else {
//                                            String error = response.optString("error", "Unknown error");
//                                            Log.e(TAG, "Server error: " + error);
//                                            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
//                                        }
//                                    } catch (Exception e) {
//                                        Log.e(TAG, "Error processing response", e);
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(String error) {
//                                    Log.e(TAG, "Network error: " + error);
//                                    Toast.makeText(requireContext(), "Network Error: " + error, Toast.LENGTH_LONG).show();
//                                }
//                            });
//                })
//                .addOnCanceledListener(() -> {
//                    Log.d(TAG, "QR scan cancelled by user");
//                    Toast.makeText(requireContext(), "Scanning cancelled", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "QR scan failed", e);
//                    Toast.makeText(requireContext(), "Scanning failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}