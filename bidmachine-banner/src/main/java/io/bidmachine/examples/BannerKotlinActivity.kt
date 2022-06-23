package io.bidmachine.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import io.bidmachine.BidMachine
import io.bidmachine.banner.BannerRequest
import io.bidmachine.banner.BannerSize
import io.bidmachine.banner.BannerView
import io.bidmachine.banner.SimpleBannerListener
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.databinding.ActivityBannerBinding
import io.bidmachine.utils.BMError

class BannerKotlinActivity : BaseKotlinExampleActivity<ActivityBannerBinding>() {

    override fun inflate(inflater: LayoutInflater) = ActivityBannerBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise SDK
        BidMachine.initialize(this, "5")

        // Enable logs
        BidMachine.setLoggingEnabled(true)

        // Set listener to perform Ads load
        binding.bLoadAd.setOnClickListener {
            loadAd()
        }

        // Set Banner Ads events listener
        binding.bannerView.setListener(object : SimpleBannerListener() {
            override fun onAdLoaded(ad: BannerView) {
                setDebugState(Status.Loaded, "Banner Ads Loaded")

                // Make BannerView visible
                ad.visibility = View.VISIBLE
            }

            override fun onAdLoadFailed(ad: BannerView, error: BMError) {
                setDebugState(Status.LoadFail, "Banner Ads Load Failed")
            }
        })
    }

    private fun loadAd() {
        setDebugState(Status.Loading)

        // Create Banner Ads request
        val bannerRequest = BannerRequest.Builder()
                .setSize(BannerSize.Size_320x50)
                .build()

        // Load Banner Ads
        binding.bannerView.load(bannerRequest)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Destroy Ads when you finish with it
        binding.bannerView.destroy()
    }

}