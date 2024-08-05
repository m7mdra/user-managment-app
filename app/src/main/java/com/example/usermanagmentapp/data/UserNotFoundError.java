package com.example.usermanagmentapp.data;
/**
 * A class represent user not found.
 */
public class UserNotFoundError extends Exception {
    final  int id;

    public UserNotFoundError(int id) {
        this.id = id;
    }
}
