package id.radenyaqien.storyapp.data.repository

import id.radenyaqien.storyapp.data.datastore.UserPreff
import id.radenyaqien.storyapp.data.remote.RemoteDataSource
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
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

class StoriesRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userPreff: UserPreff
) : StoryRepository {
    override suspend fun getStories(token: String): Flow<MyResource<List<Stories>>> {

        return remoteDataSource.fetchAllStory(token).map {
            it.map { stories ->
                stories.toStories()
            }
        }

    }

    @OptIn(FlowPreview::class)
    override suspend fun addStory(
        deskripsi: RequestBody?,
        image: File?
    ): Flow<MyResource<ResponseBody>> {
        return userPreff.user.mapNotNull { it }.flatMapConcat {
            remoteDataSource.addStories(it.token, deskripsi, image)
        }
    }


}