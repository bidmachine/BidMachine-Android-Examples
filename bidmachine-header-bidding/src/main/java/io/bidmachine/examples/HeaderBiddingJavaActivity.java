package io.bidmachine.examples;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.bidmachine.AdsFormat;
import io.bidmachine.BidMachine;
import io.bidmachine.ads.networks.adcolony.AdColonyConfig;
import io.bidmachine.ads.networks.facebook.FacebookConfig;
import io.bidmachine.ads.networks.mintegral.MintegralConfig;
import io.bidmachine.ads.networks.my_target.MyTargetConfig;
import io.bidmachine.ads.networks.tapjoy.TapjoyConfig;
import io.bidmachine.banner.BannerRequest;
import io.bidmachine.banner.BannerSize;
import io.bidmachine.banner.BannerView;
import io.bidmachine.banner.SimpleBannerListener;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.interstitial.InterstitialAd;
import io.bidmachine.interstitial.InterstitialRequest;
import io.bidmachine.interstitial.SimpleInterstitialListener;
import io.bidmachine.rewarded.RewardedAd;
import io.bidmachine.rewarded.RewardedRequest;
import io.bidmachine.rewarded.SimpleRewardedListener;
import io.bidmachine.utils.BMError;

public class HeaderBiddingJavaActivity extends BaseJavaExampleActivity {

    @Nullable
    private BannerView bannerView;
    @Nullable
    private InterstitialAd interstitialAd;
    @Nullable
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //3rd party networks should be configure before SDK initialization
        configureHeaderBiddingNetworks();

        //Initialise SDK
        BidMachine.initialize(this, "1");

        //Enable logs
        BidMachine.setLoggingEnabled(true);

        //Set activity content view
        setContentView(R.layout.activity_header_bidding);

        //Set listeners to perform Ads load
        findViewById(R.id.btnShowBanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBanner();
            }
        });
        findViewById(R.id.btnShowInterstitial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitial();
            }
        });
        findViewById(R.id.btnShowRewarded).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRewarded();
            }
        });
    }

    private void configureHeaderBiddingNetworks() {
        BidMachine.registerNetworks(
                //Configure AdColony network
                new AdColonyConfig("app185a7e71e1714831a49ec7")
                        .withMediationConfig(AdsFormat.InterstitialVideo, "vz06e8c32a037749699e7050")
                        .withMediationConfig(AdsFormat.RewardedVideo, "vz1fd5a8b2bf6841a0a4b826"),
                //Configure myTarget network
                new MyTargetConfig()
                        .withMediationConfig(AdsFormat.Banner, "437933")
                        .withMediationConfig(AdsFormat.Banner_320x50, "437933")
                        .withMediationConfig(AdsFormat.Banner_300x250, "64526")
                        .withMediationConfig(AdsFormat.Banner_728x90, "81620")
                        .withMediationConfig(AdsFormat.InterstitialStatic, "365991")
                        .withMediationConfig(AdsFormat.RewardedVideo, "482205"),
                //Configure Tapjoy network
                new TapjoyConfig("tmyN5ZcXTMyjeJNJmUD5ggECAbnEGtJREmLDd0fvqKBXcIr7e1dvboNKZI4y")
                        .withMediationConfig(AdsFormat.InterstitialVideo, "video_without_cap_pb")
                        .withMediationConfig(AdsFormat.RewardedVideo, "rewarded_video_without_cap_pb"),
                //Configure Facebook network
                new FacebookConfig("1525692904128549")
                        .withMediationConfig(AdsFormat.Banner, "1525692904128549_2386746951356469")
                        .withMediationConfig(AdsFormat.Banner_300x250, "1525692904128549_2386746951356469")
                        .withMediationConfig(AdsFormat.InterstitialStatic, "1525692904128549_2386743441356820")
                        .withMediationConfig(AdsFormat.RewardedVideo, "1525692904128549_2386753464689151"),
                //Configure Mintegral
                new MintegralConfig("92762", "936dcbdd57fe235fd7cf61c2e93da3c4")
                        .withMediationConfig(AdsFormat.InterstitialVideo, "21310")
                        .withMediationConfig(AdsFormat.RewardedVideo, "21310", "12817"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Destroy Ads when you finish with it
        if (bannerView != null) {
            bannerView.destroy();
        }
        destroyCurrentInterstitialAd();
        destroyCurrentRewardedAd();
    }

    private void showBanner() {
        if (bannerView == null) {
            bannerView = findViewById(R.id.bannerView);

            //Set Banner Ads events listener
            bannerView.setListener(new SimpleBannerListener() {
                @Override
                public void onAdLoaded(@NonNull BannerView ad) {
                    setDebugState(Status.Loaded, "Banner Ads loaded");

                    //Make BannerView visible
                    ad.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdLoadFailed(@NonNull BannerView ad, @NonNull BMError error) {
                    setDebugState(Status.LoadFail, "Banner Ads load failed");
                }
            });
        }
        if (bannerView == null) {
            setDebugState(Status.LoadFail, "BannerView not found");
            return;
        }
        setDebugState(Status.Loading);

        //Create banner request
        BannerRequest request = new BannerRequest.Builder()
                .setSize(BannerSize.Size_320x50)
                .build();

        //Load Banner Ads
        bannerView.load(request);
    }

    private void showInterstitial() {
        setDebugState(Status.Loading);

        //Destroy previous loaded Interstitial Ads
        destroyCurrentInterstitialAd();

        //Create new InterstitialRequest
        InterstitialRequest interstitialRequest = new InterstitialRequest.Builder()
                .build();

        //Create new InterstitialAd
        interstitialAd = new InterstitialAd(this)
                .setListener(new SimpleInterstitialListener() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        super.onAdLoaded(ad);
                        setDebugState(Status.Loaded, "Interstitial Ads Loaded");

                        //Show Interstitial Ad
                        ad.show();
                    }

                    @Override
                    public void onAdLoadFailed(@NonNull InterstitialAd ad, @NonNull BMError error) {
                        super.onAdLoadFailed(ad, error);
                        setDebugState(Status.LoadFail, "Interstitial Ads Load Failed");

                        //Destroy loaded ad since it not required any more
                        destroyCurrentInterstitialAd();
                    }

                    @Override
                    public void onAdClosed(@NonNull InterstitialAd ad, boolean finished) {
                        super.onAdClosed(ad, finished);
                        setDebugState(Status.Closed, "Interstitial Ads Closed");

                        //Destroy loaded ad since it not required any more
                        destroyCurrentInterstitialAd();
                    }
                });

        //Load InterstitialAd
        interstitialAd.load(interstitialRequest);
    }

    private void destroyCurrentInterstitialAd() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
            interstitialAd = null;
        }
    }

    private void showRewarded() {
        setDebugState(Status.Loading);

        //Destroy previous loaded RewardedAd
        destroyCurrentRewardedAd();

        //Create new RewardedRequest
        RewardedRequest rewardedRequest = new RewardedRequest.Builder()
                .build();

        //Create new RewardedAd
        rewardedAd = new RewardedAd(this)
                .setListener(new SimpleRewardedListener() {
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
                        destroyCurrentRewardedAd();
                    }

                    @Override
                    public void onAdClosed(@NonNull RewardedAd ad, boolean finished) {
                        setDebugState(Status.Closed, "Rewarded Ads Closed");

                        //Destroy loaded ad since it not required any more
                        destroyCurrentRewardedAd();
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

    private void destroyCurrentRewardedAd() {
        if (rewardedAd != null) {
            rewardedAd.destroy();
            rewardedAd = null;
        }
    }

}
