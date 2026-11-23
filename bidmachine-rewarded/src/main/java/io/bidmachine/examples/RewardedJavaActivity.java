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
import io.bidmachine.examples.databinding.ActivityRewardedBinding;
import io.bidmachine.format.rewardedad.RewardedAd;
import io.bidmachine.format.rewardedad.RewardedListener;

public class RewardedJavaActivity extends BaseJavaExampleActivity<ActivityRewardedBinding> {

    @Nullable
    private BidMachineInstance bidMachineInstance = null;
    @Nullable
    private RewardedAd rewardedAd = null;

    @NonNull
    @Override
    protected ActivityRewardedBinding inflate(@NonNull LayoutInflater inflater) {
        return ActivityRewardedBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable logs
        BidMachine.setLoggingEnabled(true);

        // Initialize SDK
        bidMachineInstance = BidMachine.initialize(this, SOURCE_ID);

        // Set listeners to perform Ads load and show
        binding.bLoadRewarded.setOnClickListener(v -> loadAd());
        binding.bShowRewarded.setOnClickListener(v -> showAd());
    }

    private void loadAd() {
        setDebugState(Status.Loading);

        BidMachineInstance bidMachineInstance = this.bidMachineInstance;
        if (bidMachineInstance != null) {
            bidMachineInstance.loadRewarded(new RewardedAdLoadCallback());
        } else {
            toast("Initialize BidMachine first!");
        }
    }

    private void showAd() {
        RewardedAd rewardedAd = this.rewardedAd;
        if (rewardedAd == null) {
            toast("Load Rewarded First");
        } else if (!rewardedAd.canShow()) {
            toast("Rewarded can't be shown");
        } else {
            rewardedAd.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy Ads when you finish with it
        destroyRewardedAd();
    }

    private void destroyRewardedAd() {
        RewardedAd rewardedAd = this.rewardedAd;
        if (rewardedAd != null) {
            rewardedAd.destroy();
            this.rewardedAd = null;
        }
    }


    private class RewardedAdLoadCallback implements AdLoadCallback<RewardedAd> {

        @Override
        public void onAdLoaded(@NonNull RewardedAd ad) {
            setDebugState(Status.Loaded, "Rewarded Ads Loaded");

            // Destroy previous loaded RewardedAd
            destroyRewardedAd();

            // Keep a reference to RewardedAd: you need it to show the ad,
            // listen to its events and destroy it when you're done
            rewardedAd = ad;
            ad.setListener(new RewardedAdListener());
        }

        @Override
        public void onAdLoadFailed(@NonNull BMError bmError) {
            setDebugState(Status.LoadFail, "Rewarded Ads Load Failed");
        }

    }

    private class RewardedAdListener implements RewardedListener {

        @Override
        public void onAdPresented(@NonNull RewardedAd ad) {
            toast("Rewarded Ads Presented");
        }

        @Override
        public void onAdShowFailed(@NonNull RewardedAd ad, @NonNull BMError bmError) {
            setDebugState(Status.ShowFail, "Rewarded Ads Show Failed");

            // Destroy ad since it not required any more
            destroyRewardedAd();
        }

        @Override
        public void onAdImpression(@NonNull RewardedAd ad) {
            setDebugState(Status.Impression, "Rewarded Ads Impression");
        }

        @Override
        public void onAdClicked(@NonNull RewardedAd ad) {
            setDebugState(Status.Clicked, "Rewarded Ads Clicked");
        }

        @Override
        public void onAdExpired(@NonNull RewardedAd ad) {
            setDebugState(Status.Expired, "Rewarded Ads Expired");

            // Destroy ad since it not required any more
            destroyRewardedAd();
        }

        @Override
        public void onAdRewarded(@NonNull RewardedAd ad) {
            setDebugState(Status.Rewarded, "Rewarded Ads Rewarded");

            // Here you can start you reward process
        }

        @Override
        public void onAdClosed(@NonNull RewardedAd ad) {
            setDebugState(Status.Closed, "Rewarded Ads Closed");

            // Destroy ad since it not required any more
            destroyRewardedAd();
        }

    }

}