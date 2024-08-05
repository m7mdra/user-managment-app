package com.example.usermanagmentapp.ui.details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.usermanagmentapp.data.model.User;
import com.example.usermanagmentapp.databinding.ActivityUserDetailsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserDetailsActivity extends AppCompatActivity {
    private Integer userId = -1;
    private ActivityUserDetailsBinding binding;
    private UserDetailsViewModel userDetailsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        userId = getIntent().getIntExtra("user_id", -1);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.materialToolbar);
        userDetailsViewModel = new ViewModelProvider(this).get(UserDetailsViewModel.class);
        userDetailsViewModel.state.observe(this, new UserDetailsStateObserver());
        userDetailsViewModel.load(userId);
    }

    private void showSuccessView() {
        binding.progressBar.setVisibility(View.GONE);
        binding.errorLayout.setVisibility(View.GONE);
        binding.tableLayout.setVisibility(View.VISIBLE);
    }

    private void showLoadingView() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.errorLayout.setVisibility(View.GONE);
        binding.tableLayout.setVisibility(View.GONE);

    }

    private void showErrorView(String message) {
        binding.errorLayout.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        binding.tableLayout.setVisibility(View.GONE);
        binding.retryButton.setOnClickListener(view -> userDetailsViewModel.load(userId));
        binding.errorTextView.setText(message);
    }

    private class UserDetailsStateObserver implements Observer<UserDetailsState> {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(UserDetailsState state) {
            if (state instanceof UserDetailsState.UserNotFound) {
                showErrorView("User with ID: $userId not found");
            }
            if (state instanceof UserDetailsState.Error) {
                showErrorView("Failed to load user data, try again");
            }
            if (state instanceof UserDetailsState.Loading) {
                showLoadingView();
            }
            if (state instanceof UserDetailsState.Success) {
                showSuccessView();
                User user = ((UserDetailsState.Success) state).user;
                if (user.id != null) {
                    binding.userIdTextView.setText(user.id.toString());
                }
                if (user.status != null) {
                    binding.statusTextView.setText(user.status.toString());
                }
                if (user.name != null) {
                    binding.userNameTextView.setText(user.name);
                }
                if (user.email != null) {
                    binding.emailTextView.setText(user.email);
                }

                if (user.gender != null) {
                    binding.genderTextView.setText(user.gender);
                }


            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
