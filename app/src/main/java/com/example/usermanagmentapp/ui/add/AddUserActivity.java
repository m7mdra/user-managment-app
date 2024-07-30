package com.example.usermanagmentapp.ui.add;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.usermanagmentapp.data.model.FormError;
import com.example.usermanagmentapp.data.model.UserStatus;
import com.example.usermanagmentapp.databinding.ActivityAddUserBinding;
import com.example.usermanagmentapp.extension.ActivityUtil;
import com.example.usermanagmentapp.extension.StringUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.koin.java.KoinJavaComponent;

import java.util.List;
import java.util.stream.Collectors;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import kotlin.Lazy;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {
    private final Lazy<AddUserViewModel> viewModelLazy
            = KoinJavaComponent.inject(AddUserViewModel.class);
    private KProgressHUD progressDialog;

    private class AddUserStateObserver implements Observer<AddUserState> {

        @Override
        public void onChanged(AddUserState state) {
            if (state instanceof AddUserState.Loading) {
                progressDialog.show();
            }
            if (state instanceof AddUserState.Error) {
                progressDialog.dismiss();
                Snackbar.make(
                        binding.getRoot(),
                        "Failed to add a user, try again",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
            if (state instanceof AddUserState.Success) {
                progressDialog.dismiss();
                Snackbar.make(binding.getRoot(), "User added successfully", Snackbar.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
            if (state instanceof AddUserState.AuthenticationError) {
                progressDialog.dismiss();
                Snackbar.make(binding.getRoot(), "Authentication issue", Snackbar.LENGTH_SHORT).show();
            }
            if (state instanceof AddUserState.ValidationError) {
                progressDialog.dismiss();
                List<FormError> list = ((AddUserState.ValidationError) state).list;
                String errors = list.stream()
                        .map(formError -> formError.field + " " + formError.message + "\n")
                        .collect(Collectors.joining(", "));
                new MaterialAlertDialogBuilder(AddUserActivity.this)
                        .setTitle("Error send form")
                        .setMessage("The following errors returned upon submit: " + errors)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create()
                        .show();
            }
        }
    }

    ;
    private ActivityAddUserBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.materialToolbar);

        progressDialog = ActivityUtil.createProgressDialog(this, null);
        viewModelLazy.getValue().state.observe(this, new AddUserStateObserver());
        binding.submitButton.setOnClickListener(this);
    }

    private void validateAndSubmit() {
        @Nullable String name = binding.nameEditText.getText().toString();
        @Nullable String email = binding.emailEditText.getText().toString();
        int genderId = binding.materialButtonToggleGroup.getCheckedButtonId();
        if (name == null || name.isEmpty()) {
            binding.nameEditText.setError("Enter A name");
            return;
        }
        binding.nameEditText.setError(null);
        if (email == null || !StringUtil.isValidEmail(email)) {
            binding.emailEditText.setError("Invalid email address");
            return;
        }
        binding.emailEditText.setError(null);

        if (genderId == -1) {
            Snackbar.make(binding.getRoot(), "Select a Gender", Snackbar.LENGTH_SHORT).show();
            return;
        }
        String gender;
        if (genderId == binding.maleButton.getId()) {
            gender = "male";
        } else {
            gender = "female";
        }
        UserStatus status;
        if (binding.statusSwitch.isEnabled()) {
            status = UserStatus.Active;
        } else {
            status = UserStatus.Inactive;
        }
        viewModelLazy.getValue().submit(name, email, gender, status);
    }

    @Override
    public void onClick(View view) {
        validateAndSubmit();
    }
}
