package io.bidmachine.examples.base;

import android.view.Menu;
import android.view.MenuItem;

public class BaseAndroidExampleActivity extends BaseExampleActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuItem menuItem = menu.add(0, R.id.menu_item_kotlin_example, 0, "GoKotlin!");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(R.drawable.ic_kotlin);
        return super.onCreateOptionsMenu(menu);
    }

}
