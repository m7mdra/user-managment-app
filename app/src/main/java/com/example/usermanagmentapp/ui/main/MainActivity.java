package com.example.usermanagmentapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Observer;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usermanagmentapp.R;
import com.example.usermanagmentapp.data.model.User;
import com.example.usermanagmentapp.databinding.ActivityMainBinding;
import com.example.usermanagmentapp.ui.add.AddUserActivity;
import com.example.usermanagmentapp.ui.details.UserDetailsActivity;

import org.koin.java.KoinJavaComponent;

import kotlin.Lazy;

public class MainActivity extends AppCompatActivity implements UserAdapter.UserClickListener,
        MenuProvider {
    private final Lazy<UserViewModel> viewModelLazy = KoinJavaComponent.inject(UserViewModel.class);
    private final UserAdapter adapter = new UserAdapter(this);
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.topAppBar);
        addMenuProvider(this);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
            if (activityResult.getResultCode() == RESULT_OK) {
                adapter.refresh();
            }
        });

        RecyclerView recyclerView = binding.recyclerView;
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModelLazy.getValue().state.observe(this, new UsersStateObserver());
        viewModelLazy.getValue().pagesSubscribe();
    }

    @Override
    public void onClick(User user) {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra("user_id", user.id);
        startActivity(intent);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, AddUserActivity.class);
            activityResultLauncher.launch(intent);
            return true;
        }
        return false;
    }

    private class UsersStateObserver implements Observer<PagingData<User>> {
        @Override
        public void onChanged(PagingData<User> userPagingData) {
            adapter.submitData(getLifecycle(), userPagingData);
        }
    }
}
