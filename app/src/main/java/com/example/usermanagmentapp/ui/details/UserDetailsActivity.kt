package com.example.usermanagmentapp.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.example.usermanagmentapp.databinding.ActivityUserDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserDetailsActivity : AppCompatActivity() {
    private val userId: Int by lazy {
        intent.getIntExtra("user_id", -1)
    }
    private val binding by lazy {
        ActivityUserDetailsBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<UserDetailsViewModel>()
    private val userDetailsStateObserver = Observer<UserDetailsState> {
        if(it is UserDetailsState.UserNotFound){
            showErrorView("User with ID: $userId not found")
        }
        if(it is UserDetailsState.Error){
            showErrorView("Failed to load user data, try again")
        }
        if(it is UserDetailsState.Loading){
            showLoadingView()
        }
        if(it is UserDetailsState.Success){
            showSuccessView()
            val user = it.user
            binding.userIdTextView.text = user.id.toString()
            binding.userNameTextView.text = user.name
            binding.emailTextView.text = user.email
            binding.genderTextView.text = user.gender
            binding.statusTextView.text = user.status?.name.toString()
        }
    }

    private fun showSuccessView() {
        binding.progressBar.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.tableLayout.visibility = View.VISIBLE
    }

    private fun showLoadingView() {
        binding.progressBar.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
        binding.tableLayout.visibility = View.GONE

    }

    private fun showErrorView(message: String) {
        binding.errorLayout.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.tableLayout.visibility = View.GONE
        binding.retryButton.setOnClickListener {
            viewModel.loadData(userId)
        }
        binding.errorTextView.text = message
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        viewModel.state.observe(this, userDetailsStateObserver)
        viewModel.loadData(userId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}