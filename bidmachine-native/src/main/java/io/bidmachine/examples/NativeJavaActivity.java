package io.bidmachine.examples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import io.bidmachine.AdPlacementConfig;
import io.bidmachine.BidMachine;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.examples.base.Status;
import io.bidmachine.examples.databinding.ActivityNativeBinding;
import io.bidmachine.MediaAssetType;
import io.bidmachine.nativead.NativeAd;
import io.bidmachine.nativead.NativeRequest;
import io.bidmachine.nativead.SimpleNativeListener;
import io.bidmachine.utils.BMError;

public class NativeJavaActivity extends BaseJavaExampleActivity<ActivityNativeBinding> {

    @Nullable
    private NativeAd nativeAd;

    @NonNull
    @Override
    protected ActivityNativeBinding inflate(@NonNull LayoutInflater inflater) {
        return ActivityNativeBinding.inflate(inflater);
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
    }

    private void loadAd() {
        setDebugState(Status.Loading);

        // Destroy previous loaded NativeAd
        destroyNativeAd();

        // Create placement configuration
        AdPlacementConfig config = AdPlacementConfig.nativeBuilder(MediaAssetType.DEFAULT).build();

        // Create native request
        NativeRequest request = new NativeRequest.Builder(config).build();

        // Create new NativeAd
        nativeAd = new NativeAd(this);

        // Set NativeAd events listener
        nativeAd.setListener(new SimpleNativeListener() {
            @Override
            public void onAdLoaded(@NonNull NativeAd nativeAd) {
                setDebugState(Status.Loaded, "Native Ad is loaded");

                // Show native Ads
                binding.nativeAd.nativeAdContentLayout.bind(nativeAd);
                binding.nativeAd.nativeAdContentLayout.registerViewForInteraction(nativeAd);
                binding.nativeAd.nativeAdContentLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdLoadFailed(@NonNull NativeAd nativeAd, @NonNull BMError bmError) {
                setDebugState(Status.LoadFail, "Native Ad is failed to load");
            }
        });

        // Load NativeAd
        nativeAd.load(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy Ad when you finish with it
        binding.nativeAd.nativeAdContentLayout.destroy();

        destroyNativeAd();
    }

    private void destroyNativeAd() {
        if (nativeAd != null) {
            nativeAd.destroy();
            nativeAd = null;
        }
    }

}