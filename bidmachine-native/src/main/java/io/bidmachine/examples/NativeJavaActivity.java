package io.bidmachine.examples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.bidmachine.AdLoadCallback;
import io.bidmachine.BMError;
import io.bidmachine.BidMachine;
import io.bidmachine.BidMachineInstance;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.examples.base.Status;
import io.bidmachine.examples.databinding.ActivityNativeBinding;
import io.bidmachine.format.nativead.NativeAd;
import io.bidmachine.format.nativead.NativeListener;
import io.bidmachine.format.nativead.view.NativeAdContentLayout;

public class NativeJavaActivity extends BaseJavaExampleActivity<ActivityNativeBinding> {

    @Nullable
    private BidMachineInstance bidMachineInstance = null;
    @Nullable
    private NativeAd nativeAd = null;

    @NonNull
    @Override
    protected ActivityNativeBinding inflate(@NonNull LayoutInflater inflater) {
        return ActivityNativeBinding.inflate(inflater);
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
            bidMachineInstance.loadNative(new NativeAdLoadCallback());
        } else {
            toast("Initialize BidMachine first!");
        }
    }

    private void showAd(@NonNull NativeAd nativeAd) {
        // Destroy previous loaded NativeAd
        destroyNativeAd();

        // Keep a reference to NativeAd: you need it to show the ad,
        // listen to its events and destroy it when you're done
        this.nativeAd = nativeAd;
        nativeAd.setListener(new NativeAdListener());

        // Bind NativeAd to the layout
        NativeAdContentLayout nativeAdContentLayout = binding.nativeAd.nativeAdContentLayout;
        if (nativeAdContentLayout.bind(nativeAd)) {
            nativeAdContentLayout.setVisibility(View.VISIBLE);
        } else {
            toast("Native Ad can't be bound");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy Ad when you finish with it
        destroyNativeAd();

        binding.nativeAd.nativeAdContentLayout.destroy();
    }

    private void destroyNativeAd() {
        NativeAdContentLayout nativeAdContentLayout = binding.nativeAd.nativeAdContentLayout;
        nativeAdContentLayout.unbind();
        nativeAdContentLayout.setVisibility(View.GONE);

        NativeAd nativeAd = this.nativeAd;
        if (nativeAd != null) {
            nativeAd.destroy();
            this.nativeAd = null;
        }
    }


    private class NativeAdLoadCallback implements AdLoadCallback<NativeAd> {

        @Override
        public void onAdLoaded(@NonNull NativeAd ad) {
            setDebugState(Status.Loaded, "Native Ads loaded");

            showAd(ad);
        }

        @Override
        public void onAdLoadFailed(@NonNull BMError bmError) {
            setDebugState(Status.LoadFail, "Native Ads load failed");
        }

    }

    private class NativeAdListener implements NativeListener {

        @Override
        public void onAdClicked(@NonNull NativeAd ad) {
            setDebugState(Status.Clicked, "Native Ads clicked");
        }

        @Override
        public void onAdPresented(@NonNull NativeAd ad) {
            toast("Native Ads presented");
        }

        @Override
        public void onAdShowFailed(@NonNull NativeAd ad, @NonNull BMError bmError) {
            setDebugState(Status.ShowFail, "Native Ads show failed");

            // Destroy ad since it not required any more
            destroyNativeAd();
        }

        @Override
        public void onAdImpression(@NonNull NativeAd ad) {
            setDebugState(Status.Impression, "Native Ads impression");
        }

        @Override
        public void onAdExpired(@NonNull NativeAd ad) {
            setDebugState(Status.Expired, "Native Ads expired");

            // Destroy ad since it not required any more
            destroyNativeAd();
        }

    }

}