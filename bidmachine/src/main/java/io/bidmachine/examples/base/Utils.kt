package io.bidmachine.examples.base

import android.content.Context
import android.widget.Toast

object Utils {

    @JvmStatic
    fun toast(context: Context, message: String?) {
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}