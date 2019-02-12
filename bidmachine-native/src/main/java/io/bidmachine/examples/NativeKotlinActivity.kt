package io.bidmachine.examples

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.nativead.NativeAd
import io.bidmachine.nativead.NativeAdContentLayout
import io.bidmachine.nativead.NativeRequest
import io.bidmachine.nativead.SimpleNativeListener
import io.bidmachine.utils.BMError
import kotlinx.android.synthetic.main.activity_native.*
import kotlinx.android.synthetic.main.native_ad_layout.view.*

class NativeKotlinActivity : BaseKotlinExampleActivity() {

    private var nativeAd: NativeAd? = null
    private var delayedShowNativeAd: NativeAd? = null

    private lateinit var nativeParent: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialise SDK
        BidMachine.initialize(this, "1")

        //Enable logs
        BidMachine.setLoggingEnabled(true)

        //Set activity content view
        setContentView(R.layout.activity_native)

        //Find parent for display native ads
        nativeParent = findViewById(R.id.nativeParent)

        //Set listeners to perform Ads load
        btnShowNative.setOnClickListener { showNativeAd() }
        btnLoadNative.setOnClickListener { loadNativeAd() }
        btnShowLoadedNative.setOnClickListener { showLoadedNativeAd() }
    }

    private fun showNativeAd() {
        setDebugState(Status.Loading)

        //Destroy previous loaded native ad, since it's not needed anymore
        destroyNativeAd()

        //Create new Native request
        val nativeRequest = NativeRequest.Builder()
                .build()

        //Create new NativeAd object
        nativeAd = NativeAd(this).apply {
            //Set Native Ads listener
            setListener(object : SimpleNativeListener() {
                override fun onAdLoaded(ad: NativeAd) {
                    setDebugState(Status.Loaded, "Native Ads Loaded")

                    //Show native ad
                    inflateNativeAdWithBind(ad)
                }

                override fun onAdLoadFailed(ad: NativeAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Native Ads Load Failed")

                    //Destroy ad since it not needed anymore
                    if (ad == nativeAd) {
                        destroyNativeAd()
                    } else {
                        ad.destroy()
                    }
                }
            })
            //Load native ad
            load(nativeRequest)
        }
    }

    private fun loadNativeAd() {
        setDebugState(Status.Loading)

        //Destroy previous loaded native ad, since it's not needed anymore
        destroyLoadedNativeAd()

        //Create new Native request
        val nativeRequest = NativeRequest.Builder().build()

        //Create new NativeAd object
        delayedShowNativeAd = NativeAd(this).apply {
            //Set Native Ads listener
            setListener(object : SimpleNativeListener() {
                override fun onAdLoaded(ad: NativeAd) {
                    setDebugState(Status.Loaded, "Native Ads Loaded")
                }

                override fun onAdLoadFailed(ad: NativeAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Native Ads Load Failed")

                    //Destroy ad since it not needed anymore
                    if (ad == delayedShowNativeAd) {
                        destroyLoadedNativeAd()
                    } else {
                        ad.destroy()
                    }
                }
            })
            //Load native ad
            load(nativeRequest)
        }
    }

    private fun showLoadedNativeAd() {
        if (delayedShowNativeAd == null) {
            toast("Load Interstitial First")
        } else if (!delayedShowNativeAd!!.canShow()) {
            toast("Interstitial not Loaded yet")
        } else {
            inflateNativeAdWithBind(delayedShowNativeAd)
        }
    }

    private fun inflateNativeAdWithBind(ad: NativeAd?) {
        if (ad == null) {
            return
        }
        //Prepare layout for native ads
        val nativeAdContentLayout = layoutInflater.inflate(
                R.layout.native_ad_layout, nativeParent, false) as NativeAdContentLayout
        val tvAgeRestrictions = nativeAdContentLayout.txtAgeRestriction
        if (ad.ageRestrictions != null) {
            tvAgeRestrictions.text = ad.ageRestrictions
            tvAgeRestrictions.visibility = View.VISIBLE
        } else {
            tvAgeRestrictions.visibility = View.GONE
        }
        //Bind NativeAd to NativeAdContentLayout
        nativeAdContentLayout.bind(ad)
        //Register NativeAdContentLayout for interactions
        nativeAdContentLayout.registerViewForInteraction(ad)
        nativeAdContentLayout.visibility = View.VISIBLE
        nativeParent.removeAllViews()
        nativeParent.addView(nativeAdContentLayout)
    }

    override fun onDestroy() {
        super.onDestroy()
        //Destroy Ads when you finish with it
        destroyNativeAd()
        destroyLoadedNativeAd()
    }

    private fun destroyNativeAd() {
        //Destroy Ads when you finish with it
        if (nativeAd != null) {
            nativeAd!!.destroy()
            nativeAd = null
        }
    }

    private fun destroyLoadedNativeAd() {
        //Destroy Ads when you finish with it
        if (delayedShowNativeAd != null) {
            delayedShowNativeAd!!.destroy()
            delayedShowNativeAd = null
        }
    }

}
