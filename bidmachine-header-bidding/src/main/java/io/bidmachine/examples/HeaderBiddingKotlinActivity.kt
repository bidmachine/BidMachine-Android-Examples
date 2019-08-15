package io.bidmachine.examples

import android.os.Bundle
import android.view.View
import io.bidmachine.AdsFormat
import io.bidmachine.BidMachine
import io.bidmachine.ads.networks.adcolony.AdColonyConfig
import io.bidmachine.ads.networks.facebook.FacebookConfig
import io.bidmachine.ads.networks.my_target.MyTargetConfig
import io.bidmachine.ads.networks.tapjoy.TapjoyConfig
import io.bidmachine.banner.BannerRequest
import io.bidmachine.banner.BannerSize
import io.bidmachine.banner.BannerView
import io.bidmachine.banner.SimpleBannerListener
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.interstitial.InterstitialAd
import io.bidmachine.interstitial.InterstitialRequest
import io.bidmachine.interstitial.SimpleInterstitialListener
import io.bidmachine.rewarded.RewardedAd
import io.bidmachine.rewarded.RewardedRequest
import io.bidmachine.rewarded.SimpleRewardedListener
import io.bidmachine.utils.BMError
import kotlinx.android.synthetic.main.activity_header_bidding.*

class HeaderBiddingKotlinActivity : BaseKotlinExampleActivity() {

    private var bannerView: BannerView? = null
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //3rd party networks should be configure before SDK initialization
        configureHeaderBiddingNetworks()

        //Initialise SDK
        BidMachine.initialize(this, "1")

        //Enable logs
        BidMachine.setLoggingEnabled(true)

        //Set activity content view
        setContentView(R.layout.activity_header_bidding)

        //Set listeners to perform Ads load
        btnShowBanner.setOnClickListener { showBanner() }
        btnShowInterstitial.setOnClickListener { showInterstitial() }
        btnShowRewarded.setOnClickListener { showRewarded() }
    }

    private fun configureHeaderBiddingNetworks() {
        BidMachine.registerNetworks(
            //Configure AdColony network
            AdColonyConfig("app185a7e71e1714831a49ec7")
                .withMediationConfig(AdsFormat.InterstitialVideo, "vz06e8c32a037749699e7050")
                .withMediationConfig(AdsFormat.RewardedVideo, "vz1fd5a8b2bf6841a0a4b826"),
            //Configure myTarget network
            MyTargetConfig()
                .withMediationConfig(AdsFormat.Banner, "437933")
                .withMediationConfig(AdsFormat.Banner_320x50, "437933")
                .withMediationConfig(AdsFormat.Banner_300x250, "64526")
                .withMediationConfig(AdsFormat.Banner_728x90, "81620")
                .withMediationConfig(AdsFormat.InterstitialStatic, "365991")
                .withMediationConfig(AdsFormat.RewardedVideo, "482205"),
            //Configure Tapjoy network
            TapjoyConfig("tmyN5ZcXTMyjeJNJmUD5ggECAbnEGtJREmLDd0fvqKBXcIr7e1dvboNKZI4y")
                .withMediationConfig(AdsFormat.InterstitialVideo, "video_without_cap_pb")
                .withMediationConfig(AdsFormat.RewardedVideo, "rewarded_video_without_cap_pb"),
            //Configure Facebook network
            FacebookConfig("1525692904128549")
                .withMediationConfig(AdsFormat.Banner, "1525692904128549_2386746951356469")
                .withMediationConfig(AdsFormat.Banner_300x250, "1525692904128549_2386746951356469")
                .withMediationConfig(AdsFormat.InterstitialStatic, "1525692904128549_2386743441356820")
                .withMediationConfig(AdsFormat.RewardedVideo, "1525692904128549_2386753464689151")
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        //Destroy Ads when you finish with it
        if (bannerView != null) {
            bannerView!!.destroy()
        }
        destroyCurrentInterstitialAd()
        destroyCurrentRewardedAd()
    }

    private fun showBanner() {
        if (bannerView == null) {
            bannerView = findViewById<BannerView>(R.id.bannerView).apply {
                //Set Banner Ads events listener
                setListener(object : SimpleBannerListener() {
                    override fun onAdLoaded(ad: BannerView) {
                        setDebugState(Status.Loaded, "Banner Ads loaded")

                        //Make BannerView visible
                        ad.visibility = View.VISIBLE
                    }

                    override fun onAdLoadFailed(ad: BannerView, error: BMError) {
                        setDebugState(Status.LoadFail, "Banner Ads load failed")
                    }
                })
            }
        }
        if (bannerView == null) {
            setDebugState(Status.LoadFail, "BannerView not found")
            return
        }
        setDebugState(Status.Loading)

        //Create banner request
        val request = BannerRequest.Builder()
            .setSize(BannerSize.Size_320x50)
            .build()

        //Load Banner Ads
        bannerView!!.load(request)
    }

    private fun showInterstitial() {
        setDebugState(Status.Loading)

        //Destroy previous loaded Interstitial Ads
        destroyCurrentInterstitialAd()

        //Create new InterstitialRequest
        val interstitialRequest = InterstitialRequest.Builder()
            .build()

        //Create new InterstitialAd
        interstitialAd = InterstitialAd(this).apply {

            //Set InterstitialAd events listener
            setListener(object : SimpleInterstitialListener() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    super.onAdLoaded(ad)
                    setDebugState(Status.Loaded, "Interstitial Ads Loaded")

                    //Show Interstitial Ad
                    ad.show()
                }

                override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                    super.onAdLoadFailed(ad, error)
                    setDebugState(Status.LoadFail, "Interstitial Ads Load Failed")

                    //Destroy loaded ad since it not required any more
                    destroyCurrentInterstitialAd()
                }

                override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                    super.onAdClosed(ad, finished)
                    setDebugState(Status.Closed, "Interstitial Ads Closed")

                    //Destroy loaded ad since it not required any more
                    destroyCurrentInterstitialAd()
                }
            })

            //Load InterstitialAd
            load(interstitialRequest)
        }
    }

    private fun destroyCurrentInterstitialAd() {
        interstitialAd?.apply {
            destroy()
            interstitialAd = null
        }
    }

    private fun showRewarded() {
        setDebugState(Status.Loading)

        //Destroy previous loaded RewardedAd
        destroyCurrentRewardedAd()

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
                    destroyCurrentRewardedAd()
                }

                override fun onAdClosed(ad: RewardedAd, finished: Boolean) {
                    setDebugState(Status.Closed, "Rewarded Ads Closed")

                    //Destroy loaded ad since it not required any more
                    destroyCurrentRewardedAd()
                }

                override fun onAdRewarded(ad: RewardedAd) {
                    setDebugState(Status.Rewarded, "Rewarded Ads Rewarded")

                    //Here you can start you reward process
                }
            })

            //Load RewardedAd
            load(rewardedRequest)
        }
    }

    private fun destroyCurrentRewardedAd() {
        rewardedAd?.apply {
            destroy()
            rewardedAd = null
        }
    }
}