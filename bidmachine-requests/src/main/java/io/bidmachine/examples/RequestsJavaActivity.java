package io.bidmachine.examples;

import android.os.Bundle;
import android.view.View;
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
import io.bidmachine.utils.BMError;

public class RequestsJavaActivity extends BaseJavaExampleActivity {

    @Nullable
    private BannerView bannerView;
    @Nullable
    private BannerRequest bannerRequest;
    @Nullable
    private InterstitialAd interstitialAd;
    @Nullable
    private InterstitialRequest interstitialRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialise SDK
        BidMachine.initialize(this, "1");

        //Enable logs
        BidMachine.setLoggingEnabled(true);

        //Set activity content view
        setContentView(R.layout.activity_requests);

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

        //Find BannerView in hierarchy
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
                setDebugState(Status.LoadFail, "Banner Ads load failed: " + error.getMessage());
            }
        });
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
        } else if (bannerView != null) {
            //Perform BannerAd load
            bannerView.load(bannerRequest);
        }
    }

    private void requestInterstitial() {
        setDebugState(Status.Requesting);

        //Create new Interstitial Ads request
        interstitialRequest = new InterstitialRequest.Builder()
                //Set Banner Ads request listener
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
                                        "Interstitial Request Failed: " + bmError.getMessage());
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

        //Perform Banner Ads request
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

                    //Show interstitial Ads
                    ad.show();
                }

                @Override
                public void onAdLoadFailed(@NonNull InterstitialAd ad, @NonNull BMError error) {
                    setDebugState(Status.LoadFail, "Banner Ads load failed: " + error.getMessage());
                }

                @Override
                public void onAdClosed(@NonNull InterstitialAd ad, boolean finished) {
                    setDebugState(Status.Closed);

                    //Destroy current interstitial ad since we don't need it anymore
                    destroyCurrentInterstitialAd();
                }
            });

            //Perform InterstitialAd load
            interstitialAd.load(interstitialRequest);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Destroy Ads when you finish with it
        if (bannerView != null) {
            bannerView.destroy();
        }
        destroyCurrentInterstitialAd();
    }

    private void destroyCurrentInterstitialAd() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
            interstitialAd = null;
        }
    }
}
