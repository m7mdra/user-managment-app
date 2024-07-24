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
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Add user activity.
 */
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
                Snackbar.make(
                    binding.root,
                    "Failed to add a user, try again",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            AddUserState.Loading -> {
                progressDialog.show()

            }

            AddUserState.Success -> {
                progressDialog.dismiss()
                Snackbar.make(binding.root, "User added successfully", Snackbar.LENGTH_SHORT).show()
                Thread.sleep(500)
                setResult(RESULT_OK)
                finish()
            }

            AddUserState.AuthenticationError -> {
                progressDialog.dismiss()
                Snackbar.make(binding.root, "Authentication issue", Snackbar.LENGTH_SHORT).show()

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
            validateAndSubmit()

        }

    }
    //TODO: move validation to viewModel and make class the handle validation across all app
    /**
     * validate add user form and if it all succeed submit data to to [AddUserViewModel]
     */
    private fun validateAndSubmit() {
        val name = binding.nameEditText.text?.toString()
        val email = binding.emailEditText.text?.toString()
        val genderId = binding.materialButtonToggleGroup.checkedButtonId
        if (name.isNullOrEmpty()) {
            binding.nameEditText.error = "Enter A name"
            return
        }
        binding.nameEditText.error = null
        if (email == null || !email.isValidEmail()) {
            binding.emailEditText.error = "Invalid email address"
            return
        }
        binding.emailEditText.error = null

        if (genderId == -1) {
            Snackbar.make(binding.root, "Select a Gender", Snackbar.LENGTH_SHORT).show()
            return
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}