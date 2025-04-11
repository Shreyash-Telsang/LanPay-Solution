package com.example.new4.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mName;
    private final MutableLiveData<String> mAccountNo;
    private final MutableLiveData<String> mPrivateKey;
    private final MutableLiveData<String> mBalance;
    private final MutableLiveData<String> mPhoneNo;
    private final MutableLiveData<String> mRationCard;
    private final MutableLiveData<String> mEmail;
    private final MutableLiveData<String> mMipn;

    public DashboardViewModel() {
        // Initialize user profile data with sample values
        mName = new MutableLiveData<>();
        mName.setValue("John Doe");

        mAccountNo = new MutableLiveData<>();
        mAccountNo.setValue("9876543210123456");

        mPrivateKey = new MutableLiveData<>();
        mPrivateKey.setValue("h7Kl9pP3qR2sT1vU");

        mBalance = new MutableLiveData<>();
        mBalance.setValue("₹10,000.00");

        mPhoneNo = new MutableLiveData<>();
        mPhoneNo.setValue("+91 98765 43210");

        mRationCard = new MutableLiveData<>();
        mRationCard.setValue("RAT123456789");

        mEmail = new MutableLiveData<>();
        mEmail.setValue("john.doe@example.com");

        mMipn = new MutableLiveData<>();
        mMipn.setValue("MIPN7654321");
    }

    public LiveData<String> getName() {
        return mName;
    }

    public LiveData<String> getAccountNo() {
        return mAccountNo;
    }

    public LiveData<String> getPrivateKey() {
        return mPrivateKey;
    }

    public LiveData<String> getBalance() {
        return mBalance;
    }

    public LiveData<String> getPhoneNo() {
        return mPhoneNo;
    }

    public LiveData<String> getRationCard() {
        return mRationCard;
    }

    public LiveData<String> getEmail() {
        return mEmail;
    }

    public LiveData<String> getMipn() {
        return mMipn;
    }

    // Methods to update profile data
    public void updateName(String name) {
        mName.setValue(name);
    }

    public void updateAccountNo(String accountNo) {
        mAccountNo.setValue(accountNo);
    }

    public void updatePrivateKey(String privateKey) {
        mPrivateKey.setValue(privateKey);
    }

    public void updateBalance(String balance) {
        mBalance.setValue(balance);
    }

    public void updatePhoneNo(String phoneNo) {
        mPhoneNo.setValue(phoneNo);
    }

    public void updateRationCard(String rationCard) {
        mRationCard.setValue(rationCard);
    }

    public void updateEmail(String email) {
        mEmail.setValue(email);
    }

    public void updateMipn(String mipn) {
        mMipn.setValue(mipn);
    }
}