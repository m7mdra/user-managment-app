package com.example.usermanagmentapp.ui.main

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.usermanagmentapp.databinding.RowUserBinding

class UserViewHolder(private val binding: RowUserBinding) : ViewHolder(binding.root) {
    val userTextView = binding.userTextView
    val imageView = binding.imageView
}