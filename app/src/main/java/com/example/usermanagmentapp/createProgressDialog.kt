package com.example.usermanagmentapp

import android.app.Activity
import io.github.rupinderjeet.kprogresshud.KProgressHUD

fun Activity.createProgressDialog(message: String? = null): KProgressHUD {
    val create = KProgressHUD.create(this)
    if (message != null) {
        create.setLabel(message)
    }

    return create
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setDimAmount(0.1f)
        .setCancellable(false)
        .setAnimationSpeed(2)
}