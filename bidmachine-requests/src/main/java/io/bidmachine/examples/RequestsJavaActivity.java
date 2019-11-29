package io.bidmachine.examples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.bidmachine.BidMachine;
import io.bidmachine.banner.BannerRequest;
import io.bidmachine.banner.BannerSize;
import io.bidmachine.banner.BannerView;
import io.bidmachine.banner.SimpleBannerListener;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.interstitial.InterstitialAd;
import io.bidmachine.interstitial.InterstitialRequest;
import io.bidmachine.interstitial.SimpleInterstitialListener;
import io.bidmachine.models.AuctionResult;
import io.bidmachine.nativead.NativeAd;
import io.bidmachine.nativead.NativeRequest;
import io.bidmachine.nativead.SimpleNativeListener;
import io.bidmachine.nativead.view.NativeAdContentLayout;
import io.bidmachine.rewarded.RewardedAd;
import io.bidmachine.rewarded.RewardedRequest;
import io.bidmachine.rewarded.SimpleRewardedListener;
import io.bidmachine.utils.BMError;

public class RequestsJavaActivity extends BaseJavaExampleActivity {

    private FrameLayout adContainer;
    @Nullable
    private BannerView bannerView;
    @Nullable
    private BannerRequest bannerRequest;
    @Nullable
    private InterstitialAd interstitialAd;
    @Nullable
    private InterstitialRequest interstitialRequest;
    @Nullable
    private RewardedAd rewardedAd;
    @Nullable
    private RewardedRequest rewardedRequest;
    @Nullable
    private NativeAd nativeAd;
    @Nullable
    private NativeRequest nativeRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialise SDK
        BidMachine.initialize(this, "5");

        //Enable logs
        BidMachine.setLoggingEnabled(true);

        //Set activity content view
        setContentView(R.layout.activity_requests);

        adContainer = findViewById(R.id.adContainer);

        //Set listener to perform Banner Ads request
        findViewById(R.id.btnRequestBanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBanner();
            }
        });

        //Set listener to perform Banner Ads show
        findViewById(R.id.btnShowRequestedBanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRequestedBanner();
            }
        });

        //Set listener to perform Interstitial Ads request
        findViewById(R.id.btnRequestInterstitial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestInterstitial();
            }
        });

        //Set listener to perform Interstitial Ads show
        findViewById(R.id.btnShowRequestedInterstitial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRequestedInterstitial();
            }
        });

        //Set listener to perform Rewarded Ads request
        findViewById(R.id.btnRequestRewarded).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRewarded();
            }
        });

        //Set listener to perform Rewarded Ads show
        findViewById(R.id.btnShowRequestedRewarded).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRequestedRewarded();
            }
        });

        //Set listener to perform Native Ads request
        findViewById(R.id.btnRequestNative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestNative();
            }
        });

        //Set listener to perform Native Ads show
        findViewById(R.id.btnShowRequestedNative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRequestedNative();
            }
        });
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

    /**
     * Make note, that AdRequest listeners will be notified in background thread
     */
    private void requestBanner() {
        setDebugState(Status.Requesting);

        //Create new Banner Ads request
        bannerRequest = new BannerRequest.Builder()
                .setSize(BannerSize.Size_320x50)
                //Set Banner Ads request listener
                .setListener(new BannerRequest.AdRequestListener() {
                    @Override
                    public void onRequestSuccess(@NonNull BannerRequest bannerRequest,
                                                 @NonNull AuctionResult auctionResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.Requested, "Banner Ad Requested");
                            }
                        });
                    }

                    @Override
                    public void onRequestFailed(@NonNull BannerRequest bannerRequest,
                                                @NonNull final BMError bmError) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.RequestFail,
                                              "Banner Request Failed: " + bmError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onRequestExpired(@NonNull BannerRequest bannerRequest) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.Expired, "Banner Request Expired");
                            }
                        });
                    }
                }).build();

        //Perform Banner Ads request
        bannerRequest.request(this);
    }

    private void showRequestedBanner() {
        if (bannerRequest == null) {
            toast("Please request banner first");
        } else if (bannerRequest.isExpired()) {
            toast("BannerRequest expired, request new one please");
        } else if (bannerRequest.getAuctionResult() == null) {
            toast("BannerRequest not requested or requested unsuccessfully");
        } else {
            bannerView = new BannerView(this);
            bannerView.setListener(new SimpleBannerListener() {
                @Override
                public void onAdLoaded(@NonNull BannerView ad) {
                    setDebugState(Status.Loaded, "Banner Ads loaded");

                    //Show Banner Ad
                    showAdView(ad);
                }

                @Override
                public void onAdLoadFailed(@NonNull BannerView ad, @NonNull BMError error) {
                    setDebugState(Status.LoadFail, "Banner Ads load failed: " + error.getMessage());

                    //Destroy loaded ad since it not required any more
                    destroyCurrentBannerView();
                }
            });

            //Perform BannerAd load
            bannerView.load(bannerRequest);
        }
    }

    private void destroyCurrentBannerView() {
        if (bannerView != null) {
            bannerView.destroy();
            bannerView = null;
        }
    }

    private void requestInterstitial() {
        setDebugState(Status.Requesting);

        //Create new Interstitial Ads request
        interstitialRequest = new InterstitialRequest.Builder()
                //Set Interstitial Ads request listener
                .setListener(new InterstitialRequest.AdRequestListener() {
                    @Override
                    public void onRequestSuccess(@NonNull InterstitialRequest interstitialRequest,
                                                 @NonNull AuctionResult auctionResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.Requested, "Interstitial Ad Requested");
                            }
                        });
                    }

                    @Override
                    public void onRequestFailed(@NonNull InterstitialRequest interstitialRequest,
                                                @NonNull final BMError bmError) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.RequestFail,
                                              "Interstitial Request Failed: "
                                                      + bmError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onRequestExpired(@NonNull InterstitialRequest interstitialRequest) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.Expired, "Interstitial Request Expired");
                            }
                        });
                    }
                }).build();

        //Perform Interstitial Ads request
        interstitialRequest.request(this);
    }

    private void showRequestedInterstitial() {
        if (interstitialRequest == null) {
            toast("Please request Interstitial first");
        } else if (interstitialRequest.isExpired()) {
            toast("InterstitialRequest expired, request new one please");
        } else if (interstitialRequest.getAuctionResult() == null) {
            toast("InterstitialRequest not requested or requested unsuccessfully");
        } else {
            //Destroy previous InterstitialAd object
            destroyCurrentInterstitialAd();

            //Create new InterstitialAd
            interstitialAd = new InterstitialAd(this);
            interstitialAd.setListener(new SimpleInterstitialListener() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd ad) {
                    setDebugState(Status.Loaded, "Interstitial Ads Loaded");

                    //Show Interstitial Ads
                    ad.show();
                }

                @Override
                public void onAdLoadFailed(@NonNull InterstitialAd ad, @NonNull BMError error) {
                    setDebugState(Status.LoadFail,
                                  "Interstitial Ads load failed: " + error.getMessage());

                    //Destroy current Interstitial ad since we don't need it anymore
                    destroyCurrentInterstitialAd();
                }

                @Override
                public void onAdClosed(@NonNull InterstitialAd ad, boolean finished) {
                    setDebugState(Status.Closed);

                    //Destroy current Interstitial ad since we don't need it anymore
                    destroyCurrentInterstitialAd();
                }
            });

            //Perform InterstitialAd load
            interstitialAd.load(interstitialRequest);
        }
    }

    private void destroyCurrentInterstitialAd() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
            interstitialAd = null;
        }
    }

    private void requestRewarded() {
        setDebugState(Status.Requesting);

        //Create new Rewarded Ads request
        rewardedRequest = new RewardedRequest.Builder()
                //Set Rewarded Ads request listener
                .setListener(new RewardedRequest.AdRequestListener() {
                    @Override
                    public void onRequestSuccess(@NonNull RewardedRequest rewardedRequest,
                                                 @NonNull AuctionResult auctionResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.Requested, "Rewarded Ad Requested");
                            }
                        });
                    }

                    @Override
                    public void onRequestFailed(@NonNull RewardedRequest rewardedRequest,
                                                @NonNull final BMError bmError) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.RequestFail,
                                              "Rewarded Request Failed: "
                                                      + bmError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onRequestExpired(@NonNull RewardedRequest rewardedRequest) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.Expired, "Rewarded Request Expired");
                            }
                        });
                    }
                }).build();

        //Perform Rewarded Ads request
        rewardedRequest.request(this);
    }

    private void showRequestedRewarded() {
        if (rewardedRequest == null) {
            toast("Please request Rewarded first");
        } else if (rewardedRequest.isExpired()) {
            toast("RewardedRequest expired, request new one please");
        } else if (rewardedRequest.getAuctionResult() == null) {
            toast("RewardedRequest not requested or requested unsuccessfully");
        } else {
            //Destroy previous RewardedAd object
            destroyCurrentRewardedAd();

            //Create new RewardedAd
            rewardedAd = new RewardedAd(this);
            rewardedAd.setListener(new SimpleRewardedListener() {
                @Override
                public void onAdLoaded(@NonNull RewardedAd ad) {
                    setDebugState(Status.Loaded, "Rewarded Ads Loaded");

                    //Show Rewarded Ads
                    ad.show();
                }

                @Override
                public void onAdLoadFailed(@NonNull RewardedAd ad, @NonNull BMError error) {
                    setDebugState(Status.LoadFail,
                                  "Rewarded Ads load failed: " + error.getMessage());

                    //Destroy current Rewarded ad since we don't need it anymore
                    destroyCurrentRewardedAd();
                }

                @Override
                public void onAdClosed(@NonNull RewardedAd ad, boolean finished) {
                    setDebugState(Status.Closed, "Rewarded Ads Closed");

                    //Destroy current Rewarded ad since we don't need it anymore
                    destroyCurrentRewardedAd();
                }

                @Override
                public void onAdRewarded(@NonNull RewardedAd ad) {
                    setDebugState(Status.Rewarded, "Rewarded Ads Rewarded");

                    //Here you can start you reward process
                }
            });

            //Perform RewardedAd load
            rewardedAd.load(rewardedRequest);
        }
    }

    private void destroyCurrentRewardedAd() {
        if (rewardedAd != null) {
            rewardedAd.destroy();
            rewardedAd = null;
        }
    }

    private void requestNative() {
        setDebugState(Status.Requesting);

        //Create new Native Ads request
        nativeRequest = new NativeRequest.Builder()
                //Set Native Ads request listener
                .setListener(new NativeRequest.AdRequestListener() {
                    @Override
                    public void onRequestSuccess(@NonNull NativeRequest nativeRequest,
                                                 @NonNull AuctionResult auctionResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.Requested, "Native Ad Requested");
                            }
                        });
                    }

                    @Override
                    public void onRequestFailed(@NonNull NativeRequest nativeRequest,
                                                @NonNull final BMError bmError) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.RequestFail,
                                              "Native Request Failed: "
                                                      + bmError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onRequestExpired(@NonNull NativeRequest nativeRequest) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDebugState(Status.Expired, "Native Request Expired");
                            }
                        });
                    }
                }).build();

        //Perform Native Ads request
        nativeRequest.request(this);
    }

    private void showRequestedNative() {
        if (nativeRequest == null) {
            toast("Please request Native first");
        } else if (nativeRequest.isExpired()) {
            toast("NativeRequest expired, request new one please");
        } else if (nativeRequest.getAuctionResult() == null) {
            toast("NativeRequest not requested or requested unsuccessfully");
        } else {
            //Destroy previous NativeAd object
            destroyCurrentNativeAd();

            //Create new NativeAd
            nativeAd = new NativeAd(this);
            nativeAd.setListener(new SimpleNativeListener() {
                @Override
                public void onAdLoaded(@NonNull NativeAd ad) {
                    setDebugState(Status.Loaded, "Native Ads Loaded");

                    //Show native Ads
                    View nativeView = createNativeAdView(ad);
                    showAdView(nativeView);
                }

                @Override
                public void onAdLoadFailed(@NonNull NativeAd ad, @NonNull BMError error) {
                    setDebugState(Status.LoadFail, "Native Ads load failed: " + error.getMessage());
                }
            });

            //Perform NativeAd load
            nativeAd.load(nativeRequest);
        }
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
