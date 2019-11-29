package io.bidmachine.examples

import android.os.Bundle
import android.view.View
import io.bidmachine.BidMachine
import io.bidmachine.banner.BannerRequest
import io.bidmachine.banner.BannerSize
import io.bidmachine.banner.BannerView
import io.bidmachine.banner.SimpleBannerListener
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.utils.BMError
import kotlinx.android.synthetic.main.activity_banner.*

class BannerKotlinActivity : BaseKotlinExampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialise SDK
        BidMachine.initialize(this, "5")

        //Enable logs
        BidMachine.setLoggingEnabled(true)

        //Set activity content view
        setContentView(R.layout.activity_banner)

        //Set listener to perform Ads load
        btnLoadAd.setOnClickListener {
            loadAd()
        }

        //Set Banner Ads events listener
        bannerView.setListener(object : SimpleBannerListener() {
            override fun onAdLoaded(ad: BannerView) {
                setDebugState(Status.Loaded, "Banner Ads Loaded")

                //Make BannerView visible
                ad.visibility = View.VISIBLE
            }

            override fun onAdLoadFailed(ad: BannerView, error: BMError) {
                setDebugState(Status.LoadFail, "Banner Ads Load Failed")
            }
        })
    }

    private fun loadAd() {
        setDebugState(Status.Loading)

        //Create Banner Ads request
        val bannerRequest = BannerRequest.Builder()
            .setSize(BannerSize.Size_320x50)
            .build()

        //Load Banner Ads
        bannerView.load(bannerRequest)
    }

    override fun onDestroy() {
        super.onDestroy()

        //Destroy Ads when you finish with it
        bannerView.destroy()
    }

}
