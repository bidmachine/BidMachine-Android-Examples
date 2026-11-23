package io.bidmachine.examples.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

object Utils {

    @JvmStatic
    fun toast(context: Context, message: String?) {
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    fun addViewSafely(adContainer: ViewGroup, adView: View) {
        adContainer.removeAllViews()
        adView.removeFromParent()
        adContainer.addView(adView)
    }

}

private fun View.removeFromParent() {
    (parent as? ViewGroup)?.removeView(this)
}