package io.bidmachine.examples

import android.os.Bundle
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.base.Utils.toast
import io.bidmachine.interstitial.InterstitialAd
import io.bidmachine.interstitial.InterstitialRequest
import io.bidmachine.interstitial.SimpleInterstitialListener
import io.bidmachine.utils.BMError
import kotlinx.android.synthetic.main.activity_interstitial.*

class InterstitialKotlinActivity : BaseKotlinExampleActivity() {

    private var interstitialAd: InterstitialAd? = null
    private var videoAd: InterstitialAd? = null

    private var delayedShowInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interstitial)

        //Initialise SDK
        BidMachine.initialize(this, "1")

        btnShowInterstitial.setOnClickListener { showInterstitial() }
        btnShowVideo.setOnClickListener { showVideo() }
        btnLoadInterstitial.setOnClickListener { loadInterstitial() }
        btnShowLoadedInterstitial.setOnClickListener { showLoadedInterstitial() }
    }

    private fun showInterstitial() {
        setDebugState(Status.Loading)

        //Destroy previous loaded InterstitialAd
        destroyInterstitialAd()

        //Create new InterstitialRequest
        val interstitialRequest = InterstitialRequest.Builder()
                .build()

        //Create new InterstitialAd
        interstitialAd = InterstitialAd(this).apply {

            //Set InterstitialAd events listener
            setListener(object : SimpleInterstitialListener() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    setDebugState(Status.Loaded, "Interstitial Ads Loaded")

                    //Show Interstitial Ad
                    ad.show()
                }

                override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Interstitial Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyInterstitialAd()
                }

                override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Interstitial Ads Closed")

                    //Destroy loaded ad since it not required any more
                    destroyInterstitialAd()
                }
            })

            //Load InterstitialAd
            load(interstitialRequest)
        }
    }

    /**
     * For Interstitial and Interstitial Video we use same class, for define display type you should
     * set it in ad space settings, see: TODO: add web link
     */

    private fun showVideo() {
        setDebugState(Status.Loading)
        //Destroy previous loaded InterstitialAd for Video
        destroyVideoAd()

        //Create new InterstitialRequest for Video
        val interstitialRequest = InterstitialRequest.Builder()
                .build()

        //Create new InterstitialAd for Video
        videoAd = InterstitialAd(this).apply {

            //Set InterstitialAd for Video events listener
            setListener(object : SimpleInterstitialListener() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    setDebugState(Status.Loaded, "Interstitial Ads Loaded")

                    //Show Interstitial Ad for Video
                    ad.show()
                }

                override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Interstitial Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyInterstitialAd()
                }

                override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Interstitial Ads Closed")

                    //Destroy loaded ad since it not required any more
                    destroyInterstitialAd()
                }
            })

            //Load InterstitialAd for Video
            load(interstitialRequest)
        }
    }

    private fun loadInterstitial() {
        setDebugState(Status.Loading)

        //Destroy previous loaded Interstitial Ads
        destroyDelayedShowInterstitial()

        //Create new InterstitialRequest
        val interstitialRequest = InterstitialRequest.Builder()
                .build()

        //Create new InterstitialAd
        delayedShowInterstitialAd = InterstitialAd(this).apply {

            //Set InterstitialAd events listener
            setListener(object : SimpleInterstitialListener() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    setDebugState(Status.Loaded, "Interstitial Ads Loaded")
                }

                override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Interstitial Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyDelayedShowInterstitial()
                }

                override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Interstitial Ads Closed")

                    //Destroy loaded ad since it not required any more
                    destroyDelayedShowInterstitial()
                }
            })

            //Load InterstitialAd
            load(interstitialRequest)
        }
    }

    private fun showLoadedInterstitial() {
        when {
            delayedShowInterstitialAd == null ->
                toast(this, "Load Interstitial First")
            delayedShowInterstitialAd?.isLoaded == false ->
                toast(this, "Interstitial not Loaded yet")
            else -> delayedShowInterstitialAd!!.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyInterstitialAd()
        destroyVideoAd()
        destroyDelayedShowInterstitial()
    }

    private fun destroyInterstitialAd() {
        interstitialAd?.let { ad ->
            ad.destroy()
            interstitialAd = null
        }
    }

    private fun destroyVideoAd() {
        videoAd?.let { ad ->
            ad.destroy()
            videoAd = null
        }
    }

    private fun destroyDelayedShowInterstitial() {
        delayedShowInterstitialAd?.let { ad ->
            ad.destroy()
            delayedShowInterstitialAd = null
        }
    }

}
