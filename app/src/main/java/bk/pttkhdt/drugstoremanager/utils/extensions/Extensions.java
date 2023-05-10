package bk.pttkhdt.drugstoremanager.utils.extensions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

public class Extensions {


    public static void showToastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void show(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void hide(View view) {
        view.setVisibility(View.GONE);
    }

    public static Drawable getDrawableCompat(Context context, @DrawableRes int id) {
        return ContextCompat.getDrawable(context, id);
    }

    public static int getColorCompat(Context context, @ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }



}
