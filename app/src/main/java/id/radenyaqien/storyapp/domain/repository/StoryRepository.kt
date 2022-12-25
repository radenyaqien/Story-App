package id.radenyaqien.storyapp.domain.repository

import androidx.paging.PagingData
import id.radenyaqien.storyapp.data.local.entity.StoryEntity
import id.radenyaqien.storyapp.data.remote.model.AddStoryResponse
import id.radenyaqien.storyapp.domain.model.Stories
import id.radenyaqien.storyapp.util.MyResource
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import java.io.File


interface StoryRepository {

    fun getStories(token: String): Flow<MyResource<List<Stories>>>

    fun getStoriesPagingData(token: String): Flow<PagingData<StoryEntity>>
    fun addStory(
        deskripsi: RequestBody?,
        image: File?
    ): Flow<MyResource<AddStoryResponse>>

}