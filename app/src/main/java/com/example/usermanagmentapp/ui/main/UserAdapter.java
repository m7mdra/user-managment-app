package com.example.usermanagmentapp.ui.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.usermanagmentapp.R;
import com.example.usermanagmentapp.data.model.User;
import com.example.usermanagmentapp.data.model.UserStatus;
import com.example.usermanagmentapp.databinding.RowUserBinding;

import java.util.Objects;
/**
 * Adapter for the list of users.
 */
public class UserAdapter extends PagingDataAdapter<User, UserViewHolder> {
    final UserClickListener clickListener;

    public UserAdapter(UserClickListener clickListener) {
        super(new DiffCallback());
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowUserBinding inflate = RowUserBinding.inflate(layoutInflater, parent, false);
        return new UserViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = getItem(position);
        if (user != null) {
            holder.imageView.setImageResource(imageFromStatus(user.status));
            holder.userTextView.setText(user.name);
            holder.itemView.setOnClickListener(v -> clickListener.onClick(user));
        }
    }

    private int imageFromStatus(UserStatus status) {
        if (status == UserStatus.Active) {
            return R.drawable.active_shape;
        } else {
            return R.drawable.inactive_shape;
        }
    }


    private static class DiffCallback extends DiffUtil.ItemCallback<User> {

        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return Objects.equals(oldItem.id, newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }
    }

    public interface UserClickListener {
        void onClick(User user);
    }
}

