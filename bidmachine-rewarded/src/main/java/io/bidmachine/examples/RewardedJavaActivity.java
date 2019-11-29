package io.bidmachine.examples;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import io.bidmachine.BidMachine;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.rewarded.RewardedAd;
import io.bidmachine.rewarded.RewardedRequest;
import io.bidmachine.rewarded.SimpleRewardedListener;
import io.bidmachine.utils.BMError;

public class RewardedJavaActivity extends BaseJavaExampleActivity {

    private RewardedAd rewardedAd;
    private RewardedAd delayedShowRewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialise SDK
        BidMachine.initialize(this, "5");

        //Enable logs
        BidMachine.setLoggingEnabled(true);

        //Set activity content view
        setContentView(R.layout.activity_rewarded);

        //Set listeners to perform Ads load
        findViewById(R.id.btnShowRewarded).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRewarded();
            }
        });
        findViewById(R.id.btnLoadRewarded).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRewarded();
            }
        });
        findViewById(R.id.btnShowLoadedRewarded).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadedRewarded();
            }
        });
    }

    private void showRewarded() {
        setDebugState(Status.Loading);

        //Destroy previous loaded RewardedAd
        destroyRewarded();

        //Create new RewardedRequest
        final RewardedRequest rewardedRequest = new RewardedRequest.Builder()
                .build();

        //Create new RewardedAd
        rewardedAd = new RewardedAd(this);

        //Set RewardedAd events listener
        rewardedAd.setListener(new SimpleRewardedListener() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                setDebugState(Status.Loaded, "Rewarded Ads Loaded");

                //Show RewardedAd
                ad.show();
            }

            @Override
            public void onAdLoadFailed(@NonNull RewardedAd ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "Rewarded Ads Load Failed");

                //Destroy loaded ad since it not required any more
                destroyRewarded();
            }

            @Override
            public void onAdClosed(@NonNull RewardedAd ad, boolean finished) {
                setDebugState(Status.Closed, "Rewarded Ads Closed");

                //Destroy loaded ad since it not required any more
                destroyRewarded();
            }

            @Override
            public void onAdRewarded(@NonNull RewardedAd ad) {
                setDebugState(Status.Rewarded, "Rewarded Ads Rewarded");

                //Here you can start you reward process
            }
        });

        //Load RewardedAd
        rewardedAd.load(rewardedRequest);
    }

    private void loadRewarded() {
        setDebugState(Status.Loading);

        //Destroy previous loaded RewardedAd
        destroyDelayedShowRewarded();

        //Create new RewardedRequest
        final RewardedRequest rewardedRequest = new RewardedRequest.Builder()
                .build();

        //Create new RewardedAd
        delayedShowRewardedAd = new RewardedAd(this);

        //Set RewardedAd events listener
        delayedShowRewardedAd.setListener(new SimpleRewardedListener() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                setDebugState(Status.Loaded, "Rewarded Ads Loaded");
            }

            @Override
            public void onAdLoadFailed(@NonNull RewardedAd ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "Rewarded Ads Load Failed");

                //Destroy loaded ad since it not required any more
                destroyDelayedShowRewarded();
            }

            @Override
            public void onAdClosed(@NonNull RewardedAd ad, boolean finished) {
                setDebugState(Status.Closed, "Rewarded Ads Closed");

                //Destroy loaded ad since it not required any more
                destroyDelayedShowRewarded();
            }

            @Override
            public void onAdRewarded(@NonNull RewardedAd ad) {
                setDebugState(Status.Rewarded, "Rewarded Ads Rewarded");

                //Here you can start you reward process
            }
        });

        //Load Rewarded
        delayedShowRewardedAd.load(rewardedRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Destroy Ads when you finish with it
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
