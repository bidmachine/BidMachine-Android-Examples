package io.bidmachine.examples

import android.os.Bundle
import android.view.LayoutInflater
import io.bidmachine.AdPlacementConfig
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.base.Status
import io.bidmachine.examples.databinding.ActivityRewardedBinding
import io.bidmachine.rewarded.RewardedAd
import io.bidmachine.rewarded.RewardedRequest
import io.bidmachine.rewarded.SimpleRewardedListener
import io.bidmachine.utils.BMError

class RewardedKotlinActivity : BaseKotlinExampleActivity<ActivityRewardedBinding>() {

    private var rewardedAd: RewardedAd? = null
    private var delayedShowRewardedAd: RewardedAd? = null

    override fun inflate(inflater: LayoutInflater) = ActivityRewardedBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise SDK
        BidMachine.initialize(this, SOURCE_ID)

        // Enable logs
        BidMachine.setLoggingEnabled(true)

        // Set listeners to perform Ads load
        binding.bShowRewarded.setOnClickListener {
            showRewarded()
        }
        binding.bLoadRewarded.setOnClickListener {
            loadRewarded()
        }
        binding.bShowLoadedRewarded.setOnClickListener {
            showLoadedRewarded()
        }
    }

    private fun showRewarded() {
        setDebugState(Status.Loading)

        // Destroy previous loaded RewardedAd
        destroyRewarded()

        // Create placement configuration
        val config = AdPlacementConfig.rewardedBuilder().build()

        // Create new RewardedRequest
        val rewardedRequest = RewardedRequest.Builder(config).build()

        // Create new RewardedAd
        rewardedAd = RewardedAd(this).apply {

            // Set RewardedAd events listener
            setListener(object : SimpleRewardedListener() {
                override fun onAdLoaded(ad: RewardedAd) {
                    setDebugState(Status.Loaded, "Rewarded Ads Loaded")

                    // Show RewardedAd
                    ad.show()
                }

                override fun onAdLoadFailed(ad: RewardedAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Rewarded Ads Load Failed")

                    // Destroy loaded ad since it not required any more
                    destroyRewarded()
                }

                override fun onAdClosed(ad: RewardedAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Rewarded Ads Closed")

                    // Destroy loaded ad since it not required any more
                    destroyRewarded()
                }

                override fun onAdRewarded(ad: RewardedAd) {
                    setDebugState(Status.Rewarded, "Rewarded Ads Rewarded")

                    // Here you can start you reward process
                }
            })

            // Load Rewarded Ads
            load(rewardedRequest)
        }
    }

    private fun loadRewarded() {
        setDebugState(Status.Loading)

        // Destroy previous loaded RewardedAd
        destroyDelayedShowRewarded()

        // Create placement configuration
        val config = AdPlacementConfig.rewardedBuilder().build()

        // Create new RewardedRequest
        val rewardedRequest = RewardedRequest.Builder(config).build()

        // Create new RewardedAd
        delayedShowRewardedAd = RewardedAd(this).apply {

            // Set RewardedAd events listener
            setListener(object : SimpleRewardedListener() {
                override fun onAdLoaded(ad: RewardedAd) {
                    setDebugState(Status.Loaded, "Rewarded Ads Loaded")
                }

                override fun onAdLoadFailed(ad: RewardedAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Rewarded Ads Load Failed")

                    // Destroy loaded ad since it not required any more
                    destroyDelayedShowRewarded()
                }

                override fun onAdClosed(ad: RewardedAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Rewarded Ads Closed")

                    // Destroy loaded ad since it not required any more
                    destroyDelayedShowRewarded()
                }

                override fun onAdRewarded(ad: RewardedAd) {
                    setDebugState(Status.Rewarded, "Rewarded Ads Rewarded")

                    // Here you can start you reward process
                }
            })

            // Load Rewarded
            load(rewardedRequest)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Destroy Ads when you finish with it
        destroyRewarded()
        destroyDelayedShowRewarded()
    }

    private fun destroyRewarded() {
        rewardedAd?.destroy()
        rewardedAd = null
    }

    private fun destroyDelayedShowRewarded() {
        delayedShowRewardedAd?.destroy()
        delayedShowRewardedAd = null
    }

    private fun showLoadedRewarded() {
        when {
            delayedShowRewardedAd == null -> {
                toast("Load Rewarded First")
            }

            delayedShowRewardedAd?.isLoaded == false -> {
                toast("Rewarded not Loaded yet")
            }

            else -> {
                delayedShowRewardedAd!!.show()
            }
        }
    }

}