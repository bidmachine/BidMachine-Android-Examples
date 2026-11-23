package io.bidmachine.examples

import android.os.Bundle
import android.view.LayoutInflater
import io.bidmachine.AdLoadCallback
import io.bidmachine.BMError
import io.bidmachine.BidMachine
import io.bidmachine.BidMachineInstance
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.base.Status
import io.bidmachine.examples.base.Utils.addViewSafely
import io.bidmachine.examples.databinding.ActivityBannerBinding
import io.bidmachine.format.bannerad.BannerListener
import io.bidmachine.format.bannerad.BannerView
import io.bidmachine.model.BannerAdSize

class BannerKotlinActivity : BaseKotlinExampleActivity<ActivityBannerBinding>() {

    private var bidMachineInstance: BidMachineInstance? = null
    private var bannerView: BannerView? = null

    override fun inflate(inflater: LayoutInflater) = ActivityBannerBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable logs
        BidMachine.setLoggingEnabled(true)

        // Initialize SDK
        bidMachineInstance = BidMachine.initialize(
            context = this,
            sourceId = SOURCE_ID
        )

        // Set listener to perform Ads load
        binding.bLoadAd.setOnClickListener {
            loadAd()
        }
    }

    private fun loadAd() {
        setDebugState(Status.Loading)

        bidMachineInstance?.loadBanner(
            bannerAdSize = BannerAdSize.Banner,
            adLoadCallback = BannerAdLoadCallback()
        ) ?: toast("Initialize BidMachine first!")
    }

    private fun showAd(bannerView: BannerView) {
        // Destroy previous loaded BannerView
        destroyBanner()

        // Keep a reference to BannerView: you need it to show the ad,
        // listen to its events and destroy it when you're done
        this.bannerView = bannerView
        bannerView.setListener(BannerAdListener())
        addViewSafely(
            adContainer = binding.adContainer,
            adView = bannerView
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        // Destroy Ads when you finish with it
        destroyBanner()
    }

    private fun destroyBanner() {
        bannerView?.destroy()
        bannerView = null
    }


    private inner class BannerAdLoadCallback : AdLoadCallback<BannerView> {

        override fun onAdLoaded(ad: BannerView) {
            setDebugState(
                status = Status.Loaded,
                message = "Banner Ads loaded"
            )

            showAd(ad)
        }

        override fun onAdLoadFailed(bmError: BMError) {
            setDebugState(
                status = Status.LoadFail,
                message = "Banner Ads load failed"
            )
        }

    }

    private inner class BannerAdListener : BannerListener {

        override fun onAdClicked(ad: BannerView) {
            setDebugState(
                status = Status.Clicked,
                message = "Banner Ads clicked"
            )
        }

        override fun onAdPresented(ad: BannerView) {
            toast("Banner Ads presented")
        }

        override fun onAdShowFailed(ad: BannerView, bmError: BMError) {
            setDebugState(
                status = Status.ShowFail,
                message = "Banner Ads show failed"
            )

            // Destroy ad since it not required any more
            destroyBanner()
        }

        override fun onAdImpression(ad: BannerView) {
            setDebugState(
                status = Status.Impression,
                message = "Banner Ads impression"
            )
        }

        override fun onAdExpired(ad: BannerView) {
            setDebugState(
                status = Status.Expired,
                message = "Banner Ads expired"
            )

            // Destroy ad since it not required any more
            destroyBanner()
        }

    }

}