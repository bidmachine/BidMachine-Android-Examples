package io.bidmachine.examples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.bidmachine.AdsFormat;
import io.bidmachine.BidMachine;
import io.bidmachine.ads.networks.AmazonConfig;
import io.bidmachine.ads.networks.adcolony.AdColonyConfig;
import io.bidmachine.ads.networks.criteo.CriteoConfig;
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
import io.bidmachine.nativead.NativeAd;
import io.bidmachine.nativead.NativeRequest;
import io.bidmachine.nativead.SimpleNativeListener;
import io.bidmachine.nativead.view.NativeAdContentLayout;
import io.bidmachine.rewarded.RewardedAd;
import io.bidmachine.rewarded.RewardedRequest;
import io.bidmachine.rewarded.SimpleRewardedListener;
import io.bidmachine.utils.BMError;

public class HeaderBiddingJavaActivity extends BaseJavaExampleActivity {

    private FrameLayout adContainer;
    @Nullable
    private BannerView bannerView;
    @Nullable
    private InterstitialAd interstitialAd;
    @Nullable
    private RewardedAd rewardedAd;
    @Nullable
    private NativeAd nativeAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //3rd party networks should be configure before SDK initialization
        configureHeaderBiddingNetworks();

        //Initialise SDK
        BidMachine.initialize(this, "5");

        //Enable logs
        BidMachine.setLoggingEnabled(true);

        //Set activity content view
        setContentView(R.layout.activity_header_bidding);

        adContainer = findViewById(R.id.adContainer);

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
        findViewById(R.id.btnShowNative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNative();
            }
        });
    }

    private void configureHeaderBiddingNetworks() {
        BidMachine.registerNetworks(
                //Configure AdColony network
                new AdColonyConfig("app185a7e71e1714831a49ec7")
                        .withMediationConfig(AdsFormat.InterstitialVideo,
                                             "vz06e8c32a037749699e7050")
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
                        .withMediationConfig(AdsFormat.RewardedVideo,
                                             "rewarded_video_without_cap_pb"),
                //Configure Facebook network
                new FacebookConfig("1525692904128549")
                        .withMediationConfig(AdsFormat.Banner, "1525692904128549_2386746951356469")
                        .withMediationConfig(AdsFormat.Banner_300x250,
                                             "1525692904128549_2386746951356469")
                        .withMediationConfig(AdsFormat.InterstitialStatic,
                                             "1525692904128549_2386743441356820")
                        .withMediationConfig(AdsFormat.RewardedVideo,
                                             "1525692904128549_2386753464689151"),
                //Configure Mintegral network
                new MintegralConfig("117852", "936dcbdd57fe235fd7cf61c2e93da3c4")
                        .withMediationConfig(AdsFormat.InterstitialVideo, "140146")
                        .withMediationConfig(AdsFormat.RewardedVideo, "140144", "12817"),
                //Configure Amazon network
                new AmazonConfig("a9_onboarding_app_id")
                        .withMediationConfig(AdsFormat.Banner_320x50,
                                             "5ab6a4ae-4aa5-43f4-9da4-e30755f2b295")
                        .withMediationConfig(AdsFormat.Banner_300x250,
                                             "54fb2d08-c222-40b1-8bbe-4879322dc04b")
                        .withMediationConfig(AdsFormat.Banner_728x90,
                                             "bed17ec3-b185-453e-b2a8-4a3c6bb9234d")
                        .withMediationConfig(AdsFormat.InterstitialStatic,
                                             "4e918ac0-5c68-4fe1-8d26-4e76e8f74831")
                        .withMediationConfig(AdsFormat.InterstitialVideo,
                                             "4acc26e6-3ada-4ee8-bae0-753c1e0ad278"),
                //Configure Criteo
                new CriteoConfig("3703"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Destroy Ads when you finish with it
        destroyCurrentBannerView();
        destroyCurrentInterstitialAd();
        destroyCurrentRewardedAd();
        destroyCurrentNativeAd();
    }

    private void showAdView(View view) {
        adContainer.removeAllViews();
        adContainer.addView(view);
    }

    private void showBanner() {
        setDebugState(Status.Loading);

        //Destroy previous loaded Banner Ads
        destroyCurrentBannerView();

        //Create banner request
        BannerRequest request = new BannerRequest.Builder()
                .setSize(BannerSize.Size_320x50)
                .build();

        bannerView = new BannerView(this)
                .setListener(new SimpleBannerListener() {
                    @Override
                    public void onAdLoaded(@NonNull BannerView ad) {
                        setDebugState(Status.Loaded, "Banner Ads loaded");

                        //Show Banner Ad
                        showAdView(ad);
                    }

                    @Override
                    public void onAdLoadFailed(@NonNull BannerView ad, @NonNull BMError error) {
                        setDebugState(Status.LoadFail, "Banner Ads load failed");

                        //Destroy loaded ad since it not required any more
                        destroyCurrentBannerView();
                    }
                });

        //Load Banner Ads
        bannerView.load(request);
    }

    private void destroyCurrentBannerView() {
        if (bannerView != null) {
            bannerView.destroy();
            bannerView = null;
        }
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

    private void showNative() {
        setDebugState(Status.Loading);

        //Destroy previous loaded NativeAd
        destroyCurrentNativeAd();

        //Create new NativeRequest
        NativeRequest nativeRequest = new NativeRequest.Builder()
                .build();

        //Create new NativeAd
        nativeAd = new NativeAd(this)
                .setListener(new SimpleNativeListener() {
                    @Override
                    public void onAdLoaded(@NonNull NativeAd ad) {
                        setDebugState(Status.Loaded, "NativeAd Loaded");

                        //Show native Ads
                        View nativeView = createNativeAdView(ad);
                        showAdView(nativeView);
                    }

                    @Override
                    public void onAdLoadFailed(@NonNull NativeAd ad, @NonNull BMError error) {
                        setDebugState(Status.LoadFail, "NativeAd Load Failed");

                        //Destroy loaded ad since it not required any more
                        destroyCurrentNativeAd();
                    }
                });

        //Load NativeAd
        nativeAd.load(nativeRequest);
    }

    private View createNativeAdView(NativeAd nativeAd) {
        NativeAdContentLayout nativeView = (NativeAdContentLayout) LayoutInflater.from(this)
                .inflate(R.layout.native_ad, adContainer, false);
        nativeView.bind(nativeAd);
        nativeView.registerViewForInteraction(nativeAd);
        nativeView.setVisibility(View.VISIBLE);
        return nativeView;
    }

    private void destroyCurrentNativeAd() {
        if (nativeAd != null) {
            nativeAd.destroy();
            nativeAd = null;
        }
    }

}
