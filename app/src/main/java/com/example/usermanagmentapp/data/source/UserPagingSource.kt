package com.example.usermanagmentapp.data.source

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.usermanagmentapp.data.model.User
import com.example.usermanagmentapp.data.repository.UserRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

class UserPagingSource(private val repository: UserRepository, private val ioScheduler: Scheduler) :
    RxPagingSource<Int, User>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, User>> {
        val page = params.key ?: 1
        return repository.users(page, params.loadSize)
            .subscribeOn(ioScheduler)
            .map<LoadResult<Int, User>> { users ->
                LoadResult.Page(
                    data = users,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (users.isEmpty()) null else page + 1
                )
            }
            .onErrorReturn { LoadResult.Error(it) }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}