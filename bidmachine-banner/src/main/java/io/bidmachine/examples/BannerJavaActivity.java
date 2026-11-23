package io.bidmachine.examples;

import static io.bidmachine.examples.base.Utils.addViewSafely;

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
import io.bidmachine.examples.databinding.ActivityBannerBinding;
import io.bidmachine.format.bannerad.BannerListener;
import io.bidmachine.format.bannerad.BannerView;
import io.bidmachine.model.BannerAdSize;

public class BannerJavaActivity extends BaseJavaExampleActivity<ActivityBannerBinding> {

    @Nullable
    private BidMachineInstance bidMachineInstance = null;
    @Nullable
    private BannerView bannerView = null;

    @NonNull
    @Override
    protected ActivityBannerBinding inflate(@NonNull LayoutInflater inflater) {
        return ActivityBannerBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable logs
        BidMachine.setLoggingEnabled(true);

        // Initialize SDK
        bidMachineInstance = BidMachine.initialize(this, SOURCE_ID);

        // Set listener to perform Ads load
        binding.bLoadAd.setOnClickListener(v -> loadAd());
    }

    private void loadAd() {
        setDebugState(Status.Loading);

        BidMachineInstance bidMachineInstance = this.bidMachineInstance;
        if (bidMachineInstance != null) {
            bidMachineInstance.loadBanner(BannerAdSize.Banner, new BannerAdLoadCallback());
        } else {
            toast("Initialize BidMachine first!");
        }
    }

    private void showAd(@NonNull BannerView bannerView) {
        // Destroy previous loaded BannerView
        destroyBanner();

        // Keep a reference to BannerView: you need it to show the ad,
        // listen to its events and destroy it when you're done
        this.bannerView = bannerView;
        bannerView.setListener(new BannerAdListener());
        addViewSafely(binding.adContainer, bannerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy Ads when you finish with it
        destroyBanner();
    }

    private void destroyBanner() {
        BannerView bannerView = this.bannerView;
        if (bannerView != null) {
            bannerView.destroy();
            this.bannerView = null;
        }
    }


    private class BannerAdLoadCallback implements AdLoadCallback<BannerView> {

        @Override
        public void onAdLoaded(@NonNull BannerView ad) {
            setDebugState(Status.Loaded, "Banner Ads loaded");

            showAd(ad);
        }

        @Override
        public void onAdLoadFailed(@NonNull BMError bmError) {
            setDebugState(Status.LoadFail, "Banner Ads load failed");
        }

    }

    private class BannerAdListener implements BannerListener {

        @Override
        public void onAdClicked(@NonNull BannerView ad) {
            setDebugState(Status.Clicked, "Banner Ads clicked");
        }

        @Override
        public void onAdPresented(@NonNull BannerView ad) {
            toast("Banner Ads presented");
        }

        @Override
        public void onAdShowFailed(@NonNull BannerView ad, @NonNull BMError bmError) {
            setDebugState(Status.ShowFail, "Banner Ads show failed");

            // Destroy ad since it not required any more
            destroyBanner();
        }

        @Override
        public void onAdImpression(@NonNull BannerView ad) {
            setDebugState(Status.Impression, "Banner Ads impression");
        }

        @Override
        public void onAdExpired(@NonNull BannerView ad) {
            setDebugState(Status.Expired, "Banner Ads expired");

            // Destroy ad since it not required any more
            destroyBanner();
        }

    }

}