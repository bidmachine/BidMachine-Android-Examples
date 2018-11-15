package io.bidmachine.examples;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import io.bidmachine.BidMachine;
import io.bidmachine.examples.base.BaseExampleActivity;
import io.bidmachine.rewardedvideo.RewardedVideoAd;
import io.bidmachine.rewardedvideo.RewardedVideoRequest;
import io.bidmachine.rewardedvideo.SimpleRewardedVideoListener;
import io.bidmachine.utils.BMError;

public class RewardedVideoActivity extends BaseExampleActivity {

    private RewardedVideoAd rewardedVideo;
    private RewardedVideoAd delayedShowRewardedVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded);

        //Set publisher id, which will be used for every request
        BidMachine.setPublisherId("1");

        //Initialise SDK
        BidMachine.initialize(this);

        findViewById(R.id.btnShowRewarded).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRewardedVideo();
            }
        });
        findViewById(R.id.btnLoadRewarded).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRewardedVideo();
            }
        });
        findViewById(R.id.btnShowLoadedRewarded).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadedRewardedVideo();
            }
        });
    }

    private void showRewardedVideo() {
        //Destroy previous loaded RewardedAd
        destroyRewardedVideo();

        //Create new RewardedVideoRequest
        final RewardedVideoRequest rewardedVideoRequest = new RewardedVideoRequest.Builder()
                .setAdSpaceId("29")
                .build();

        //Create new RewardedVideoAd
        rewardedVideo = new RewardedVideoAd(this);

        //Set RewardedVideoAd events listener
        rewardedVideo.setListener(new SimpleRewardedVideoListener() {
            @Override
            public void onAdLoaded(@NonNull RewardedVideoAd ad) {
                toast("Rewarded Video Ads Loaded");

                //Show RewardedVideoAd
                ad.show();
            }

            @Override
            public void onAdLoadFailed(@NonNull RewardedVideoAd ad, @NonNull BMError error) {
                toast("Rewarded Video Ads Load Failed");

                //Destroy loaded ad since it not required any more
                destroyRewardedVideo();
            }

            @Override
            public void onAdClosed(@NonNull RewardedVideoAd ad, boolean finished) {
                toast("Rewarded Video Ads Completed");

                //Destroy loaded ad since it not required any more
                destroyRewardedVideo();
            }

            @Override
            public void onAdRewarded(@NonNull RewardedVideoAd ad) {
                toast("Rewarded Video Ads Completed");
                //TODO: add note about reward handle
            }
        });

        //Load InterstitialAd
        rewardedVideo.load(rewardedVideoRequest);
    }

    private void loadRewardedVideo() {
        //Destroy previous loaded RewardedAd
        destroyRewardedVideo();

        //Create new RewardedVideoRequest
        final RewardedVideoRequest rewardedVideoRequest = new RewardedVideoRequest.Builder()
                .setAdSpaceId("29")
                .build();

        //Create new RewardedVideoAd
        rewardedVideo = new RewardedVideoAd(this);

        //Set RewardedVideoAd events listener
        rewardedVideo.setListener(new SimpleRewardedVideoListener() {
            @Override
            public void onAdLoaded(@NonNull RewardedVideoAd ad) {
                toast("Rewarded Video Ads Loaded");
            }

            @Override
            public void onAdLoadFailed(@NonNull RewardedVideoAd ad, @NonNull BMError error) {
                toast("Rewarded Video Ads Load Failed");

                //Destroy loaded ad since it not required any more
                destroyDelayedShowRewardedVideo();
            }

            @Override
            public void onAdClosed(@NonNull RewardedVideoAd ad, boolean finished) {
                toast("Rewarded Video Ads Completed");

                //Destroy loaded ad since it not required any more
                destroyDelayedShowRewardedVideo();
            }

            @Override
            public void onAdRewarded(@NonNull RewardedVideoAd ad) {
                toast("Rewarded Video Ads Completed");
                //TODO: add note about reward handle
            }
        });

        //Load RewardedVideo
        rewardedVideo.load(rewardedVideoRequest);
    }

    private void showLoadedRewardedVideo() {
        if (delayedShowRewardedVideo == null) {
            toast("Load RewardedVideo First");
        } else if (!delayedShowRewardedVideo.isLoaded()) {
            toast("RewardedVideo not Loaded yet");
        } else {
            delayedShowRewardedVideo.show();
        }
    }

    private void destroyRewardedVideo() {
        if (rewardedVideo != null) {
            rewardedVideo.destroy();
            rewardedVideo = null;
        }
    }

    private void destroyDelayedShowRewardedVideo() {
        if (delayedShowRewardedVideo != null) {
            delayedShowRewardedVideo.destroy();
            delayedShowRewardedVideo = null;
        }
    }

}
