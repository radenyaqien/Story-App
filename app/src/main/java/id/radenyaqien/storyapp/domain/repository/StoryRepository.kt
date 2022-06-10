package id.radenyaqien.storyapp.domain.repository

import id.radenyaqien.storyapp.domain.model.Stories
import id.radenyaqien.storyapp.util.MyResource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody


interface StoryRepository {

    suspend fun getStories(): Flow<MyResource<List<Stories>>>

    suspend fun addStory(
        deskripsi: RequestBody,
        image: MultipartBody.Part
    ): Flow<MyResource<ResponseBody>>

//    suspend fun getStory(id: String): Flow<MyResource<Stories>>
}