package io.bidmachine.examples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.Utils.toast
import io.bidmachine.rewardedvideo.RewardedVideoAd
import io.bidmachine.rewardedvideo.RewardedVideoRequest
import io.bidmachine.rewardedvideo.SimpleRewardedVideoListener
import io.bidmachine.utils.BMError

class RewardedVideoKotlinActivity : AppCompatActivity() {

    private var rewardedVideo: RewardedVideoAd? = null
    private var delayedShowRewardedVideo: RewardedVideoAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewarded)

        //Set publisher id, which will be used for every request
        BidMachine.setPublisherId("1")

        //Initialise SDK
        BidMachine.initialize(this)

        findViewById<View>(R.id.btnShowRewarded).setOnClickListener { showRewardedVideo() }
        findViewById<View>(R.id.btnLoadRewarded).setOnClickListener { loadRewardedVideo() }
        findViewById<View>(R.id.btnShowLoadedRewarded).setOnClickListener { showLoadedRewardedVideo() }
    }

    private fun showRewardedVideo() {
        //Destroy previous loaded RewardedAd
        destroyRewardedVideo()

        //Create new RewardedVideoRequest
        val rewardedVideoRequest = RewardedVideoRequest.Builder()
                .setAdSpaceId("29")
                .build()

        //Create new RewardedVideoAd
        rewardedVideo = RewardedVideoAd(this).apply {

            //Set RewardedVideoAd events listener
            setListener(object : SimpleRewardedVideoListener() {
                override fun onAdLoaded(ad: RewardedVideoAd) {
                    toast(this@RewardedVideoKotlinActivity, "Rewarded Video Ads Loaded")

                    //Show RewardedVideoAd
                    ad.show()
                }

                override fun onAdLoadFailed(ad: RewardedVideoAd, error: BMError) {
                    toast(this@RewardedVideoKotlinActivity, "Rewarded Video Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyRewardedVideo()
                }

                override fun onAdClosed(ad: RewardedVideoAd, finished: Boolean) {
                    toast(this@RewardedVideoKotlinActivity, "Rewarded Video Ads Completed")

                    //Destroy loaded ad since it not required any more
                    destroyRewardedVideo()
                }

                override fun onAdRewarded(ad: RewardedVideoAd) {
                    toast(this@RewardedVideoKotlinActivity, "Rewarded Video Ads Completed")
                    //TODO: add note about reward handle
                }
            })

            //Load RewardedVideo
            load(rewardedVideoRequest)
        }
    }

    private fun loadRewardedVideo() {
        //Destroy previous loaded RewardedAd
        destroyRewardedVideo()

        //Create new RewardedVideoRequest
        val rewardedVideoRequest = RewardedVideoRequest.Builder()
                .setAdSpaceId("29")
                .build()

        //Create new RewardedVideoAd
        rewardedVideo = RewardedVideoAd(this).apply {

            //Set RewardedVideoAd events listener
            setListener(object : SimpleRewardedVideoListener() {
                override fun onAdLoaded(ad: RewardedVideoAd) {
                    toast(this@RewardedVideoKotlinActivity, "Rewarded Video Ads Loaded")
                }

                override fun onAdLoadFailed(ad: RewardedVideoAd, error: BMError) {
                    toast(this@RewardedVideoKotlinActivity, "Rewarded Video Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyDelayedShowRewardedVideo()
                }

                override fun onAdClosed(ad: RewardedVideoAd, finished: Boolean) {
                    toast(this@RewardedVideoKotlinActivity, "Rewarded Video Ads Completed")

                    //Destroy loaded ad since it not required any more
                    destroyDelayedShowRewardedVideo()
                }

                override fun onAdRewarded(ad: RewardedVideoAd) {
                    toast(this@RewardedVideoKotlinActivity, "Rewarded Video Ads Completed")
                    //TODO: add note about reward handle
                }
            })

            //Load RewardedVideo
            load(rewardedVideoRequest)
        }
    }

    private fun showLoadedRewardedVideo() {
        when {
            delayedShowRewardedVideo == null ->
                toast(this, "Load RewardedVideo First")
            delayedShowRewardedVideo?.isLoaded == false ->
                toast(this, "RewardedVideo not Loaded yet")
            else -> delayedShowRewardedVideo!!.show()
        }
    }

    private fun destroyRewardedVideo() {
        if (rewardedVideo != null) {
            rewardedVideo!!.destroy()
            rewardedVideo = null
        }
    }

    private fun destroyDelayedShowRewardedVideo() {
        if (delayedShowRewardedVideo != null) {
            delayedShowRewardedVideo!!.destroy()
            delayedShowRewardedVideo = null
        }
    }

}
