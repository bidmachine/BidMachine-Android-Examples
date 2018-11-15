package io.bidmachine.examples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.Utils.toast
import io.bidmachine.interstitial.InterstitialAd
import io.bidmachine.interstitial.InterstitialRequest
import io.bidmachine.interstitial.SimpleInterstitialListener
import io.bidmachine.utils.BMError
import kotlinx.android.synthetic.main.activity_interstitial.*

class InterstitialKotlinActivity : AppCompatActivity() {

    private var interstitialAd: InterstitialAd? = null
    private var videoAd: InterstitialAd? = null

    private var delayedShowInterstitial: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interstitial)

        //Set publisher id, which will be used for every request
        BidMachine.setPublisherId("1")

        //Initialise SDK
        BidMachine.initialize(this)

        btnShowInterstitial.setOnClickListener { showInterstitial() }
        btnShowVideo.setOnClickListener { showVideo() }
        btnLoadInterstitial.setOnClickListener { loadInterstitial() }
        btnShowLoadedInterstitial.setOnClickListener { showLoadedInterstitial() }

    }

    private fun showInterstitial() {
        //Destroy previous loaded InterstitialAd
        destroyInterstitialAd()

        //Create new InterstitialRequest
        val interstitialRequest = InterstitialRequest.Builder()
                .setAdSpaceId("29")
                .build()

        //Create new InterstitialAd
        interstitialAd = InterstitialAd(this).apply {

            //Set InterstitialAd events listener
            setListener(object : SimpleInterstitialListener() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    toast(this@InterstitialKotlinActivity, "Interstitial Ads Loaded")

                    //Show Interstitial Ad
                    ad.show()
                }

                override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                    toast(this@InterstitialKotlinActivity, "Interstitial Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyInterstitialAd()
                }

                override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                    toast(this@InterstitialKotlinActivity, "Interstitial Ads Completed")

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
        //Destroy previous loaded InterstitialAd for Video
        destroyVideoAd()

        //Create new InterstitialRequest for Video
        val interstitialRequest = InterstitialRequest.Builder()
                .setAdSpaceId("29")
                .build()

        //Create new InterstitialAd for Video
        videoAd = InterstitialAd(this).apply {

            //Set InterstitialAd for Video events listener
            setListener(object : SimpleInterstitialListener() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    toast(this@InterstitialKotlinActivity, "Interstitial Ads Loaded")

                    //Show Interstitial Ad for Video
                    ad.show()
                }

                override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                    toast(this@InterstitialKotlinActivity, "Interstitial Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyInterstitialAd()
                }

                override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                    toast(this@InterstitialKotlinActivity, "Interstitial Ads Completed")

                    //Destroy loaded ad since it not required any more
                    destroyInterstitialAd()
                }
            })

            //Load InterstitialAd for Video
            load(interstitialRequest)
        }
    }

    private fun loadInterstitial() {
        //Destroy previous loaded Interstitial Ads
        destroyDelayedShowInterstitial()

        //Create new InterstitialRequest
        val interstitialRequest = InterstitialRequest.Builder()
                .setAdSpaceId("29")
                .build()

        //Create new InterstitialAd
        delayedShowInterstitial = InterstitialAd(this).apply {

            //Set InterstitialAd events listener
            setListener(object : SimpleInterstitialListener() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    toast(this@InterstitialKotlinActivity, "Interstitial Ads Loaded")
                }

                override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                    toast(this@InterstitialKotlinActivity, "Interstitial Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyDelayedShowInterstitial()
                }

                override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                    toast(this@InterstitialKotlinActivity, "Interstitial Ads Completed")

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
            delayedShowInterstitial == null ->
                toast(this, "Load Interstitial First")
            delayedShowInterstitial?.isLoaded == false ->
                toast(this, "Interstitial not Loaded yet")
            else -> delayedShowInterstitial!!.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyInterstitialAd()
        destroyVideoAd()
        destroyDelayedShowInterstitial()
    }

    private fun destroyInterstitialAd() {
        interstitialAd?.destroy()
    }

    private fun destroyVideoAd() {
        videoAd?.destroy()
    }

    private fun destroyDelayedShowInterstitial() {
        delayedShowInterstitial?.destroy()
    }

}
