package com.example.usermanagmentapp.data.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Data class for User
 */

public class User {

    public static User testUser = new User(
            -1,
            "name",
            "mail@d.com",
            "gender",
            UserStatus.Active);


    @Nullable
    @SerializedName("email")
    public final String email;
    @Nullable
    @SerializedName("gender")
    public final String gender;
    @Nullable
    @SerializedName("id")
    public final Integer id;
    @Nullable
    @SerializedName("name")
    public final String name;
    @Nullable
    @SerializedName("status")
    public final UserStatus status;

    public User(
            @Nullable Integer id,
            @Nullable String name, @Nullable String email, @Nullable String gender, @Nullable UserStatus status) {
        this.email = email;
        this.gender = gender;
        this.id = id;
        this.name = name;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(gender, user.gender) && Objects.equals(id, user.id) && Objects.equals(name, user.name) && status == user.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, gender, id, name, status);
    }
}
