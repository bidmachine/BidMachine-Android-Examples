package io.bidmachine.examples

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.BaseKotlinExampleActivity
import io.bidmachine.richmedia.RichMediaRequest
import io.bidmachine.richmedia.RichMediaView
import io.bidmachine.richmedia.SimpleRichMediaListener
import io.bidmachine.utils.BMError

class RichMediaKotlinActivity : BaseKotlinExampleActivity() {

    private lateinit var richMediaView: RichMediaView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialise SDK
        BidMachine.initialize(this, "5")

        //Enable logs
        BidMachine.setLoggingEnabled(true)

        //Set activity content view
        setContentView(R.layout.activity_rich_media)

        //Set listener to perform Ads load
        findViewById<Button>(R.id.bLoadAd).setOnClickListener {
            loadAd()
        }
        findViewById<ImageButton>(R.id.bResumeRichMedia).setOnClickListener {
            resumeRichMedia()
        }
        findViewById<ImageButton>(R.id.bPauseRichMedia).setOnClickListener {
            pauseRichMedia()
        }
        findViewById<ImageButton>(R.id.bMuteRichMedia).setOnClickListener {
            muteRichMedia()
        }
        findViewById<ImageButton>(R.id.bUnmuteRichMedia).setOnClickListener {
            unmuteRichMedia()
        }

        //Find RichMediaView in hierarchy
        richMediaView = findViewById(R.id.richMediaView)

        //Set RichMedia Ads events listener
        richMediaView.setListener(object : SimpleRichMediaListener() {
            override fun onAdLoaded(ad: RichMediaView) {
                setDebugState(Status.Loaded, "RichMedia Ads Loaded")

                //Make RichMediaView visible
                ad.visibility = View.VISIBLE
            }

            override fun onAdLoadFailed(ad: RichMediaView, error: BMError) {
                setDebugState(Status.LoadFail, "RichMedia Ads Load Failed")
            }
        })
    }

    private fun loadAd() {
        setDebugState(Status.Loading)

        //Create RichMediaRequest
        val richMediaRequest = RichMediaRequest.Builder()
            .build()

        //Load RichMedia Ads
        richMediaView.load(richMediaRequest)
    }

    private fun resumeRichMedia() {
        richMediaView.resume()
    }

    private fun pauseRichMedia() {
        richMediaView.pause()
    }

    private fun muteRichMedia() {
        richMediaView.mute()
    }

    private fun unmuteRichMedia() {
        richMediaView.unmute()
    }

    override fun onDestroy() {
        super.onDestroy()

        //Destroy Ads when you finish with it
        richMediaView.destroy()
    }

}