package io.bidmachine.examples

import android.os.Bundle
import android.view.View
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.nativead.NativeAd
import io.bidmachine.nativead.NativeRequest
import io.bidmachine.nativead.SimpleNativeListener
import io.bidmachine.nativead.view.NativeAdContentLayout
import io.bidmachine.utils.BMError
import kotlinx.android.synthetic.main.activity_native.*

class NativeKotlinActivity : BaseKotlinExampleActivity() {

    private lateinit var nativeAdContentLayout: NativeAdContentLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialise SDK
        BidMachine.initialize(this, "5")

        //Enable logs
        BidMachine.setLoggingEnabled(true)

        //Set activity content view
        setContentView(R.layout.activity_native)

        //Set listener to perform Ads load
        btnLoadAd.setOnClickListener {
            loadAd()
        }

        //Find NativeAdContentLayout in hierarchy
        nativeAdContentLayout = findViewById(R.id.native_item)
    }

    private fun loadAd() {
        setDebugState(Status.Loading)

        //Create native request
        val request = NativeRequest.Builder().build()

        //Load Native Ad
        val nativeAd = NativeAd(this)
        nativeAd.setListener(object : SimpleNativeListener() {
            override fun onAdLoaded(ad: NativeAd) {
                setDebugState(Status.Loaded)

                //Show native Ads
                nativeAdContentLayout.bind(ad)
                nativeAdContentLayout.registerViewForInteraction(nativeAd)
                nativeAdContentLayout.visibility = View.VISIBLE
            }

            override fun onAdLoadFailed(ad: NativeAd, error: BMError) {
                setDebugState(Status.LoadFail)
            }
        })
        nativeAd.load(request)
    }

    override fun onDestroy() {
        super.onDestroy()

        //Destroy Ad when you finish with it
        nativeAdContentLayout.destroy()
    }

}
