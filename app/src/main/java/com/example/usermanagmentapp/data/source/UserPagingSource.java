package com.example.usermanagmentapp.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.usermanagmentapp.data.model.User;
import com.example.usermanagmentapp.data.repository.UserRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;

public class UserPagingSource extends RxPagingSource<Integer, User> {
    private final UserRepository repository;
    private final Scheduler ioScheduler;

    /**
     * A [PagingSource] that loads users from [UserRepository]
     *
     * @param repository  The repository to fetch users from.
     * @param ioScheduler The scheduler to use for IO operations.
     */
    public UserPagingSource(UserRepository repository, Scheduler ioScheduler) {
        this.repository = repository;
        this.ioScheduler = ioScheduler;
    }

    /**
     * Loads users from the network.
     *
     * @param loadParams The parameters for the load.
     */
    @NonNull
    @Override
    public Single<LoadResult<Integer, User>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        Integer page = loadParams.getKey();
        if (page == null) {
            page = 1;
        } else {
            page = loadParams.getKey();
        }


        Integer finalPage = page;
        return repository.users(page, loadParams.getLoadSize())
                .map((Function<List<User>, LoadResult<Integer, User>>) users -> {

                    Integer prevKey = finalPage == 1 ? null : finalPage - 1;
                    Integer nextKey = users.isEmpty() ? null : finalPage + 1;
                    return new LoadResult.Page<>(users, prevKey, nextKey);
                }).onErrorReturn(LoadResult.Error::new);
    }

    /**
     * Returns the key to be used for loading data.
     */
    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, User> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, User> anchorPage = pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;

    }
}
