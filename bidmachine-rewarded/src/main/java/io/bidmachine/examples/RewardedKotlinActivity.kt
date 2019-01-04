package io.bidmachine.examples

import android.os.Bundle
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.base.Utils.toast
import io.bidmachine.rewarded.RewardedAd
import io.bidmachine.rewarded.RewardedRequest
import io.bidmachine.rewarded.SimpleRewardedListener
import io.bidmachine.utils.BMError
import kotlinx.android.synthetic.main.activity_rewarded.*

class RewardedKotlinActivity : BaseKotlinExampleActivity() {

    private var rewardedAd: RewardedAd? = null
    private var delayedShowRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialise SDK
        BidMachine.initialize(this, "1")

        //Enable logs
        BidMachine.setLoggingEnabled(true)

        //Set activity content view
        setContentView(R.layout.activity_rewarded)

        //Set listeners to perform Ads load
        btnShowRewarded.setOnClickListener { showRewarded() }
        btnLoadRewarded.setOnClickListener { loadRewarded() }
        btnShowLoadedRewarded.setOnClickListener { showLoadedRewarded() }
    }

    private fun showRewarded() {
        setDebugState(Status.Loading)

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
                    setDebugState(Status.Loaded, "Rewarded Ads Loaded")

                    //Show RewardedAd
                    ad.show()
                }

                override fun onAdLoadFailed(ad: RewardedAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Rewarded Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyRewarded()
                }

                override fun onAdClosed(ad: RewardedAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Rewarded Ads Closed")

                    //Destroy loaded ad since it not required any more
                    destroyRewarded()
                }

                override fun onAdRewarded(ad: RewardedAd) {
                    setDebugState(Status.Rewarded, "Rewarded Ads Rewarded")

                    //Here you can start you reward process
                }
            })

            //Load Rewarded Ads
            load(rewardedRequest)
        }
    }

    private fun loadRewarded() {
        setDebugState(Status.Loading)

        //Destroy previous loaded RewardedAd
        destroyDelayedShowRewarded()

        //Create new RewardedRequest
        val rewardedRequest = RewardedRequest.Builder()
                .build()

        //Create new RewardedAd
        delayedShowRewardedAd = RewardedAd(this).apply {

            //Set RewardedAd events listener
            setListener(object : SimpleRewardedListener() {
                override fun onAdLoaded(ad: RewardedAd) {
                    setDebugState(Status.Loaded, "Rewarded Ads Loaded")
                }

                override fun onAdLoadFailed(ad: RewardedAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Rewarded Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyDelayedShowRewarded()
                }

                override fun onAdClosed(ad: RewardedAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Rewarded Ads Closed")

                    //Destroy loaded ad since it not required any more
                    destroyDelayedShowRewarded()
                }

                override fun onAdRewarded(ad: RewardedAd) {
                    setDebugState(Status.Rewarded, "Rewarded Ads Rewarded")

                    //Here you can start you reward process
                }
            })

            //Load Rewarded
            load(rewardedRequest)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        //Destroy Ads when you finish with it
        destroyRewarded()
        destroyDelayedShowRewarded()
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

    private fun showLoadedRewarded() {
        when {
            delayedShowRewardedAd == null ->
                toast(this, "Load Rewarded First")
            delayedShowRewardedAd?.isLoaded == false ->
                toast(this, "Rewarded not Loaded yet")
            else -> delayedShowRewardedAd!!.show()
        }
    }
}
