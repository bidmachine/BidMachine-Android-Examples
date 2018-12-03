package io.bidmachine.examples;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import io.bidmachine.BidMachine;
import io.bidmachine.examples.base.BaseExampleActivity;
import io.bidmachine.interstitial.InterstitialAd;
import io.bidmachine.interstitial.InterstitialRequest;
import io.bidmachine.interstitial.SimpleInterstitialListener;
import io.bidmachine.utils.BMError;

public class InterstitialActivity extends BaseExampleActivity {

    private InterstitialAd interstitialAd;
    private InterstitialAd videoAd;

    private InterstitialAd delayedShowInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        //Initialise SDK
        BidMachine.initialize(this, "1");

        findViewById(R.id.btnShowInterstitial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial();
            }
        });

        findViewById(R.id.btnShowVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideo();
            }
        });
        findViewById(R.id.btnLoadInterstitial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInterstitial();
            }
        });
        findViewById(R.id.btnShowLoadedInterstitial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadedInterstitial();
            }
        });

    }

    private void showInterstitial() {
        //Destroy previous loaded InterstitialAd
        destroyInterstitialAd();

        //Create new InterstitialRequest
        final InterstitialRequest interstitialRequest = new InterstitialRequest.Builder()
                .build();

        //Create new InterstitialAd
        interstitialAd = new InterstitialAd(this);

        //Set InterstitialAd events listener
        interstitialAd.setListener(new SimpleInterstitialListener() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd ad) {
                toast("Interstitial Ads Loaded");

                //Show Interstitial Ad
                ad.show();
            }

            @Override
            public void onAdLoadFailed(@NonNull InterstitialAd ad, @NonNull BMError error) {
                toast("Interstitial Ads Load Failed");

                //Destroy loaded ad since it not required any more
                destroyInterstitialAd();
            }

            @Override
            public void onAdClosed(@NonNull InterstitialAd ad, boolean finished) {
                toast("Interstitial Ads Completed");

                //Destroy loaded ad since it not required any more
                destroyInterstitialAd();
            }
        });

        //Load InterstitialAd
        interstitialAd.load(interstitialRequest);
    }

    /**
     * For Interstitial and Interstitial Video we use same class, for define display type you should
     * set it in ad space settings, see: TODO: add web link
     */

    private void showVideo() {
        //Destroy previous loaded InterstitialAd for Video
        destroyVideoAd();

        //Create new InterstitialRequest for Video
        final InterstitialRequest interstitialRequest = new InterstitialRequest.Builder()
                .build();

        //Create new InterstitialAd for Video
        videoAd = new InterstitialAd(this);

        //Set InterstitialAd for Video events listener
        videoAd.setListener(new SimpleInterstitialListener() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd ad) {
                toast("Interstitial Ads Loaded");

                //Show Interstitial Ad for Video
                ad.show();
            }

            @Override
            public void onAdLoadFailed(@NonNull InterstitialAd ad, @NonNull BMError error) {
                toast("Interstitial Ads Load Failed");

                //Destroy loaded ad since it not required any more
                destroyVideoAd();
            }

            @Override
            public void onAdClosed(@NonNull InterstitialAd ad, boolean finished) {
                toast("Interstitial Ads Completed");

                //Destroy loaded ad since it not required any more
                destroyVideoAd();
            }
        });
        //Load InterstitialAd for Video
        videoAd.load(interstitialRequest);
    }

    private void loadInterstitial() {
        //Destroy previous loaded Interstitial Ads
        destroyDelayedShowInterstitial();

        //Create new InterstitialRequest
        final InterstitialRequest interstitialRequest = new InterstitialRequest.Builder()
                .build();

        //Create new InterstitialAd
        delayedShowInterstitialAd = new InterstitialAd(this);

        //Set InterstitialAd events listener
        delayedShowInterstitialAd.setListener(new SimpleInterstitialListener() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd ad) {
                toast("Interstitial Ads Loaded");
            }

            @Override
            public void onAdLoadFailed(@NonNull InterstitialAd ad, @NonNull BMError error) {
                toast("Interstitial Ads Load Failed");

                //Destroy loaded ad since it not required any more
                destroyDelayedShowInterstitial();
            }

            @Override
            public void onAdClosed(@NonNull InterstitialAd ad, boolean finished) {
                toast("Interstitial Ads Completed");

                //Destroy loaded ad since it not required any more
                destroyDelayedShowInterstitial();
            }
        });

        //Load InterstitialAd
        delayedShowInterstitialAd.load(interstitialRequest);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

}
