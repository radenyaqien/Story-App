package id.radenyaqien.storyapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import id.radenyaqien.storyapp.data.datastore.UserPreff
import id.radenyaqien.storyapp.data.local.AppDatabase
import id.radenyaqien.storyapp.data.local.entity.StoryEntity
import id.radenyaqien.storyapp.data.paging.StoryRemoteMediator
import id.radenyaqien.storyapp.data.remote.RemoteDataSource
import id.radenyaqien.storyapp.data.remote.model.AddStoryResponse
import id.radenyaqien.storyapp.domain.model.Stories
import id.radenyaqien.storyapp.domain.repository.StoryRepository
import id.radenyaqien.storyapp.util.MyResource
import id.radenyaqien.storyapp.util.map
import id.radenyaqien.storyapp.util.toStories
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class StoriesRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userPreff: UserPreff,
    private val appDatabase: AppDatabase
) : StoryRepository {

    override fun getStories(token: String): Flow<MyResource<List<Stories>>> {

        return remoteDataSource.fetchAllStory(token).map {
            it.map { stories ->
                stories.toStories()
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getStoriesPagingData(token: String): Flow<PagingData<StoryEntity>> {
        val pagingSourceFactory = { appDatabase.storyDao().getStories() }
        val pager = Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = StoryRemoteMediator(remoteDataSource.api, appDatabase, token)
        )
        return pager.flow
    }

    @OptIn(FlowPreview::class)
    override fun addStory(
        deskripsi: RequestBody?,
        image: File?
    ): Flow<MyResource<AddStoryResponse>> = userPreff.user.mapNotNull { it }.flatMapConcat {
        remoteDataSource.addStories(it.token, deskripsi, image)
    }


    companion object {
        private const val ITEMS_PER_PAGE: Int = 10
    }


}