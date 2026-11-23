package io.bidmachine.examples;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.bidmachine.AdLoadCallback;
import io.bidmachine.BMError;
import io.bidmachine.BidMachine;
import io.bidmachine.BidMachineInstance;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.examples.base.Status;
import io.bidmachine.examples.databinding.ActivityInterstitialBinding;
import io.bidmachine.format.interstitialad.InterstitialAd;
import io.bidmachine.format.interstitialad.InterstitialListener;

public class InterstitialJavaActivity extends BaseJavaExampleActivity<ActivityInterstitialBinding> {

    @Nullable
    private BidMachineInstance bidMachineInstance = null;
    @Nullable
    private InterstitialAd interstitialAd = null;

    @NonNull
    @Override
    protected ActivityInterstitialBinding inflate(@NonNull LayoutInflater inflater) {
        return ActivityInterstitialBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable logs
        BidMachine.setLoggingEnabled(true);

        // Initialize SDK
        bidMachineInstance = BidMachine.initialize(this, SOURCE_ID);

        // Set listeners to perform Ads load and show
        binding.bLoadInterstitial.setOnClickListener(v -> loadAd());
        binding.bShowInterstitial.setOnClickListener(v -> showAd());
    }

    private void loadAd() {
        setDebugState(Status.Loading);

        BidMachineInstance bidMachineInstance = this.bidMachineInstance;
        if (bidMachineInstance != null) {
            bidMachineInstance.loadInterstitial(new InterstitialAdLoadCallback());
        } else {
            toast("Initialize BidMachine first!");
        }
    }

    private void showAd() {
        InterstitialAd interstitialAd = this.interstitialAd;
        if (interstitialAd == null) {
            toast("Load Interstitial First");
        } else if (!interstitialAd.canShow()) {
            toast("Interstitial can't be shown");
        } else {
            interstitialAd.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy Ads when you finish with it
        destroyInterstitialAd();
    }

    private void destroyInterstitialAd() {
        InterstitialAd interstitialAd = this.interstitialAd;
        if (interstitialAd != null) {
            interstitialAd.destroy();
            this.interstitialAd = null;
        }
    }


    private class InterstitialAdLoadCallback implements AdLoadCallback<InterstitialAd> {

        @Override
        public void onAdLoaded(@NonNull InterstitialAd ad) {
            setDebugState(Status.Loaded, "Interstitial Ads Loaded");

            // Destroy previous loaded InterstitialAd
            destroyInterstitialAd();

            // Keep a reference to InterstitialAd: you need it to show the ad,
            // listen to its events and destroy it when you're done
            interstitialAd = ad;
            ad.setListener(new InterstitialAdListener());
        }

        @Override
        public void onAdLoadFailed(@NonNull BMError bmError) {
            setDebugState(Status.LoadFail, "Interstitial Ads Load Failed");
        }

    }

    private class InterstitialAdListener implements InterstitialListener {

        @Override
        public void onAdPresented(@NonNull InterstitialAd ad) {
            toast("Interstitial Ads Presented");
        }

        @Override
        public void onAdShowFailed(@NonNull InterstitialAd ad, @NonNull BMError bmError) {
            setDebugState(Status.ShowFail, "Interstitial Ads Show Failed");

            // Destroy ad since it not required any more
            destroyInterstitialAd();
        }

        @Override
        public void onAdImpression(@NonNull InterstitialAd ad) {
            setDebugState(Status.Impression, "Interstitial Ads Impression");
        }

        @Override
        public void onAdClicked(@NonNull InterstitialAd ad) {
            setDebugState(Status.Clicked, "Interstitial Ads Clicked");
        }

        @Override
        public void onAdExpired(@NonNull InterstitialAd ad) {
            setDebugState(Status.Expired, "Interstitial Ads Expired");

            // Destroy ad since it not required any more
            destroyInterstitialAd();
        }

        @Override
        public void onAdClosed(@NonNull InterstitialAd ad) {
            setDebugState(Status.Closed, "Interstitial Ads Closed");

            // Destroy ad since it not required any more
            destroyInterstitialAd();
        }

    }

}