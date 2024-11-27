package io.bidmachine.examples.base

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import io.bidmachine.BidMachine
import io.bidmachine.examples.base.databinding.ActivityBaseBinding

abstract class BaseExampleActivity<Binding : ViewBinding> : AppCompatActivity() {

    private lateinit var baseBinding: ActivityBaseBinding

    protected lateinit var binding: Binding

    protected abstract fun inflate(inflater: LayoutInflater): Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            BidMachine.setTestMode(true)
        }

        binding = inflate(layoutInflater)
        baseBinding = ActivityBaseBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setSupportActionBar(toolbar)
            toolbar.subtitle = "BidMachine Version - ${BidMachine.VERSION}"
            sTestMode.setOnCheckedChangeListener { _, isChecked ->
                BidMachine.setTestMode(isChecked)
            }
            adTypeContainer.addView(binding.root)
        }
        applyInsets(this, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_kotlin_example -> {
                try {
                    val activityInfos = packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_ACTIVITIES or PackageManager.GET_META_DATA
                    )
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

    private fun applyInsets(activity: Activity, isRequestApplyInsets: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            val contentView = baseBinding.root.apply {
                setPadding(0, 0, 0, 0)
            }
            val view = activity.window?.decorView ?: contentView
            setInsetsChanger(view, isRequestApplyInsets) {
                baseBinding.toolbar.setPadding(it.left, it.top, it.right, 0)
                view.setPadding(it.left, 0, it.right, it.bottom)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun setInsetsChanger(view: View, isRequestApplyInsets: Boolean, action: (insets: Insets) -> Unit) {
        setInsetsChanger(
            view,
            isRequestApplyInsets,
            WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout(),
            action
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun setInsetsChanger(
        view: View,
        isRequestApplyInsets: Boolean,
        typeInsets: Int,
        action: (insets: Insets) -> Unit
    ) {
        view.setOnApplyWindowInsetsListener { _, windowInsets ->
            action(windowInsets.getInsets(typeInsets))
            windowInsets
        }
        if (isRequestApplyInsets) {
            view.requestApplyInsets()
        }
    }


    companion object {

        const val SOURCE_ID = "122"

        private const val BM_KOTLIN = "bm-example-kotlin"

    }

}