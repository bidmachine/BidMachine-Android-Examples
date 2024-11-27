package io.bidmachine.examples;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.bidmachine.BidMachine;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.examples.base.Status;
import io.bidmachine.examples.databinding.ActivityRewardedBinding;
import io.bidmachine.rewarded.RewardedAd;
import io.bidmachine.rewarded.RewardedRequest;
import io.bidmachine.rewarded.SimpleRewardedListener;
import io.bidmachine.utils.BMError;

public class RewardedJavaActivity extends BaseJavaExampleActivity<ActivityRewardedBinding> {

    @Nullable
    private RewardedAd rewardedAd;
    @Nullable
    private RewardedAd delayedShowRewardedAd;

    @NonNull
    @Override
    protected ActivityRewardedBinding inflate(@NonNull LayoutInflater inflater) {
        return ActivityRewardedBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise SDK
        BidMachine.initialize(this, SOURCE_ID);

        // Enable logs
        BidMachine.setLoggingEnabled(true);

        // Set listeners to perform Ads load
        binding.bShowRewarded.setOnClickListener(v -> showRewarded());
        binding.bLoadRewarded.setOnClickListener(v -> loadRewarded());
        binding.bShowLoadedRewarded.setOnClickListener(v -> showLoadedRewarded());
    }

    private void showRewarded() {
        setDebugState(Status.Loading);

        // Destroy previous loaded RewardedAd
        destroyRewarded();

        // Create new RewardedRequest
        RewardedRequest rewardedRequest = new RewardedRequest.Builder().build();

        // Create new RewardedAd
        rewardedAd = new RewardedAd(this);

        // Set RewardedAd events listener
        rewardedAd.setListener(new SimpleRewardedListener() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                setDebugState(Status.Loaded, "Rewarded Ads Loaded");

                // Show RewardedAd
                ad.show();
            }

            @Override
            public void onAdLoadFailed(@NonNull RewardedAd ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "Rewarded Ads Load Failed");

                // Destroy loaded ad since it not required any more
                destroyRewarded();
            }

            @Override
            public void onAdClosed(@NonNull RewardedAd ad, boolean finished) {
                setDebugState(Status.Closed, "Rewarded Ads Closed");

                // Destroy loaded ad since it not required any more
                destroyRewarded();
            }

            @Override
            public void onAdRewarded(@NonNull RewardedAd ad) {
                setDebugState(Status.Rewarded, "Rewarded Ads Rewarded");

                // Here you can start you reward process
            }
        });

        // Load RewardedAd
        rewardedAd.load(rewardedRequest);
    }

    private void loadRewarded() {
        setDebugState(Status.Loading);

        // Destroy previous loaded RewardedAd
        destroyDelayedShowRewarded();

        // Create new RewardedRequest
        RewardedRequest rewardedRequest = new RewardedRequest.Builder().build();

        // Create new RewardedAd
        delayedShowRewardedAd = new RewardedAd(this);

        // Set RewardedAd events listener
        delayedShowRewardedAd.setListener(new SimpleRewardedListener() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                setDebugState(Status.Loaded, "Rewarded Ads Loaded");
            }

            @Override
            public void onAdLoadFailed(@NonNull RewardedAd ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "Rewarded Ads Load Failed");

                // Destroy loaded ad since it not required any more
                destroyDelayedShowRewarded();
            }

            @Override
            public void onAdClosed(@NonNull RewardedAd ad, boolean finished) {
                setDebugState(Status.Closed, "Rewarded Ads Closed");

                // Destroy loaded ad since it not required any more
                destroyDelayedShowRewarded();
            }

            @Override
            public void onAdRewarded(@NonNull RewardedAd ad) {
                setDebugState(Status.Rewarded, "Rewarded Ads Rewarded");

                // Here you can start you reward process
            }
        });

        // Load Rewarded
        delayedShowRewardedAd.load(rewardedRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy Ads when you finish with it
        destroyRewarded();
        destroyDelayedShowRewarded();
    }

    private void destroyRewarded() {
        if (rewardedAd != null) {
            rewardedAd.destroy();
            rewardedAd = null;
        }
    }

    private void destroyDelayedShowRewarded() {
        if (delayedShowRewardedAd != null) {
            delayedShowRewardedAd.destroy();
            delayedShowRewardedAd = null;
        }
    }

    private void showLoadedRewarded() {
        if (delayedShowRewardedAd == null) {
            toast("Load Rewarded First");
        } else if (!delayedShowRewardedAd.isLoaded()) {
            toast("Rewarded not Loaded yet");
        } else {
            delayedShowRewardedAd.show();
        }
    }

}