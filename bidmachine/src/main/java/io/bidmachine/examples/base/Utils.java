package io.bidmachine.examples.base;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;

public class Utils {

    public static void toast(@NonNull Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
