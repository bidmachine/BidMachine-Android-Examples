package io.bidmachine.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.examples.databinding.ActivityRichMediaBinding
import io.bidmachine.richmedia.RichMediaRequest
import io.bidmachine.richmedia.RichMediaView
import io.bidmachine.richmedia.SimpleRichMediaListener
import io.bidmachine.utils.BMError

class RichMediaKotlinActivity : BaseKotlinExampleActivity<ActivityRichMediaBinding>() {

    override fun inflate(inflater: LayoutInflater) = ActivityRichMediaBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise SDK
        BidMachine.initialize(this, "5")

        // Enable logs
        BidMachine.setLoggingEnabled(true)

        // Set listener to perform Ads load
        binding.bLoadAd.setOnClickListener {
            loadAd()
        }
        binding.bResumeRichMedia.setOnClickListener {
            resumeRichMedia()
        }
        binding.bPauseRichMedia.setOnClickListener {
            pauseRichMedia()
        }
        binding.bMuteRichMedia.setOnClickListener {
            muteRichMedia()
        }
        binding.bUnmuteRichMedia.setOnClickListener {
            unmuteRichMedia()
        }

        // Set RichMedia Ads events listener
        binding.richMediaView.setListener(object : SimpleRichMediaListener() {
            override fun onAdLoaded(ad: RichMediaView) {
                setDebugState(Status.Loaded, "RichMedia Ads Loaded")

                // Make RichMediaView visible
                ad.visibility = View.VISIBLE
            }

            override fun onAdLoadFailed(ad: RichMediaView, error: BMError) {
                setDebugState(Status.LoadFail, "RichMedia Ads Load Failed")
            }
        })
    }

    private fun loadAd() {
        setDebugState(Status.Loading)

        // Create RichMediaRequest
        val richMediaRequest = RichMediaRequest.Builder().build()

        // Load RichMedia Ads
        binding.richMediaView.load(richMediaRequest)
    }

    private fun resumeRichMedia() {
        binding.richMediaView.resume()
    }

    private fun pauseRichMedia() {
        binding.richMediaView.pause()
    }

    private fun muteRichMedia() {
        binding.richMediaView.mute()
    }

    private fun unmuteRichMedia() {
        binding.richMediaView.unmute()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Destroy Ads when you finish with it
        binding.richMediaView.destroy()
    }

}