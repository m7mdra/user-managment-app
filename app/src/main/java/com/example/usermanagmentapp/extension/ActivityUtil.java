package com.example.usermanagmentapp.extension;

import android.content.Context;

import androidx.annotation.Nullable;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public final class ActivityUtil {
    private ActivityUtil() {
    }

    static KProgressHUD createProgressDialog(Context context, @Nullable String message) {
        KProgressHUD kProgressHUD = KProgressHUD.create(context);
        if (message != null) {
            kProgressHUD.setLabel(message);
        }
        kProgressHUD
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.1f)
                .setCancellable(false)
                .setAnimationSpeed(2);
        return kProgressHUD;
    }
}
