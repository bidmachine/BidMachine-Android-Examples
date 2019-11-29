package io.bidmachine.examples;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import io.bidmachine.BidMachine;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.nativead.NativeAd;
import io.bidmachine.nativead.NativeRequest;
import io.bidmachine.nativead.SimpleNativeListener;
import io.bidmachine.nativead.view.NativeAdContentLayout;
import io.bidmachine.utils.BMError;

public class NativeJavaActivity extends BaseJavaExampleActivity {

    private NativeAdContentLayout nativeAdContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialise SDK
        BidMachine.initialize(this, "5");

        //Enable logs
        BidMachine.setLoggingEnabled(true);

        //Set activity content view
        setContentView(R.layout.activity_native);

        //Set listener to perform Ads load
        findViewById(R.id.btnLoadAd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });

        //Find NativeAdContentLayout in hierarchy
        nativeAdContentLayout = findViewById(R.id.native_item);
    }

    private void loadAd() {
        setDebugState(Status.Loading);

        //Create native request
        NativeRequest request = new NativeRequest.Builder().build();

        //Load Native Ad
        NativeAd nativeAd = new NativeAd(this);
        nativeAd.setListener(new SimpleNativeListener() {
            @Override
            public void onAdLoaded(@NonNull NativeAd nativeAd) {
                setDebugState(Status.Loaded, "Native Ad is loaded");

                //Show native Ads
                nativeAdContentLayout.bind(nativeAd);
                nativeAdContentLayout.registerViewForInteraction(nativeAd);
                nativeAdContentLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdLoadFailed(@NonNull NativeAd nativeAd, @NonNull BMError bmError) {
                setDebugState(Status.LoadFail, "Native Ad is failed to load");
            }
        });
        nativeAd.load(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Destroy Ad when you finish with it
        nativeAdContentLayout.destroy();
    }

}
