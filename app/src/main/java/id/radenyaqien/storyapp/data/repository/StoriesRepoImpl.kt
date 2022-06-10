package id.radenyaqien.storyapp.data.repository

import id.radenyaqien.storyapp.data.datastore.UserPreff
import id.radenyaqien.storyapp.data.remote.RemoteDataSource
import id.radenyaqien.storyapp.domain.model.Stories
import id.radenyaqien.storyapp.domain.repository.StoryRepository
import id.radenyaqien.storyapp.util.MyResource
import id.radenyaqien.storyapp.util.map
import id.radenyaqien.storyapp.util.toStories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class StoriesRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userPreff: UserPreff

) : StoryRepository {
    override suspend fun getStories(): Flow<MyResource<List<Stories>>> {
        val token = userPreff.user.map {
            it.token
        }

        return remoteDataSource.fetchAllStory(token.first()).map {
            it.map { stories ->
                stories.toStories()
            }
        }

    }

    override suspend fun addStory(
        deskripsi: RequestBody,
        image: MultipartBody.Part
    ): Flow<MyResource<ResponseBody>> {
        val token = userPreff.user.first {
            it.token != ""
        }.token
        return remoteDataSource.addStories(token, deskripsi, image)
    }
}