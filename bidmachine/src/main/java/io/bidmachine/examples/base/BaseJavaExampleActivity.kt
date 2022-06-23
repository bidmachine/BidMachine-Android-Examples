package io.bidmachine.examples.base

import android.view.Menu
import android.view.MenuItem
import androidx.viewbinding.ViewBinding

abstract class BaseJavaExampleActivity<Binding : ViewBinding> : BaseExampleActivity<Binding>() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        menu.add(0, R.id.menu_item_kotlin_example, 0, "GoKotlin!").apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            setIcon(R.drawable.ic_kotlin)
        }
        return super.onCreateOptionsMenu(menu)
    }

}