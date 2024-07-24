package com.example.usermanagmentapp.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.usermanagmentapp.extension.createProgressDialog
import com.example.usermanagmentapp.data.model.UserStatus
import com.example.usermanagmentapp.databinding.ActivityAddUserBinding
import com.example.usermanagmentapp.extension.isValidEmail
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddUserActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddUserBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<AddUserViewModel>()
    private val progressDialog by lazy {
        createProgressDialog()
    }
    private val addUserStateObserver = Observer<AddUserState> {
        when (it) {
            AddUserState.Error -> {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to add a user, try again", Toast.LENGTH_SHORT).show()
            }

            AddUserState.Loading -> {
                progressDialog.show()

            }

            AddUserState.Success -> {
                progressDialog.dismiss()
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }

            AddUserState.AuthenticationError -> {
                progressDialog.dismiss()
                Toast.makeText(this, "Authentication issue", Toast.LENGTH_SHORT).show()
            }

            is AddUserState.ValidationError -> {
                progressDialog.dismiss()

                MaterialAlertDialogBuilder(this)
                    .setTitle("Error send form")
                    .setMessage("The following errors returned upon submit:\n" +
                            it.errors.joinToString { "${it.field}: ${it.message}\n" })
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        viewModel.state.observe(this, addUserStateObserver)
        binding.submitButton.setOnClickListener {
            val name = binding.nameEditText.text?.toString()
            val email = binding.emailEditText.text?.toString()
            val genderId = binding.materialButtonToggleGroup.checkedButtonId
            if (name.isNullOrEmpty()) {
                binding.nameEditText.error = "Enter A name"
                return@setOnClickListener
            }
            binding.nameEditText.error = null
            if (email == null || !email.isValidEmail()) {
                binding.emailEditText.error = "Invalid email address"
                return@setOnClickListener
            }
            binding.emailEditText.error = null

            if (genderId == -1) {
                Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val gender = if (genderId == binding.maleButton.id) {
                "male"
            } else {
                "female"
            }
            val status = if (binding.statusSwitch.isEnabled) {
                UserStatus.Active
            } else {
                UserStatus.Inactive
            }
            viewModel.submit(name, email, gender, status)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}