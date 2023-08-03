package io.bidmachine.examples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import io.bidmachine.BidMachine;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.examples.databinding.ActivityRichMediaBinding;
import io.bidmachine.richmedia.RichMediaRequest;
import io.bidmachine.richmedia.RichMediaView;
import io.bidmachine.richmedia.SimpleRichMediaListener;
import io.bidmachine.utils.BMError;

public class RichMediaJavaActivity extends BaseJavaExampleActivity<ActivityRichMediaBinding> {

    @NonNull
    @Override
    protected ActivityRichMediaBinding inflate(@NonNull LayoutInflater inflater) {
        return ActivityRichMediaBinding.inflate(inflater);
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
        binding.bResumeRichMedia.setOnClickListener(v -> resumeRichMedia());
        binding.bPauseRichMedia.setOnClickListener(v -> pauseRichMedia());
        binding.bMuteRichMedia.setOnClickListener(v -> muteRichMedia());
        binding.bUnmuteRichMedia.setOnClickListener(v -> unmuteRichMedia());

        // Set RichMedia Ads events listener
        binding.richMediaView.setListener(new SimpleRichMediaListener() {
            @Override
            public void onAdLoaded(@NonNull RichMediaView ad) {
                setDebugState(Status.Loaded, "RichMedia Ads loaded");

                // Make RichMediaView visible
                ad.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdLoadFailed(@NonNull RichMediaView ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "RichMedia Ads load failed");
            }
        });
    }

    private void loadAd() {
        setDebugState(Status.Loading);

        // Create RichMediaRequest
        RichMediaRequest request = new RichMediaRequest.Builder().build();

        // Load RichMedia Ads
        binding.richMediaView.load(request);
    }

    private void resumeRichMedia() {
        binding.richMediaView.resume();
    }

    private void pauseRichMedia() {
        binding.richMediaView.pause();
    }

    private void muteRichMedia() {
        binding.richMediaView.mute();
    }

    private void unmuteRichMedia() {
        binding.richMediaView.unmute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy Ads when you finish with it
        binding.richMediaView.destroy();
    }

}