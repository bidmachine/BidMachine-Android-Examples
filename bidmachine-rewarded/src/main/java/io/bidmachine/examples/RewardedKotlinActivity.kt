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
import io.bidmachine.examples.databinding.ActivityRewardedBinding
import io.bidmachine.format.rewardedad.RewardedAd
import io.bidmachine.format.rewardedad.RewardedListener

class RewardedKotlinActivity : BaseKotlinExampleActivity<ActivityRewardedBinding>() {

    private var bidMachineInstance: BidMachineInstance? = null
    private var rewardedAd: RewardedAd? = null

    override fun inflate(inflater: LayoutInflater) = ActivityRewardedBinding.inflate(inflater)

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
        binding.bLoadRewarded.setOnClickListener {
            loadAd()
        }
        binding.bShowRewarded.setOnClickListener {
            showAd()
        }
    }

    private fun loadAd() {
        setDebugState(Status.Loading)

        bidMachineInstance?.loadRewarded(adLoadCallback = RewardedAdLoadCallback())
            ?: toast("Initialize BidMachine first!")
    }

    private fun showAd() {
        val rewardedAd: RewardedAd? = rewardedAd
        when {
            rewardedAd == null -> {
                toast("Load Rewarded First")
            }

            !(rewardedAd as Ad).canShow() -> {
                toast("Rewarded can't be shown")
            }

            else -> {
                rewardedAd.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Destroy Ads when you finish with it
        destroyRewardedAd()
    }

    // canShow() and destroy() are called through the Ad interface,
    // since Kotlin can't resolve them directly on RewardedAd in BidMachine 4.0.0
    private fun destroyRewardedAd() {
        (rewardedAd as Ad?)?.destroy()
        rewardedAd = null
    }


    private inner class RewardedAdLoadCallback : AdLoadCallback<RewardedAd> {

        override fun onAdLoaded(ad: RewardedAd) {
            setDebugState(
                status = Status.Loaded,
                message = "Rewarded Ads Loaded"
            )

            // Destroy previous loaded RewardedAd
            destroyRewardedAd()

            // Keep a reference to RewardedAd: you need it to show the ad,
            // listen to its events and destroy it when you're done
            rewardedAd = ad
            ad.setListener(RewardedAdListener())
        }

        override fun onAdLoadFailed(bmError: BMError) {
            setDebugState(
                status = Status.LoadFail,
                message = "Rewarded Ads Load Failed"
            )
        }

    }

    private inner class RewardedAdListener : RewardedListener {

        override fun onAdPresented(ad: RewardedAd) {
            toast("Rewarded Ads Presented")
        }

        override fun onAdShowFailed(ad: RewardedAd, bmError: BMError) {
            setDebugState(
                status = Status.ShowFail,
                message = "Rewarded Ads Show Failed"
            )

            // Destroy ad since it not required any more
            destroyRewardedAd()
        }

        override fun onAdImpression(ad: RewardedAd) {
            setDebugState(
                status = Status.Impression,
                message = "Rewarded Ads Impression"
            )
        }

        override fun onAdClicked(ad: RewardedAd) {
            setDebugState(
                status = Status.Clicked,
                message = "Rewarded Ads Clicked"
            )
        }

        override fun onAdExpired(ad: RewardedAd) {
            setDebugState(
                status = Status.Expired,
                message = "Rewarded Ads Expired"
            )

            // Destroy ad since it not required any more
            destroyRewardedAd()
        }

        override fun onAdRewarded(ad: RewardedAd) {
            setDebugState(
                status = Status.Rewarded,
                message = "Rewarded Ads Rewarded"
            )

            // Here you can start you reward process
        }

        override fun onAdClosed(ad: RewardedAd) {
            setDebugState(
                status = Status.Closed,
                message = "Rewarded Ads Closed"
            )

            // Destroy ad since it not required any more
            destroyRewardedAd()
        }

    }

}