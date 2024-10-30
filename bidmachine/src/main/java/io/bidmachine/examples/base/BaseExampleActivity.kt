package io.bidmachine.examples.base

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.databinding.ActivityBaseBinding

abstract class BaseExampleActivity<Binding : ViewBinding> : AppCompatActivity() {

    companion object {
        const val SOURCE_ID = "122"

        private const val BM_KOTLIN = "bm-example-kotlin"
    }

    private lateinit var baseBinding: ActivityBaseBinding

    protected lateinit var binding: Binding

    protected abstract fun inflate(inflater: LayoutInflater): Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            BidMachine.setTestMode(true)
        }
        supportActionBar?.subtitle = "BidMachine Version - ${BidMachine.VERSION}"

        binding = inflate(layoutInflater)
        baseBinding = ActivityBaseBinding.inflate(layoutInflater).apply {
            setContentView(root)

            sTestMode.setOnCheckedChangeListener { _, isChecked ->
                BidMachine.setTestMode(isChecked)
            }
            adTypeContainer.addView(binding.root)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_kotlin_example -> {
                try {
                    val activityInfos = packageManager.getPackageInfo(packageName,
                                                                      PackageManager.GET_ACTIVITIES or PackageManager.GET_META_DATA)
                            .activities ?: return false
                    for (activityInfo in activityInfos) {
                        if (activityInfo.metaData != null && activityInfo.metaData.getBoolean(BM_KOTLIN)) {
                            val intent = Intent()
                            intent.setClassName(this, activityInfo.name)
                            startActivity(intent)
                            return true
                        }
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
                return false
            }
            R.id.menu_item_java_example -> {
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    protected fun toast(message: String?) {
        Utils.toast(this, message)
    }

    protected fun setDebugState(status: Status) {
        setDebugState(status, null)
    }

    protected fun setDebugState(status: Status, message: String?) {
        baseBinding.pbLoading.visibility = if (status.isLoading) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
        baseBinding.tvStatus.text = status.status

        toast(message)
    }


    protected enum class Status(val isLoading: Boolean, val status: String) {

        Requesting(true, "Requesting"),
        Requested(false, "Requested"),
        RequestFail(false, "Request Fail"),
        Loading(true, "Loading"),
        Loaded(false, "Loaded"),
        LoadFail(false, "Load Fail"),
        Closed(false, "Closed"),
        Rewarded(false, "Rewarded"),
        Expired(false, "Expired");

    }

}