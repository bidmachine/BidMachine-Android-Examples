package io.bidmachine.examples.base;

import android.view.Menu;
import android.view.MenuItem;

public class BaseKotlinExampleActivity extends BaseExampleActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuItem menuItem = menu.add(0, R.id.menu_item_java_example, 0, "GoAndroid!");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(android.R.drawable.sym_def_app_icon);
        return super.onCreateOptionsMenu(menu);
    }

}
