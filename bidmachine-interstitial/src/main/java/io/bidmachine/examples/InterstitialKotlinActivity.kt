package io.bidmachine.examples

import android.os.Bundle
import android.view.LayoutInflater
import io.bidmachine.Ad
import io.bidmachine.AdLoadCallback
import io.bidmachine.BMError
import io.bidmachine.BidMachine
import io.bidmachine.BidMachineInstance
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.base.Status
import io.bidmachine.examples.databinding.ActivityInterstitialBinding
import io.bidmachine.format.interstitialad.InterstitialAd
import io.bidmachine.format.interstitialad.InterstitialListener

class InterstitialKotlinActivity : BaseKotlinExampleActivity<ActivityInterstitialBinding>() {

    private var bidMachineInstance: BidMachineInstance? = null
    private var interstitialAd: InterstitialAd? = null

    override fun inflate(inflater: LayoutInflater) = ActivityInterstitialBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable logs
        BidMachine.setLoggingEnabled(true)

        // Initialize SDK
        bidMachineInstance = BidMachine.initialize(
            context = this,
            sourceId = SOURCE_ID
        )

        // Set listeners to perform Ads load and show
        binding.bLoadInterstitial.setOnClickListener {
            loadAd()
        }
        binding.bShowInterstitial.setOnClickListener {
            showAd()
        }
    }

    private fun loadAd() {
        setDebugState(Status.Loading)

        bidMachineInstance?.loadInterstitial(adLoadCallback = InterstitialAdLoadCallback())
            ?: toast("Initialize BidMachine first!")
    }

    private fun showAd() {
        val interstitialAd: InterstitialAd? = interstitialAd
        when {
            interstitialAd == null -> {
                toast("Load Interstitial First")
            }

            !(interstitialAd as Ad).canShow() -> {
                toast("Interstitial can't be shown")
            }

            else -> {
                interstitialAd.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Destroy Ads when you finish with it
        destroyInterstitialAd()
    }

    // canShow() and destroy() are called through the Ad interface,
    // since Kotlin can't resolve them directly on InterstitialAd in BidMachine 4.0.0
    private fun destroyInterstitialAd() {
        (interstitialAd as Ad?)?.destroy()
        interstitialAd = null
    }


    private inner class InterstitialAdLoadCallback : AdLoadCallback<InterstitialAd> {

        override fun onAdLoaded(ad: InterstitialAd) {
            setDebugState(
                status = Status.Loaded,
                message = "Interstitial Ads Loaded"
            )

            // Destroy previous loaded InterstitialAd
            destroyInterstitialAd()

            // Keep a reference to InterstitialAd: you need it to show the ad,
            // listen to its events and destroy it when you're done
            interstitialAd = ad
            ad.setListener(InterstitialAdListener())
        }

        override fun onAdLoadFailed(bmError: BMError) {
            setDebugState(
                status = Status.LoadFail,
                message = "Interstitial Ads Load Failed"
            )
        }

    }

    private inner class InterstitialAdListener : InterstitialListener {

        override fun onAdPresented(ad: InterstitialAd) {
            toast("Interstitial Ads Presented")
        }

        override fun onAdShowFailed(ad: InterstitialAd, bmError: BMError) {
            setDebugState(
                status = Status.ShowFail,
                message = "Interstitial Ads Show Failed"
            )

            // Destroy ad since it not required any more
            destroyInterstitialAd()
        }

        override fun onAdImpression(ad: InterstitialAd) {
            setDebugState(
                status = Status.Impression,
                message = "Interstitial Ads Impression"
            )
        }

        override fun onAdClicked(ad: InterstitialAd) {
            setDebugState(
                status = Status.Clicked,
                message = "Interstitial Ads Clicked"
            )
        }

        override fun onAdExpired(ad: InterstitialAd) {
            setDebugState(
                status = Status.Expired,
                message = "Interstitial Ads Expired"
            )

            // Destroy ad since it not required any more
            destroyInterstitialAd()
        }

        override fun onAdClosed(ad: InterstitialAd) {
            setDebugState(
                status = Status.Closed,
                message = "Interstitial Ads Closed"
            )

            // Destroy ad since it not required any more
            destroyInterstitialAd()
        }

    }

}