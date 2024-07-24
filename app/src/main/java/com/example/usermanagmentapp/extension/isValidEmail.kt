package com.example.usermanagmentapp.extension

import android.util.Patterns

fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()