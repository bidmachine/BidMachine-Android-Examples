package io.bidmachine.examples;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.bidmachine.AdContentType;
import io.bidmachine.BidMachine;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.examples.base.Status;
import io.bidmachine.examples.databinding.ActivityInterstitialBinding;
import io.bidmachine.interstitial.InterstitialAd;
import io.bidmachine.interstitial.InterstitialRequest;
import io.bidmachine.interstitial.SimpleInterstitialListener;
import io.bidmachine.utils.BMError;

public class InterstitialJavaActivity extends BaseJavaExampleActivity<ActivityInterstitialBinding> {

    @Nullable
    private InterstitialAd interstitialAd;
    @Nullable
    private InterstitialAd videoAd;
    @Nullable
    private InterstitialAd delayedShowInterstitialAd;

    @NonNull
    @Override
    protected ActivityInterstitialBinding inflate(@NonNull LayoutInflater inflater) {
        return ActivityInterstitialBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise SDK
        BidMachine.initialize(this, SOURCE_ID);

        // Enable logs
        BidMachine.setLoggingEnabled(true);

        // Set listeners to perform Ads load
        binding.bShowInterstitial.setOnClickListener(v -> showInterstitial());
        binding.bShowVideo.setOnClickListener(v -> showVideo());
        binding.bLoadInterstitial.setOnClickListener(v -> loadInterstitial());
        binding.bShowLoadedInterstitial.setOnClickListener(v -> showLoadedInterstitial());

    }

    private void showInterstitial() {
        setDebugState(Status.Loading);

        // Destroy previous loaded InterstitialAd
        destroyInterstitialAd();

        // Create new InterstitialRequest
        InterstitialRequest interstitialRequest = new InterstitialRequest.Builder().build();

        // Create new InterstitialAd
        interstitialAd = new InterstitialAd(this);

        // Set InterstitialAd events listener
        interstitialAd.setListener(new SimpleInterstitialListener() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd ad) {
                setDebugState(Status.Loaded, "Interstitial Ads Loaded");

                // Show Interstitial Ad
                ad.show();
            }

            @Override
            public void onAdLoadFailed(@NonNull InterstitialAd ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "Interstitial Ads Load Failed");

                // Destroy loaded ad since it not required any more
                destroyInterstitialAd();
            }

            @Override
            public void onAdClosed(@NonNull InterstitialAd ad, boolean finished) {
                setDebugState(Status.Closed, "Interstitial Ads Closed");

                // Destroy loaded ad since it not required any more
                destroyInterstitialAd();
            }
        });

        // Load InterstitialAd
        interstitialAd.load(interstitialRequest);
    }

    private void showVideo() {
        setDebugState(Status.Loading);

        // Destroy previous loaded InterstitialAd for Video
        destroyVideoAd();

        // Create new InterstitialRequest for Video
        InterstitialRequest interstitialRequest = new InterstitialRequest.Builder()
                .setAdContentType(AdContentType.Video) // Set required Interstitial content type
                .build();

        // Create new InterstitialAd for Video
        videoAd = new InterstitialAd(this);

        // Set InterstitialAd for Video events listener
        videoAd.setListener(new SimpleInterstitialListener() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd ad) {
                setDebugState(Status.Loaded, "Interstitial Ads Loaded");

                // Show Interstitial Ad for Video
                ad.show();
            }

            @Override
            public void onAdLoadFailed(@NonNull InterstitialAd ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "Interstitial Ads Load Failed");

                // Destroy loaded ad since it not required any more
                destroyVideoAd();
            }

            @Override
            public void onAdClosed(@NonNull InterstitialAd ad, boolean finished) {
                setDebugState(Status.Closed, "Interstitial Ads Closed");

                // Destroy loaded ad since it not required any more
                destroyVideoAd();
            }
        });
        // Load InterstitialAd for Video
        videoAd.load(interstitialRequest);
    }

    private void loadInterstitial() {
        setDebugState(Status.Loading);

        // Destroy previous loaded Interstitial Ads
        destroyDelayedShowInterstitial();

        // Create new InterstitialRequest
        InterstitialRequest interstitialRequest = new InterstitialRequest.Builder().build();

        // Create new InterstitialAd
        delayedShowInterstitialAd = new InterstitialAd(this);

        // Set InterstitialAd events listener
        delayedShowInterstitialAd.setListener(new SimpleInterstitialListener() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd ad) {
                setDebugState(Status.Loaded, "Interstitial Ads Loaded");
            }

            @Override
            public void onAdLoadFailed(@NonNull InterstitialAd ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "Interstitial Ads Load Failed");

                // Destroy loaded ad since it not required any more
                destroyDelayedShowInterstitial();
            }

            @Override
            public void onAdClosed(@NonNull InterstitialAd ad, boolean finished) {
                setDebugState(Status.Closed, "Interstitial Ads Closed");

                // Destroy loaded ad since it not required any more
                destroyDelayedShowInterstitial();
            }
        });

        // Load InterstitialAd
        delayedShowInterstitialAd.load(interstitialRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy Ads when you finish with it
        destroyInterstitialAd();
        destroyVideoAd();
        destroyDelayedShowInterstitial();
    }

    private void destroyInterstitialAd() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
            interstitialAd = null;
        }
    }

    private void destroyVideoAd() {
        if (videoAd != null) {
            videoAd.destroy();
            videoAd = null;
        }
    }

    private void destroyDelayedShowInterstitial() {
        if (delayedShowInterstitialAd != null) {
            delayedShowInterstitialAd.destroy();
            delayedShowInterstitialAd = null;
        }
    }

    private void showLoadedInterstitial() {
        if (delayedShowInterstitialAd == null) {
            toast("Load Interstitial First");
        } else if (!delayedShowInterstitialAd.isLoaded()) {
            toast("Interstitial not Loaded yet");
        } else {
            delayedShowInterstitialAd.show();
        }
    }

}