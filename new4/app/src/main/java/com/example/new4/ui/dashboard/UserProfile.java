package com.example.new4.ui.dashboard;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserProfile implements Serializable {
    @SerializedName("Account") public String account;
    @SerializedName("Private Key") public String privateKey;
    @SerializedName("Balance") public double balance;
    @SerializedName("Name") public String name;
    @SerializedName("Phone no.") public String phone;
    @SerializedName("Ration card no.") public String ration;
    @SerializedName("Mail id") public String email;
    @SerializedName("PIN") public int pin;

    // Getters
    public String getAccount() { return account; }
    public String getPrivateKey() { return privateKey; }
    public double getBalance() { return balance; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getRationCard() { return ration; }
    public String getEmail() { return email; }
    public int getPin() { return pin; }

    // Setters
    public void setAccount(String account) { this.account = account; }
    public void setPrivateKey(String privateKey) { this.privateKey = privateKey; }
    public void setBalance(double balance) { this.balance = balance; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRationCard(String ration) { this.ration = ration; }
    public void setEmail(String email) { this.email = email; }
    public void setPin(int pin) { this.pin = pin; }
}