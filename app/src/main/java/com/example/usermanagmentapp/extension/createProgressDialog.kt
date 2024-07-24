package com.example.usermanagmentapp.extension

import android.app.Activity
import io.github.rupinderjeet.kprogresshud.KProgressHUD


/**
 * Create a progress dialog for the activity
 */
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