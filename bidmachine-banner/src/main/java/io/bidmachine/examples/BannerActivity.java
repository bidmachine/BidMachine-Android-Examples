package io.bidmachine.examples;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import io.bidmachine.BidMachine;
import io.bidmachine.banner.BannerRequest;
import io.bidmachine.banner.BannerView;
import io.bidmachine.banner.SimpleBannerListener;
import io.bidmachine.examples.base.BaseExampleActivity;
import io.bidmachine.utils.BMError;

public class BannerActivity extends BaseExampleActivity {

    private BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        //Set publisher id, which will be used for every request
        BidMachine.setPublisherId("1");

        //Initialise SDK
        BidMachine.initialize(this);

        //Find BannerView in hierarchy
        bannerView = findViewById(R.id.bannerView);

        //Set Banner Ads events listener
        bannerView.setListener(new SimpleBannerListener() {
            @Override
            public void onAdLoaded(@NonNull BannerView ad) {
                toast("Banner Ads loaded");

                //make BannerView visible
                ad.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdLoadFailed(@NonNull BannerView ad, @NonNull BMError error) {
                toast("Banner Ads load failed");
            }
        });

        //Create banner request
        BannerRequest request = new BannerRequest.Builder()
                .setAdSpaceId("29")
                .build();

        //Load Banner Ads
        bannerView.load(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Destroy ads when you finish with it
        if (bannerView != null) {
            bannerView.destroy();
        }
    }

}
