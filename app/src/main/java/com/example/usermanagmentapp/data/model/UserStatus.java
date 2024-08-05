package com.example.usermanagmentapp.data.model;

import com.google.gson.annotations.SerializedName;

public enum UserStatus {
    @SerializedName("active")
    Active,

    @SerializedName("inactive")
    Inactive
}
