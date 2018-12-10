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

    private lateinit var bannerView: BannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

        //Helper for load new ad instance
        btnLoadAd.setOnClickListener {
            loadAd()
        }

        //Initialise SDK
        BidMachine.initialize(this, "1")

        //Find BannerView in hierarchy
        bannerView = findViewById(R.id.bannerView)

        //Set Banner Ads events listener
        bannerView.setListener(object : SimpleBannerListener() {
            override fun onAdLoaded(ad: BannerView) {
                ad.visibility = View.VISIBLE
                setDebugState(Status.Loaded, "Banner Ads Loaded")
            }

            override fun onAdLoadFailed(ad: BannerView, error: BMError) {
                setDebugState(Status.LoadFail, "Banner Ads Load Failed")
            }
        })

        loadAd()
    }

    private fun loadAd() {
        setDebugState(Status.Loading)

        //Create Banner request
        val bannerRequest = BannerRequest.Builder()
                .setSize(BannerSize.Size_320_50)
                .build()

        //Load Banner Ads
        bannerView.load(bannerRequest)
    }

    override fun onDestroy() {
        super.onDestroy()

        //Destroy ads when you finish with it
        if (this::bannerView.isInitialized) {
            bannerView.destroy()
        }
    }

}
