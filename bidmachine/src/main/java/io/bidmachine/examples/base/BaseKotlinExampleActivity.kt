package io.bidmachine.examples.base

import android.view.Menu
import android.view.MenuItem
import androidx.viewbinding.ViewBinding

abstract class BaseKotlinExampleActivity<Binding : ViewBinding> : BaseExampleActivity<Binding>() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        menu.add(0, R.id.menu_item_java_example, 0, "GoAndroid!").apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            setIcon(R.drawable.ic_android)
        }
        return super.onCreateOptionsMenu(menu)
    }

}