package io.bidmachine.examples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import io.bidmachine.BidMachine;
import io.bidmachine.banner.BannerRequest;
import io.bidmachine.banner.BannerSize;
import io.bidmachine.banner.BannerView;
import io.bidmachine.banner.SimpleBannerListener;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.examples.base.Status;
import io.bidmachine.examples.databinding.ActivityBannerBinding;
import io.bidmachine.utils.BMError;

public class BannerJavaActivity extends BaseJavaExampleActivity<ActivityBannerBinding> {

    @NonNull
    @Override
    protected ActivityBannerBinding inflate(@NonNull LayoutInflater inflater) {
        return ActivityBannerBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise SDK
        BidMachine.initialize(this, SOURCE_ID);

        // Enable logs
        BidMachine.setLoggingEnabled(true);

        // Set listener to perform Ads load
        binding.bLoadAd.setOnClickListener(v -> loadAd());

        // Set Banner Ads events listener
        binding.bannerView.setListener(new SimpleBannerListener() {
            @Override
            public void onAdLoaded(@NonNull BannerView ad) {
                setDebugState(Status.Loaded, "Banner Ads loaded");

                // Make BannerView visible
                ad.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdLoadFailed(@NonNull BannerView ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "Banner Ads load failed");
            }
        });
    }

    private void loadAd() {
        setDebugState(Status.Loading);

        // Create banner request
        BannerRequest request = new BannerRequest.Builder()
                .setSize(BannerSize.Size_320x50)
                .build();

        // Load Banner Ads
        binding.bannerView.load(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy Ads when you finish with it
        binding.bannerView.destroy();
    }

}