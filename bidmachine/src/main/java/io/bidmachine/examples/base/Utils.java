package io.bidmachine.examples.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

public class Utils {

    public static void toast(@NonNull Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
