package com.example.usermanagmentapp.ui.main;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usermanagmentapp.databinding.RowUserBinding;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public final TextView userTextView;
    public final ImageView imageView;

    public UserViewHolder(@NonNull RowUserBinding binding) {
        super(binding.getRoot());
        userTextView = binding.userTextView;
        imageView = binding.imageView;
    }
}
