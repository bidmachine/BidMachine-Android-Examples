package io.bidmachine.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import io.bidmachine.BidMachine
import io.bidmachine.banner.BannerRequest
import io.bidmachine.banner.BannerSize
import io.bidmachine.banner.BannerView
import io.bidmachine.banner.SimpleBannerListener
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.databinding.ActivityRequestsBinding
import io.bidmachine.interstitial.InterstitialAd
import io.bidmachine.interstitial.InterstitialRequest
import io.bidmachine.interstitial.SimpleInterstitialListener
import io.bidmachine.models.AuctionResult
import io.bidmachine.nativead.NativeAd
import io.bidmachine.nativead.NativeRequest
import io.bidmachine.nativead.SimpleNativeListener
import io.bidmachine.nativead.view.NativeAdContentLayout
import io.bidmachine.rewarded.RewardedAd
import io.bidmachine.rewarded.RewardedRequest
import io.bidmachine.rewarded.SimpleRewardedListener
import io.bidmachine.utils.BMError

class RequestsKotlinExample : BaseKotlinExampleActivity<ActivityRequestsBinding>() {

    private var bannerView: BannerView? = null
    private var bannerRequest: BannerRequest? = null
    private var interstitialAd: InterstitialAd? = null
    private var interstitialRequest: InterstitialRequest? = null
    private var rewardedAd: RewardedAd? = null
    private var rewardedRequest: RewardedRequest? = null
    private var nativeAd: NativeAd? = null
    private var nativeRequest: NativeRequest? = null

    override fun inflate(inflater: LayoutInflater) = ActivityRequestsBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise SDK
        BidMachine.initialize(this, "5")

        // Enable logs
        BidMachine.setLoggingEnabled(true)

        // Set listener to perform Ads request
        binding.bRequestBanner.setOnClickListener {
            requestBanner()
        }

        // Set listener to perform Ads show
        binding.bShowRequestedBanner.setOnClickListener {
            showRequestedBanner()
        }

        // Set listener to perform Interstitial Ads request
        binding.bRequestInterstitial.setOnClickListener {
            requestInterstitial()
        }

        // Set listener to perform Interstitial Ads show
        binding.bShowRequestedInterstitial.setOnClickListener {
            showRequestedInterstitial()
        }

        // Set listener to perform Rewarded Ads request
        binding.bRequestRewarded.setOnClickListener {
            requestRewarded()
        }

        // Set listener to perform Rewarded Ads show
        binding.bShowRequestedRewarded.setOnClickListener {
            showRequestedRewarded()
        }

        // Set listener to perform Native Ads request
        binding.bRequestNative.setOnClickListener {
            requestNative()
        }

        // Set listener to perform Native Ads show
        binding.bShowRequestedNative.setOnClickListener {
            showRequestedNative()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Destroy Ads when you finish with it
        destroyCurrentBannerView()
        destroyCurrentInterstitialAd()
        destroyCurrentRewardedAd()
        destroyCurrentNativeAd()
    }

    private fun showAdView(view: View) {
        binding.adContainer.removeAllViews()
        binding.adContainer.addView(view)
    }

    /**
     * Make note, that AdRequest listeners will be notified in background thread
     */
    private fun requestBanner() {
        setDebugState(Status.Requesting)

        // Create new Banner Ads request
        bannerRequest = BannerRequest.Builder()
                .setSize(BannerSize.Size_320x50)
                // Set Banner Ads request listener
                .setListener(object : BannerRequest.AdRequestListener {
                    override fun onRequestSuccess(request: BannerRequest, auctionResult: AuctionResult) {
                        runOnUiThread {
                            setDebugState(Status.Requested, "Banner Ad Requested")
                        }
                    }

                    override fun onRequestFailed(request: BannerRequest, error: BMError) {
                        runOnUiThread {
                            setDebugState(Status.RequestFail, "Banner Request Failed: ${error.message}")
                        }
                    }

                    override fun onRequestExpired(request: BannerRequest) {
                        runOnUiThread {
                            setDebugState(Status.Expired, "Banner Request Expired")
                        }
                    }
                })
                .build().apply {
                    // Perform Banner Ads request
                    request(this@RequestsKotlinExample)
                }
    }

    private fun showRequestedBanner() {
        when {
            bannerRequest == null -> {
                toast("Please request Banner first")
            }
            bannerRequest?.isExpired == true -> {
                toast("BannerRequest expired, request new one please")
            }
            bannerRequest?.auctionResult == null -> {
                toast("BannerRequest not requested or requested unsuccessfully")
            }
            else -> {
                // Destroy previous BannerView object
                destroyCurrentBannerView()

                bannerView = BannerView(this).apply {
                    // Set listener ot change BannerView visibility when Ads loaded
                    setListener(object : SimpleBannerListener() {
                        override fun onAdLoaded(ad: BannerView) {
                            setDebugState(Status.Loaded, "Banner Ads Loaded")

                            // Show Banner Ad
                            showAdView(ad)
                        }

                        override fun onAdLoadFailed(ad: BannerView, error: BMError) {
                            setDebugState(Status.LoadFail, "Banner Ads load failed: ${error.message}")

                            // Destroy loaded ad since it not required any more
                            destroyCurrentBannerView()
                        }
                    })

                    // Perform BannerAd load
                    load(bannerRequest)
                }
            }
        }
    }

    private fun destroyCurrentBannerView() {
        bannerView?.destroy()
        bannerView = null
    }

    private fun requestInterstitial() {
        setDebugState(Status.Requesting)

        // Create new Interstitial Ads request
        interstitialRequest = InterstitialRequest.Builder()
                // Set Interstitial Ads request listener
                .setListener(object : InterstitialRequest.AdRequestListener {
                    override fun onRequestSuccess(request: InterstitialRequest, auctionResult: AuctionResult) {
                        runOnUiThread {
                            setDebugState(Status.Requested, "Interstitial Ad Requested")
                        }
                    }

                    override fun onRequestFailed(request: InterstitialRequest, bmError: BMError) {
                        runOnUiThread {
                            setDebugState(Status.RequestFail, "Interstitial Request Failed: ${bmError.message}")
                        }
                    }

                    override fun onRequestExpired(request: InterstitialRequest) {
                        runOnUiThread {
                            setDebugState(Status.Expired, "Interstitial Request Expired")
                        }
                    }
                })
                .build().apply {
                    // Perform Interstitial Ads request
                    request(this@RequestsKotlinExample)
                }
    }

    private fun showRequestedInterstitial() {
        when {
            interstitialRequest == null -> {
                toast("Please request Interstitial first")
            }
            interstitialRequest?.isExpired == true -> {
                toast("InterstitialRequest expired, request new one please")
            }
            interstitialRequest?.auctionResult == null -> {
                toast("InterstitialRequest not requested or requested unsuccessfully")
            }
            else -> {
                // Destroy previous InterstitialAd object
                destroyCurrentInterstitialAd()

                // Create new InterstitialAd
                interstitialAd = InterstitialAd(this).apply {
                    setListener(object : SimpleInterstitialListener() {
                        override fun onAdLoaded(ad: InterstitialAd) {
                            setDebugState(Status.Loaded, "Interstitial Ads Loaded")

                            // Show Interstitial Ads
                            ad.show()
                        }

                        override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                            setDebugState(Status.LoadFail, "Interstitial Ads load failed: ${error.message}")

                            // Destroy current Interstitial ad since we don't need it anymore
                            destroyCurrentInterstitialAd()
                        }

                        override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                            setDebugState(Status.Closed)

                            // Destroy current Interstitial ad since we don't need it anymore
                            destroyCurrentInterstitialAd()
                        }
                    })

                    // Perform InterstitialAd load
                    load(interstitialRequest)
                }
            }
        }
    }

    private fun destroyCurrentInterstitialAd() {
        interstitialAd?.destroy()
        interstitialAd = null
    }

    private fun requestRewarded() {
        setDebugState(Status.Requesting)

        // Create new Rewarded Ads request
        rewardedRequest = RewardedRequest.Builder()
                // Set Rewarded Ads request listener
                .setListener(object : RewardedRequest.AdRequestListener {
                    override fun onRequestSuccess(request: RewardedRequest, auctionResult: AuctionResult) {
                        runOnUiThread {
                            setDebugState(Status.Requested, "Rewarded Ad Requested")
                        }
                    }

                    override fun onRequestFailed(request: RewardedRequest, bmError: BMError) {
                        runOnUiThread {
                            setDebugState(Status.RequestFail, "Rewarded Request Failed: ${bmError.message}")
                        }
                    }

                    override fun onRequestExpired(request: RewardedRequest) {
                        runOnUiThread {
                            setDebugState(Status.Expired, "Rewarded Request Expired")
                        }
                    }
                })
                .build().apply {
                    // Perform Rewarded Ads request
                    request(this@RequestsKotlinExample)
                }
    }

    private fun showRequestedRewarded() {
        when {
            rewardedRequest == null -> {
                toast("Please request Rewarded first")
            }
            rewardedRequest?.isExpired == true -> {
                toast("RewardedRequest expired, request new one please")
            }
            rewardedRequest?.auctionResult == null -> {
                toast("RewardedRequest not requested or requested unsuccessfully")
            }
            else -> {
                // Destroy previous RewardedAd object
                destroyCurrentRewardedAd()

                // Create new RewardedAd
                rewardedAd = RewardedAd(this).apply {
                    setListener(object : SimpleRewardedListener() {
                        override fun onAdLoaded(ad: RewardedAd) {
                            setDebugState(Status.Loaded, "Rewarded Ads Loaded")

                            // Show Rewarded Ads
                            ad.show()
                        }

                        override fun onAdLoadFailed(ad: RewardedAd, error: BMError) {
                            setDebugState(Status.LoadFail, "Rewarded Ads load failed: ${error.message}")

                            // Destroy current Rewarded ad since we don't need it anymore
                            destroyCurrentRewardedAd()
                        }

                        override fun onAdClosed(ad: RewardedAd, finished: Boolean) {
                            setDebugState(Status.Closed)

                            // Destroy current Rewarded ad since we don't need it anymore
                            destroyCurrentRewardedAd()
                        }
                    })

                    // Perform RewardedAd load
                    load(rewardedRequest)
                }
            }
        }
    }

    private fun destroyCurrentRewardedAd() {
        rewardedAd?.destroy()
        rewardedAd = null
    }

    private fun requestNative() {
        setDebugState(Status.Requesting)

        // Create new Native Ads request
        nativeRequest = NativeRequest.Builder()
                // Set Native Ads request listener
                .setListener(object : NativeRequest.AdRequestListener {
                    override fun onRequestSuccess(request: NativeRequest, auctionResult: AuctionResult) {
                        runOnUiThread {
                            setDebugState(Status.Requested, "Native Ad Requested")
                        }
                    }

                    override fun onRequestFailed(request: NativeRequest, bmError: BMError) {
                        runOnUiThread {
                            setDebugState(Status.RequestFail, "Native Request Failed: ${bmError.message}")
                        }
                    }

                    override fun onRequestExpired(request: NativeRequest) {
                        runOnUiThread {
                            setDebugState(Status.Expired, "Native Request Expired")
                        }
                    }
                })
                .build().apply {
                    // Perform Native Ads request
                    request(this@RequestsKotlinExample)
                }
    }

    private fun showRequestedNative() {
        when {
            nativeRequest == null -> {
                toast("Please request Native first")
            }
            nativeRequest?.isExpired == true -> {
                toast("NativeRequest expired, request new one please")
            }
            nativeRequest?.auctionResult == null -> {
                toast("NativeRequest not requested or requested unsuccessfully")
            }
            else -> {
                // Destroy previous NativeAd object
                destroyCurrentNativeAd()

                // Create new NativeAd
                nativeAd = NativeAd(this).apply {
                    setListener(object : SimpleNativeListener() {
                        override fun onAdLoaded(ad: NativeAd) {
                            setDebugState(Status.Loaded, "Native Ads Loaded")

                            // Show native Ads
                            val nativeView = createNativeAdView(ad)
                            showAdView(nativeView)
                        }

                        override fun onAdLoadFailed(ad: NativeAd, error: BMError) {
                            setDebugState(Status.LoadFail, "Native Ads load failed: ${error.message}")

                            // Destroy current Native ad since we don't need it anymore
                            destroyCurrentNativeAd()
                        }
                    })

                    // Perform NativeAd load
                    load(nativeRequest)
                }
            }
        }
    }

    private fun createNativeAdView(nativeAd: NativeAd): View {
        val nativeView = LayoutInflater.from(this)
                .inflate(R.layout.native_ad, binding.adContainer, false) as NativeAdContentLayout
        nativeView.bind(nativeAd)
        nativeView.registerViewForInteraction(nativeAd)
        nativeView.visibility = View.VISIBLE
        return nativeView
    }

    private fun destroyCurrentNativeAd() {
        nativeAd?.destroy()
        nativeAd = null
    }

}