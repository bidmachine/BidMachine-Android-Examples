package io.bidmachine.examples;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import io.bidmachine.BidMachine;
import io.bidmachine.examples.base.BaseJavaExampleActivity;
import io.bidmachine.richmedia.RichMediaRequest;
import io.bidmachine.richmedia.RichMediaView;
import io.bidmachine.richmedia.SimpleRichMediaListener;
import io.bidmachine.utils.BMError;

public class RichMediaJavaActivity extends BaseJavaExampleActivity {

    private RichMediaView richMediaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialise SDK
        BidMachine.initialize(this, "5");

        //Enable logs
        BidMachine.setLoggingEnabled(true);

        //Set activity content view
        setContentView(R.layout.activity_rich_media);

        //Set listener to perform Ads load
        findViewById(R.id.bLoadAd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });
        findViewById(R.id.bResumeRichMedia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeRichMedia();
            }
        });
        findViewById(R.id.bPauseRichMedia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseRichMedia();
            }
        });
        findViewById(R.id.bMuteRichMedia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muteRichMedia();
            }
        });
        findViewById(R.id.bUnmuteRichMedia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unmuteRichMedia();
            }
        });

        //Find RichMediaView in hierarchy
        richMediaView = findViewById(R.id.richMediaView);

        //Set RichMedia Ads events listener
        richMediaView.setListener(new SimpleRichMediaListener() {
            @Override
            public void onAdLoaded(@NonNull RichMediaView ad) {
                setDebugState(Status.Loaded, "RichMedia Ads loaded");

                //Make RichMediaView visible
                ad.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdLoadFailed(@NonNull RichMediaView ad, @NonNull BMError error) {
                setDebugState(Status.LoadFail, "RichMedia Ads load failed");
            }
        });
    }

    private void loadAd() {
        setDebugState(Status.Loading);

        //Create RichMediaRequest
        RichMediaRequest request = new RichMediaRequest.Builder()
                .build();

        //Load RichMedia Ads
        richMediaView.load(request);
    }

    private void resumeRichMedia() {
        if (richMediaView != null) {
            richMediaView.resume();
        }
    }

    private void pauseRichMedia() {
        if (richMediaView != null) {
            richMediaView.pause();
        }
    }

    private void muteRichMedia() {
        if (richMediaView != null) {
            richMediaView.mute();
        }
    }

    private void unmuteRichMedia() {
        if (richMediaView != null) {
            richMediaView.unmute();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Destroy Ads when you finish with it
        if (richMediaView != null) {
            richMediaView.destroy();
            richMediaView = null;
        }
    }

}