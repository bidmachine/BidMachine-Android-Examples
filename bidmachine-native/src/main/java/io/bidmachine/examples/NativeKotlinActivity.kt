package io.bidmachine.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.base.Status
import io.bidmachine.examples.databinding.ActivityNativeBinding
import io.bidmachine.nativead.NativeAd
import io.bidmachine.nativead.NativeRequest
import io.bidmachine.nativead.SimpleNativeListener
import io.bidmachine.utils.BMError

class NativeKotlinActivity : BaseKotlinExampleActivity<ActivityNativeBinding>() {

    private var nativeAd: NativeAd? = null

    override fun inflate(inflater: LayoutInflater) = ActivityNativeBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise SDK
        BidMachine.initialize(this, SOURCE_ID)

        // Enable logs
        BidMachine.setLoggingEnabled(true)

        // Set listener to perform Ads load
        binding.bLoadAd.setOnClickListener {
            loadAd()
        }
    }

    private fun loadAd() {
        setDebugState(Status.Loading)

        // Destroy previous loaded NativeAd
        destroyNativeAd()

        // Create native request
        val request = NativeRequest.Builder().build()

        // Load Native Ad
        nativeAd = NativeAd(this).apply {
            setListener(object : SimpleNativeListener() {
                override fun onAdLoaded(ad: NativeAd) {
                    setDebugState(Status.Loaded)

                    // Show native Ads
                    binding.nativeAd.nativeAdContentLayout.bind(ad)
                    binding.nativeAd.nativeAdContentLayout.registerViewForInteraction(ad)
                    binding.nativeAd.nativeAdContentLayout.visibility = View.VISIBLE
                }

                override fun onAdLoadFailed(ad: NativeAd, error: BMError) {
                    setDebugState(Status.LoadFail)
                }
            })

            // Load NativeAd
            load(request)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Destroy Ad when you finish with it
        binding.nativeAd.nativeAdContentLayout.destroy()

        destroyNativeAd()
    }

    private fun destroyNativeAd() {
        nativeAd?.destroy()
        nativeAd = null
    }

}