package io.bidmachine.examples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.Utils.toast
import io.bidmachine.rewarded.RewardedAd
import io.bidmachine.rewarded.RewardedRequest
import io.bidmachine.rewarded.SimpleRewardedListener
import io.bidmachine.utils.BMError

class RewardedKotlinActivity : AppCompatActivity() {

    private var rewardedAd: RewardedAd? = null
    private var delayedShowRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewarded)

        //Initialise SDK
        BidMachine.initialize(this, "1")

        findViewById<View>(R.id.btnShowRewarded).setOnClickListener { showRewarded() }
        findViewById<View>(R.id.btnLoadRewarded).setOnClickListener { loadRewarded() }
        findViewById<View>(R.id.btnShowLoadedRewarded).setOnClickListener { showLoadedRewarded() }
    }

    private fun showRewarded() {
        //Destroy previous loaded RewardedAd
        destroyRewarded()

        //Create new RewardedRequest
        val rewardedRequest = RewardedRequest.Builder()
                .build()

        //Create new RewardedAd
        rewardedAd = RewardedAd(this).apply {

            //Set RewardedAd events listener
            setListener(object : SimpleRewardedListener() {
                override fun onAdLoaded(ad: RewardedAd) {
                    toast(this@RewardedKotlinActivity, "Rewarded Ads Loaded")

                    //Show RewardedAd
                    ad.show()
                }

                override fun onAdLoadFailed(ad: RewardedAd, error: BMError) {
                    toast(this@RewardedKotlinActivity, "Rewarded Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyRewarded()
                }

                override fun onAdClosed(ad: RewardedAd, finished: Boolean) {
                    toast(this@RewardedKotlinActivity, "Rewarded Ads Completed")

                    //Destroy loaded ad since it not required any more
                    destroyRewarded()
                }

                override fun onAdRewarded(ad: RewardedAd) {
                    toast(this@RewardedKotlinActivity, "Rewarded Ads Completed")
                    //TODO: add note about reward handle
                }
            })

            //Load Rewarded Ads
            load(rewardedRequest)
        }
    }

    private fun loadRewarded() {
        //Destroy previous loaded RewardedAd
        destroyRewarded()

        //Create new RewardedRequest
        val rewardedRequest = RewardedRequest.Builder()
                .build()

        //Create new RewardedAd
        rewardedAd = RewardedAd(this).apply {

            //Set RewardedAd events listener
            setListener(object : SimpleRewardedListener() {
                override fun onAdLoaded(ad: RewardedAd) {
                    toast(this@RewardedKotlinActivity, "Rewarded Ads Loaded")
                }

                override fun onAdLoadFailed(ad: RewardedAd, error: BMError) {
                    toast(this@RewardedKotlinActivity, "Rewarded Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyDelayedShowRewarded()
                }

                override fun onAdClosed(ad: RewardedAd, finished: Boolean) {
                    toast(this@RewardedKotlinActivity, "Rewarded Ads Completed")

                    //Destroy loaded ad since it not required any more
                    destroyDelayedShowRewarded()
                }

                override fun onAdRewarded(ad: RewardedAd) {
                    toast(this@RewardedKotlinActivity, "Rewarded Ads Completed")
                    //TODO: add note about reward handle
                }
            })

            //Load Rewarded
            load(rewardedRequest)
        }
    }

    private fun showLoadedRewarded() {
        when {
            delayedShowRewardedAd == null ->
                toast(this, "Load Rewarded First")
            delayedShowRewardedAd?.isLoaded == false ->
                toast(this, "Rewarded not Loaded yet")
            else -> delayedShowRewardedAd!!.show()
        }
    }

    private fun destroyRewarded() {
        rewardedAd?.let { ad ->
            ad.destroy()
            rewardedAd = null
        }
    }

    private fun destroyDelayedShowRewarded() {
        delayedShowRewardedAd?.let { ad ->
            ad.destroy()
            delayedShowRewardedAd = null
        }
    }

}
