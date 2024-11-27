package io.bidmachine.examples

import android.os.Bundle
import android.view.LayoutInflater
import io.bidmachine.AdContentType
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.base.Status
import io.bidmachine.examples.databinding.ActivityInterstitialBinding
import io.bidmachine.interstitial.InterstitialAd
import io.bidmachine.interstitial.InterstitialRequest
import io.bidmachine.interstitial.SimpleInterstitialListener
import io.bidmachine.utils.BMError

class InterstitialKotlinActivity : BaseKotlinExampleActivity<ActivityInterstitialBinding>() {

    private var interstitialAd: InterstitialAd? = null
    private var videoAd: InterstitialAd? = null
    private var delayedShowInterstitialAd: InterstitialAd? = null

    override fun inflate(inflater: LayoutInflater) = ActivityInterstitialBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise SDK
        BidMachine.initialize(this, SOURCE_ID)

        // Enable logs
        BidMachine.setLoggingEnabled(true)

        // Set listeners to perform Ads load
        binding.bShowInterstitial.setOnClickListener {
            showInterstitial()
        }
        binding.bShowVideo.setOnClickListener {
            showVideo()
        }
        binding.bLoadInterstitial.setOnClickListener {
            loadInterstitial()
        }
        binding.bShowLoadedInterstitial.setOnClickListener {
            showLoadedInterstitial()
        }
    }

    private fun showInterstitial() {
        setDebugState(Status.Loading)

        // Destroy previous loaded InterstitialAd
        destroyInterstitialAd()

        // Create new InterstitialRequest
        val interstitialRequest = InterstitialRequest.Builder().build()

        // Create new InterstitialAd
        interstitialAd = InterstitialAd(this).apply {
            setListener(object : SimpleInterstitialListener() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    setDebugState(Status.Loaded, "Interstitial Ads Loaded")

                    // Show Interstitial Ad
                    ad.show()
                }

                override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Interstitial Ads Load Failed")

                    // Destroy loaded ad since it not required any more
                    destroyInterstitialAd()
                }

                override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Interstitial Ads Closed")

                    // Destroy loaded ad since it not required any more
                    destroyInterstitialAd()
                }
            })

            // Load InterstitialAd
            load(interstitialRequest)
        }
    }

    private fun showVideo() {
        setDebugState(Status.Loading)

        // Destroy previous loaded InterstitialAd for Video
        destroyVideoAd()

        // Create new InterstitialRequest for Video
        val interstitialRequest = InterstitialRequest.Builder()
            .setAdContentType(AdContentType.Video) // Set required Interstitial content type
            .build()

        // Create new InterstitialAd for Video
        videoAd = InterstitialAd(this).apply {
            setListener(object : SimpleInterstitialListener() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    setDebugState(Status.Loaded, "Interstitial Ads Loaded")

                    // Show Interstitial Ad for Video
                    ad.show()
                }

                override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Interstitial Ads Load Failed")

                    // Destroy loaded ad since it not required any more
                    destroyInterstitialAd()
                }

                override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Interstitial Ads Closed")

                    // Destroy loaded ad since it not required any more
                    destroyInterstitialAd()
                }
            })

            // Load InterstitialAd for Video
            load(interstitialRequest)
        }
    }

    private fun loadInterstitial() {
        setDebugState(Status.Loading)

        // Destroy previous loaded Interstitial Ads
        destroyDelayedShowInterstitial()

        // Create new InterstitialRequest
        val interstitialRequest = InterstitialRequest.Builder().build()

        // Create new InterstitialAd
        delayedShowInterstitialAd = InterstitialAd(this).apply {
            setListener(object : SimpleInterstitialListener() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    setDebugState(Status.Loaded, "Interstitial Ads Loaded")
                }

                override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Interstitial Ads Load Failed")

                    // Destroy loaded ad since it not required any more
                    destroyDelayedShowInterstitial()
                }

                override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Interstitial Ads Closed")

                    // Destroy loaded ad since it not required any more
                    destroyDelayedShowInterstitial()
                }
            })

            // Load InterstitialAd
            load(interstitialRequest)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Destroy Ads when you finish with it
        destroyInterstitialAd()
        destroyVideoAd()
        destroyDelayedShowInterstitial()
    }

    private fun destroyInterstitialAd() {
        interstitialAd?.destroy()
        interstitialAd = null
    }

    private fun destroyVideoAd() {
        videoAd?.destroy()
        videoAd = null
    }

    private fun destroyDelayedShowInterstitial() {
        delayedShowInterstitialAd?.destroy()
        delayedShowInterstitialAd = null
    }

    private fun showLoadedInterstitial() {
        when {
            delayedShowInterstitialAd == null -> {
                toast("Load Interstitial First")
            }

            delayedShowInterstitialAd?.isLoaded == false -> {
                toast("Interstitial not Loaded yet")
            }

            else -> {
                delayedShowInterstitialAd!!.show()
            }
        }
    }

}