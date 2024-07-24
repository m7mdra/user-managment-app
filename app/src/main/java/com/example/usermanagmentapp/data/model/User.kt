package com.example.usermanagmentapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    @SerializedName("email")
    val email: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("name")
    val name: String?,
    @SerializedName("status")
    val status: UserStatus?

) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (email != other.email) return false
        if (gender != other.gender) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + id
        result = 31 * result + name.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }
}

enum class UserStatus {
    @SerializedName("active")
    Active,

    @SerializedName("inactive")
    Inactive
}