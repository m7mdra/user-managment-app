package com.example.usermanagmentapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Enum class for user status
 */
enum class UserStatus {
    @SerializedName("active")
    Active,

    @SerializedName("inactive")
    Inactive
}