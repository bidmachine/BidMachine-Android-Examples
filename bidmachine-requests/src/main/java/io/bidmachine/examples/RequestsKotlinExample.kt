package io.bidmachine.examples

import android.os.Bundle
import android.view.View
import io.bidmachine.BidMachine
import io.bidmachine.banner.BannerRequest
import io.bidmachine.banner.BannerSize
import io.bidmachine.banner.BannerView
import io.bidmachine.banner.SimpleBannerListener
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.interstitial.InterstitialAd
import io.bidmachine.interstitial.InterstitialRequest
import io.bidmachine.interstitial.SimpleInterstitialListener
import io.bidmachine.models.AuctionResult
import io.bidmachine.utils.BMError
import kotlinx.android.synthetic.main.activity_requests.*

class RequestsKotlinExample : BaseKotlinExampleActivity() {

    private var bannerRequest: BannerRequest? = null
    private var interstitialAd: InterstitialAd? = null
    private var interstitialRequest: InterstitialRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialise SDK
        BidMachine.initialize(this, "1")

        //Enable logs
        BidMachine.setLoggingEnabled(true)

        //Set activity content view
        setContentView(R.layout.activity_requests)

        //Set listener to perform Ads request
        btnRequestBanner.setOnClickListener {
            requestBanner()
        }

        //Set listener to perform Ads show
        btnShowRequestedBanner.setOnClickListener {
            showRequestedBanner()
        }

        //Set listener to perform Interstitial Ads request
        btnRequestInterstitial.setOnClickListener { requestInterstitial() }

        //Set listener to perform Interstitial Ads show
        btnShowRequestedInterstitial.setOnClickListener { showRequestedInterstitial() }

        //Set listener ot change BannerView visibility when Ads loaded
        bannerView.setListener(object : SimpleBannerListener() {
            override fun onAdLoaded(ad: BannerView) {
                setDebugState(Status.Loaded, "Banner Ads Loaded")

                //Make BannerView visible
                ad.visibility = View.VISIBLE
            }

            override fun onAdLoadFailed(ad: BannerView, error: BMError) {
                setDebugState(Status.LoadFail, "Banner Ads load failed: ${error.message}")
            }
        })
    }

    /**
     * Make note, that AdRequest listeners will be notified in background thread
     */
    private fun requestBanner() {
        setDebugState(Status.Requesting)

        //Create new Banner Ads request
        bannerRequest = BannerRequest.Builder()
                .setSize(BannerSize.Size_320x50)
                //Set Banner Ads request listener
                .setListener(object : BannerRequest.AdRequestListener {
                    override fun onRequestSuccess(request: BannerRequest, p1: AuctionResult) {
                        runOnUiThread {
                            setDebugState(Status.Requested, "Banner Ad Requested")
                        }
                    }

                    override fun onRequestFailed(request: BannerRequest, error: BMError) {
                        runOnUiThread {
                            setDebugState(Status.RequestFail,
                                    "Banner Request Failed: ${error.message}")
                        }
                    }

                    override fun onRequestExpired(request: BannerRequest) {
                        runOnUiThread {
                            setDebugState(Status.Expired, "Banner Request Expired")
                        }
                    }
                }).build()

        //Perform Banner Ads request
        bannerRequest?.request(this)
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
                //Perform BannerAd load
                bannerView.load(bannerRequest)
            }
        }
    }

    private fun requestInterstitial() {
        setDebugState(Status.Requesting)

        //Create new Interstitial Ads request
        interstitialRequest = InterstitialRequest.Builder()
                //Set Banner Ads request listener
                .setListener(object : InterstitialRequest.AdRequestListener {
                    override fun onRequestSuccess(interstitialRequest: InterstitialRequest,
                                                  auctionResult: AuctionResult) {
                        runOnUiThread {
                            setDebugState(Status.Requested, "Interstitial Ad Requested")
                        }
                    }

                    override fun onRequestFailed(interstitialRequest: InterstitialRequest,
                                                 bmError: BMError) {
                        runOnUiThread {
                            setDebugState(Status.RequestFail,
                                    "Interstitial Request Failed: " + bmError.message)
                        }
                    }

                    override fun onRequestExpired(interstitialRequest: InterstitialRequest) {
                        runOnUiThread {
                            setDebugState(Status.Expired, "Interstitial Request Expired")
                        }
                    }
                }).build()

        //Perform Banner Ads request
        interstitialRequest!!.request(this)
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
                //Destroy previous InterstitialAd object
                destroyCurrentInterstitialAd()

                //Create new InterstitialAd
                interstitialAd = InterstitialAd(this).apply {
                    setListener(object : SimpleInterstitialListener() {
                        override fun onAdLoaded(ad: InterstitialAd) {
                            setDebugState(Status.Loaded, "Interstitial Ads Loaded")

                            //Show interstitial Ads
                            ad.show()
                        }

                        override fun onAdLoadFailed(ad: InterstitialAd, error: BMError) {
                            setDebugState(Status.LoadFail, "Banner Ads load failed: " + error.message)
                        }

                        override fun onAdClosed(ad: InterstitialAd, finished: Boolean) {
                            setDebugState(Status.Closed)

                            //Destroy current interstitial ad since we don't need it anymore
                            destroyCurrentInterstitialAd()
                        }
                    })

                    //Perform InterstitialAd load
                    load(interstitialRequest)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        //Destroy Ads when you finish with it
        bannerView?.destroy()
        destroyCurrentInterstitialAd()
    }

    private fun destroyCurrentInterstitialAd() {
        interstitialAd?.apply {
            destroy()
            interstitialAd = null
        }
    }

}
