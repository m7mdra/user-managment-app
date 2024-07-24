package com.example.usermanagmentapp.extension

import android.util.Patterns

/**
 * Returns `true` if the string is a valid email address.
 */
fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()