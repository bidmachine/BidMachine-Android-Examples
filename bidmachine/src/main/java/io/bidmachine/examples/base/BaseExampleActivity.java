package io.bidmachine.examples.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseExampleActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuItem menuItem = menu.add(0, R.id.menu_item_kotlin_example, 0, "GoKotlin!");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(R.drawable.ic_kotlin);
        return super.onCreateOptionsMenu(menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    protected void toast(@NonNull String message) {
        Utils.toast(this, message);
    }

}
