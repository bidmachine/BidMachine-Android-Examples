package io.bidmachine.examples.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import io.bidmachine.BidMachine;

public class BaseExampleActivity extends AppCompatActivity {

    private FrameLayout adTypeContainer;
    private ProgressBar loadingProgressBar;
    private TextView currentAdStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        adTypeContainer = findViewById(R.id.adTypecontainer);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        currentAdStatus = findViewById(R.id.currentAdStatus);

        ((Switch) findViewById(R.id.testModeSwitcher)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BidMachine.setTestMode(isChecked);
            }
        });

        BidMachine.setTestMode(true);
    }

    @Override
    public void setContentView(int layoutResID) {
        if (adTypeContainer != null) {
            View view = LayoutInflater
                    .from(this)
                    .inflate(layoutResID, adTypeContainer, false);
            adTypeContainer.addView(view);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_kotlin_example) {
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),
                        PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA);
                for (ActivityInfo activityInfo : packageInfo.activities) {
                    if (activityInfo.metaData != null && activityInfo.metaData.getBoolean("bm-example-kotlin")) {
                        Intent intent = new Intent();
                        intent.setClassName(this, activityInfo.name);
                        startActivity(intent);
                        return true;
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return false;
        } else if (item.getItemId() == R.id.menu_item_java_example) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void toast(@NonNull String message) {
        Utils.toast(this, message);
    }

    protected void setDebugState(Status status) {
        setDebugState(status, null);
    }

    protected void setDebugState(Status status, String message) {
        if (loadingProgressBar != null) {
            loadingProgressBar.setVisibility(status.isLoading() ? View.VISIBLE : View.INVISIBLE);
        }
        if (currentAdStatus != null) {
            currentAdStatus.setText(status.getStatus());
        }
        if (TextUtils.isEmpty(message)) {
            toast("Banner Ads loaded");
        }
    }

    protected enum Status {
        Loading(true, "Loading"),
        Loaded(false, "Loaded"),
        LoadFail(false, "Load Fail"),
        Closed(false, "Closed"),
        Rewarded(false, "Rewarded");

        private boolean loading;
        private String status;

        Status(boolean loading, String status) {
            this.loading = loading;
            this.status = status;
        }

        public boolean isLoading() {
            return loading;
        }

        public String getStatus() {
            return status;
        }
    }

}
