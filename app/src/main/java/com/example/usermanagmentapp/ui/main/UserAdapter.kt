package com.example.usermanagmentapp.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.usermanagmentapp.R
import com.example.usermanagmentapp.data.model.User
import com.example.usermanagmentapp.data.model.UserStatus
import com.example.usermanagmentapp.databinding.RowUserBinding

class UserAdapter(private val onClick: ((Int, User) -> Unit)? = null) :
    PagingDataAdapter<User, UserViewHolder>(USER_COMPARATOR) {
    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val from = LayoutInflater.from(parent.context)
        return UserViewHolder(RowUserBinding.inflate(from, parent, false))
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        Log.d("MEGA", "onBindViewHolder: $user")
        user?.let {
            val status = user.status
            holder.userTextView.text = user.name
            holder.imageView.setImageResource(imageFromStatus(status))

            holder.itemView.setOnClickListener {
                onClick?.invoke(position,user)
            }
        }
    }

    private fun imageFromStatus(status: UserStatus): Int {

        return when (status) {
            UserStatus.Active -> R.drawable.active_shape
            UserStatus.Inactive -> R.drawable.inactive_shape
        }

    }

}

