package io.bidmachine.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import io.bidmachine.AdLoadCallback
import io.bidmachine.BMError
import io.bidmachine.BidMachine
import io.bidmachine.BidMachineInstance
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.base.Status
import io.bidmachine.examples.databinding.ActivityNativeBinding
import io.bidmachine.format.nativead.NativeAd
import io.bidmachine.format.nativead.NativeListener
import io.bidmachine.format.nativead.view.NativeAdContentLayout

class NativeKotlinActivity : BaseKotlinExampleActivity<ActivityNativeBinding>() {

    private var bidMachineInstance: BidMachineInstance? = null
    private var nativeAd: NativeAd? = null

    override fun inflate(inflater: LayoutInflater) = ActivityNativeBinding.inflate(inflater)

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

        bidMachineInstance?.loadNative(adLoadCallback = NativeAdLoadCallback())
            ?: toast("Initialize BidMachine first!")
    }

    private fun showAd(nativeAd: NativeAd) {
        // Destroy previous loaded NativeAd
        destroyNativeAd()

        // Keep a reference to NativeAd: you need it to show the ad,
        // listen to its events and destroy it when you're done
        this.nativeAd = nativeAd
        nativeAd.setListener(NativeAdListener())

        // Bind NativeAd to the layout
        val nativeAdContentLayout: NativeAdContentLayout = binding.nativeAd.nativeAdContentLayout
        if (nativeAdContentLayout.bind(nativeAd)) {
            nativeAdContentLayout.visibility = View.VISIBLE
        } else {
            toast("Native Ad can't be bound")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Destroy Ad when you finish with it
        destroyNativeAd()

        binding.nativeAd.nativeAdContentLayout.destroy()
    }

    private fun destroyNativeAd() {
        binding.nativeAd.nativeAdContentLayout.apply {
            unbind()
            visibility = View.GONE
        }

        nativeAd?.destroy()
        nativeAd = null
    }


    private inner class NativeAdLoadCallback : AdLoadCallback<NativeAd> {

        override fun onAdLoaded(ad: NativeAd) {
            setDebugState(
                status = Status.Loaded,
                message = "Native Ads loaded"
            )

            showAd(ad)
        }

        override fun onAdLoadFailed(bmError: BMError) {
            setDebugState(
                status = Status.LoadFail,
                message = "Native Ads load failed"
            )
        }

    }

    private inner class NativeAdListener : NativeListener {

        override fun onAdClicked(ad: NativeAd) {
            setDebugState(
                status = Status.Clicked,
                message = "Native Ads clicked"
            )
        }

        override fun onAdPresented(ad: NativeAd) {
            toast("Native Ads presented")
        }

        override fun onAdShowFailed(ad: NativeAd, bmError: BMError) {
            setDebugState(
                status = Status.ShowFail,
                message = "Native Ads show failed"
            )

            // Destroy ad since it not required any more
            destroyNativeAd()
        }

        override fun onAdImpression(ad: NativeAd) {
            setDebugState(
                status = Status.Impression,
                message = "Native Ads impression"
            )
        }

        override fun onAdExpired(ad: NativeAd) {
            setDebugState(
                status = Status.Expired,
                message = "Native Ads expired"
            )

            // Destroy ad since it not required any more
            destroyNativeAd()
        }

    }

}