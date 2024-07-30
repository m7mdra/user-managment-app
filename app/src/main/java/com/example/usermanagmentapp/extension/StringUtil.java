package com.example.usermanagmentapp.extension;

import android.util.Patterns;

import androidx.annotation.Nullable;

public final class StringUtil {
    private StringUtil() {
    }

    public static Boolean isValidEmail(@Nullable String email) {
        return email != null &&
                !email.isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
