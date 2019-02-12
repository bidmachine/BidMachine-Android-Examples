package io.bidmachine.examples;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.bidmachine.BidMachine;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.nativead.NativeAd;
import io.bidmachine.nativead.NativeAdContentLayout;
import io.bidmachine.nativead.NativeRequest;
import io.bidmachine.nativead.SimpleNativeListener;
import io.bidmachine.utils.BMError;

public class NativeJavaActivity extends BaseJavaExampleActivity {

    @Nullable
    private NativeAd nativeAd;
    @Nullable
    private NativeAd delayedShowNativeAd;

    @NonNull
    private ViewGroup nativeParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialise SDK
        BidMachine.initialize(this, "1");

        //Enable logs
        BidMachine.setLoggingEnabled(true);

        //Set activity content view
        setContentView(R.layout.activity_native);

        //Find parent for display native ads
        nativeParent = findViewById(R.id.nativeParent);

        //Set listeners to perform Ads load
        findViewById(R.id.btnShowNative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNativeAd();
            }
        });
        findViewById(R.id.btnLoadNative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNativeAd();
            }
        });
        findViewById(R.id.btnShowLoadedNative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadedNativeAd();
            }
        });
    }

    private void showNativeAd() {
        setDebugState(Status.Loading);

        //Destroy previous loaded native ad, since it's not needed anymore
        destroyNativeAd();

        //Create new Native request
        final NativeRequest nativeRequest = new NativeRequest.Builder()
                .build();

        //Create new NativeAd object
        nativeAd = new NativeAd(this);
        //Set Native Ads listener
        nativeAd.setListener(new SimpleNativeListener() {
            @Override
            public void onAdLoaded(@NonNull NativeAd ad) {
                setDebugState(Status.Loaded, "Native Ads Loaded");

                //Show native ad
                inflateNativeAdWithBind(ad);
            }

            @Override
            public void onAdLoadFailed(@NonNull NativeAd ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "Native Ads Load Failed");

                //Destroy ad since it not needed anymore
                if (ad == nativeAd) {
                    destroyNativeAd();
                } else {
                    ad.destroy();
                }
            }
        });
        //Load native ad
        nativeAd.load(nativeRequest);
    }

    private void loadNativeAd() {
        setDebugState(Status.Loading);

        //Destroy previous loaded native ad, since it's not needed anymore
        destroyLoadedNativeAd();

        //Create new Native request
        final NativeRequest nativeRequest = new NativeRequest.Builder()
                .build();

        //Create new NativeAd object
        delayedShowNativeAd = new NativeAd(this);
        //Set Native Ads listener
        delayedShowNativeAd.setListener(new SimpleNativeListener() {
            @Override
            public void onAdLoaded(@NonNull NativeAd ad) {
                setDebugState(Status.Loaded, "Native Ads Loaded");
            }

            @Override
            public void onAdLoadFailed(@NonNull NativeAd ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "Native Ads Load Failed");

                //Destroy ad since it not needed anymore
                if (ad == delayedShowNativeAd) {
                    destroyLoadedNativeAd();
                } else {
                    ad.destroy();
                }
            }
        });
        //Load native ad
        delayedShowNativeAd.load(nativeRequest);
    }

    private void showLoadedNativeAd() {
        if (delayedShowNativeAd == null) {
            toast("Load Interstitial First");
        } else if (!delayedShowNativeAd.isLoaded()) {
            toast("Interstitial not Loaded yet");
        } else {
            inflateNativeAdWithBind(delayedShowNativeAd);
        }
    }

    private void inflateNativeAdWithBind(NativeAd ad) {
        if (ad == null) {
            return;
        }
        //Prepare layout for native ads
        final NativeAdContentLayout nativeAdContentLayout =
                (NativeAdContentLayout) getLayoutInflater().inflate(
                        R.layout.native_ad_layout, nativeParent, false);
        final TextView tvAgeRestrictions =
                nativeAdContentLayout.findViewById(R.id.txtAgeRestriction);
        if (ad.getAgeRestrictions() != null) {
            tvAgeRestrictions.setText(ad.getAgeRestrictions());
            tvAgeRestrictions.setVisibility(View.VISIBLE);
        } else {
            tvAgeRestrictions.setVisibility(View.GONE);
        }
        //Bind NativeAd to NativeAdContentLayout
        nativeAdContentLayout.bind(ad);
        //Register NativeAdContentLayout for interactions
        nativeAdContentLayout.registerViewForInteraction(ad);
        nativeAdContentLayout.setVisibility(View.VISIBLE);
        nativeParent.removeAllViews();
        nativeParent.addView(nativeAdContentLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Destroy Ads when you finish with it
        destroyNativeAd();
        destroyLoadedNativeAd();
    }

    private void destroyNativeAd() {
        //Destroy Ads when you finish with it
        if (nativeAd != null) {
            nativeAd.destroy();
            nativeAd = null;
        }
    }

    private void destroyLoadedNativeAd() {
        //Destroy Ads when you finish with it
        if (delayedShowNativeAd != null) {
            delayedShowNativeAd.destroy();
            delayedShowNativeAd = null;
        }
    }

}
