package io.bidmachine.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import io.bidmachine.AdsFormat
import io.bidmachine.BidMachine
import io.bidmachine.ads.networks.adcolony.AdColonyConfig
import io.bidmachine.ads.networks.amazon.AmazonConfig
import io.bidmachine.ads.networks.criteo.CriteoConfig
import io.bidmachine.ads.networks.meta_audience.MetaAudienceConfig
import io.bidmachine.ads.networks.my_target.MyTargetConfig
import io.bidmachine.ads.networks.tapjoy.TapjoyConfig
import io.bidmachine.ads.networks.vungle.VungleConfig
import io.bidmachine.banner.BannerRequest
import io.bidmachine.banner.BannerSize
import io.bidmachine.banner.BannerView
import io.bidmachine.banner.SimpleBannerListener
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.databinding.ActivityHeaderBiddingBinding
import io.bidmachine.interstitial.InterstitialAd
import io.bidmachine.interstitial.InterstitialRequest
import io.bidmachine.interstitial.SimpleInterstitialListener
import io.bidmachine.nativead.NativeAd
import io.bidmachine.nativead.NativeRequest
import io.bidmachine.nativead.SimpleNativeListener
import io.bidmachine.nativead.view.NativeAdContentLayout
import io.bidmachine.rewarded.RewardedAd
import io.bidmachine.rewarded.RewardedRequest
import io.bidmachine.rewarded.SimpleRewardedListener
import io.bidmachine.utils.BMError

class HeaderBiddingKotlinActivity : BaseKotlinExampleActivity<ActivityHeaderBiddingBinding>() {

    private var bannerView: BannerView? = null
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var nativeAd: NativeAd? = null

    override fun inflate(inflater: LayoutInflater) = ActivityHeaderBiddingBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 3rd party networks should be configure before SDK initialization
        configureHeaderBiddingNetworks()

        // Initialise SDK
        BidMachine.initialize(this, SOURCE_ID)

        // Enable logs
        BidMachine.setLoggingEnabled(true)

        // Set listeners to perform Ads load
        binding.bShowBanner.setOnClickListener {
            showBanner()
        }
        binding.bShowInterstitial.setOnClickListener {
            showInterstitial()
        }
        binding.bShowRewarded.setOnClickListener {
            showRewarded()
        }
        binding.bShowNative.setOnClickListener {
            showNative()
        }
    }

    private fun configureHeaderBiddingNetworks() {
        BidMachine.registerNetworks(
            // Configure AdColony network
            AdColonyConfig("app185a7e71e1714831a49ec7")
                    .withMediationConfig(AdsFormat.InterstitialVideo,
                                         "vz06e8c32a037749699e7050")
                    .withMediationConfig(AdsFormat.RewardedVideo,
                                         "vz1fd5a8b2bf6841a0a4b826"),
            // Configure Amazon network
            AmazonConfig("a9_onboarding_app_id")
                    .withMediationConfig(AdsFormat.Banner_320x50,
                                         "5ab6a4ae-4aa5-43f4-9da4-e30755f2b295")
                    .withMediationConfig(AdsFormat.Banner_300x250,
                                         "54fb2d08-c222-40b1-8bbe-4879322dc04b")
                    .withMediationConfig(AdsFormat.Banner_728x90,
                                         "bed17ec3-b185-453e-b2a8-4a3c6bb9234d")
                    .withMediationConfig(AdsFormat.InterstitialStatic,
                                         "4e918ac0-5c68-4fe1-8d26-4e76e8f74831")
                    .withMediationConfig(AdsFormat.InterstitialVideo,
                                         "4acc26e6-3ada-4ee8-bae0-753c1e0ad278"),
            // Configure Criteo
            CriteoConfig("B-057601")
                    .withMediationConfig(AdsFormat.Banner_320x50,
                                         "30s6zt3ayypfyemwjvmp")
                    .withMediationConfig(AdsFormat.Interstitial,
                                         "6yws53jyfjgoq1ghnuqb"),
            // Configure MetaAudience network
            MetaAudienceConfig("1525692904128549")
                    .withMediationConfig(AdsFormat.Banner,
                                         "1525692904128549_2386746951356469")
                    .withMediationConfig(AdsFormat.Banner_300x250,
                                         "1525692904128549_2386746951356469")
                    .withMediationConfig(AdsFormat.InterstitialStatic,
                                         "1525692904128549_2386743441356820")
                    .withMediationConfig(AdsFormat.RewardedVideo,
                                         "1525692904128549_2386753464689151"),
            // Configure myTarget network
            MyTargetConfig()
                    .withMediationConfig(AdsFormat.Banner,
                                         "437933")
                    .withMediationConfig(AdsFormat.Banner_320x50,
                                         "437933")
                    .withMediationConfig(AdsFormat.Banner_300x250,
                                         "64526")
                    .withMediationConfig(AdsFormat.Banner_728x90,
                                         "81620")
                    .withMediationConfig(AdsFormat.InterstitialStatic,
                                         "365991")
                    .withMediationConfig(AdsFormat.RewardedVideo,
                                         "482205"),
            // Configure Tapjoy network
            TapjoyConfig("tmyN5ZcXTMyjeJNJmUD5ggECAbnEGtJREmLDd0fvqKBXcIr7e1dvboNKZI4y")
                    .withMediationConfig(AdsFormat.InterstitialVideo,
                                         "video_without_cap_pb")
                    .withMediationConfig(AdsFormat.RewardedVideo,
                                         "rewarded_video_without_cap_pb"),
            // Configure Vungle network
            VungleConfig("60cb5dadf2a364141bd2083c", "561e8d956b8d90f61a003219")
                    .withMediationConfig(AdsFormat.Interstitial,
                                         "INTERTITIAL_NO_SKIPPABLE_1-2245459")
                    .withMediationConfig(AdsFormat.Rewarded,
                                         "REWARDED_NON_SKIPPABLE-1381336")
        )
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

    private fun showBanner() {
        setDebugState(Status.Loading)

        // Destroy previous loaded Banner Ads
        destroyCurrentBannerView()

        // Create banner request
        val request = BannerRequest.Builder()
                .setSize(BannerSize.Size_320x50)
                .build()

        bannerView = BannerView(this).apply {
            setListener(object : SimpleBannerListener() {
                override fun onAdLoaded(ad: BannerView) {
                    setDebugState(Status.Loaded, "Banner Ads loaded")

                    // Show Banner Ad
                    showAdView(ad)
                }

                override fun onAdLoadFailed(ad: BannerView, error: BMError) {
                    setDebugState(Status.LoadFail, "Banner Ads load failed")

                    // Destroy loaded ad since it not required any more
                    destroyCurrentBannerView()
                }
            })

            // Load Banner Ads
            load(request)
        }
    }

    private fun destroyCurrentBannerView() {
        bannerView?.destroy()
        bannerView = null
    }

    private fun showInterstitial() {
        setDebugState(Status.Loading)

        // Destroy previous loaded Interstitial Ads
        destroyCurrentInterstitialAd()

        // Create new InterstitialRequest
        val interstitialRequest = InterstitialRequest.Builder().build()

        // Create new InterstitialAd
        interstitialAd = InterstitialAd(this).apply {
            setListener(object : SimpleInterstitialListener() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    setDebugState(Status.Loaded, "Interstitial Ads Loaded")

                    // Show Interstitial Ad
                    ad.show()
                }

                override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Interstitial Ads Load Failed")

                    // Destroy loaded ad since it not required any more
                    destroyCurrentInterstitialAd()
                }

                override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Interstitial Ads Closed")

                    // Destroy loaded ad since it not required any more
                    destroyCurrentInterstitialAd()
                }
            })

            // Load InterstitialAd
            load(interstitialRequest)
        }
    }

    private fun destroyCurrentInterstitialAd() {
        interstitialAd?.destroy()
        interstitialAd = null
    }

    private fun showRewarded() {
        setDebugState(Status.Loading)

        // Destroy previous loaded RewardedAd
        destroyCurrentRewardedAd()

        // Create new RewardedRequest
        val rewardedRequest = RewardedRequest.Builder().build()

        // Create new RewardedAd
        rewardedAd = RewardedAd(this).apply {
            setListener(object : SimpleRewardedListener() {
                override fun onAdLoaded(ad: RewardedAd) {
                    setDebugState(Status.Loaded, "Rewarded Ads Loaded")

                    // Show RewardedAd
                    ad.show()
                }

                override fun onAdLoadFailed(ad: RewardedAd, error: BMError) {
                    setDebugState(Status.LoadFail, "Rewarded Ads Load Failed")

                    // Destroy loaded ad since it not required any more
                    destroyCurrentRewardedAd()
                }

                override fun onAdClosed(ad: RewardedAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Rewarded Ads Closed")

                    // Destroy loaded ad since it not required any more
                    destroyCurrentRewardedAd()
                }

                override fun onAdRewarded(ad: RewardedAd) {
                    setDebugState(Status.Rewarded, "Rewarded Ads Rewarded")

                    // Here you can start you reward process
                }
            })

            // Load RewardedAd
            load(rewardedRequest)
        }
    }

    private fun destroyCurrentRewardedAd() {
        rewardedAd?.destroy()
        rewardedAd = null
    }

    private fun showNative() {
        setDebugState(Status.Loading)

        // Destroy previous loaded NativeAd
        destroyCurrentNativeAd()

        // Create new NativeRequest
        val nativeRequest = NativeRequest.Builder().build()

        // Create new NativeAd
        nativeAd = NativeAd(this).apply {
            setListener(object : SimpleNativeListener() {
                override fun onAdLoaded(ad: NativeAd) {
                    setDebugState(Status.Loaded, "NativeAd Loaded")

                    // Show native Ads
                    val nativeView = createNativeAdView(ad)
                    showAdView(nativeView)
                }

                override fun onAdLoadFailed(ad: NativeAd, error: BMError) {
                    setDebugState(Status.LoadFail, "NativeAd Load Failed")

                    // Destroy loaded ad since it not required any more
                    destroyCurrentNativeAd()
                }
            })

            // Load NativeAd
            load(nativeRequest)
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