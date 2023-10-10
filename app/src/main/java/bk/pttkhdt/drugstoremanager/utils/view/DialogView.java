package bk.pttkhdt.drugstoremanager.utils.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import bk.pttkhdt.drugstoremanager.databinding.DialogConfirmWithDescriptionBinding;

public class DialogView {

    public static void showDialogDescriptionByHtml(Activity activity, String title, String description, final Runnable listenerPositive) {
        try {
            DialogConfirmWithDescriptionBinding binding = DialogConfirmWithDescriptionBinding.inflate(LayoutInflater.from(activity));
            Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            binding.btnAccept.setOnClickListener(v -> {
                listenerPositive.run();
                dialog.dismiss();
            });

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

            binding.tvTitle.setText(title);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.tvDescription.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY));
            } else {
                binding.tvDescription.setText(Html.fromHtml(description));
            }

            dialog.show();
        } catch (Exception exception) {

        }
    }
}
