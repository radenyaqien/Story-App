package id.radenyaqien.storyapp.data.remote

import id.radenyaqien.storyapp.data.remote.model.AddStoryResponse
import id.radenyaqien.storyapp.data.remote.model.RegisterResponse
import id.radenyaqien.storyapp.util.MyResource
import id.radenyaqien.storyapp.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class RemoteDataSource
@Inject constructor(
    val api: StoryApi
) {

    fun login(email: String, password: String) = safeApiCall {
        api.login(
            mapOf(
                "email" to email,
                "password" to password
            )
        )
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): Flow<MyResource<RegisterResponse>> = safeApiCall {
        api.register(
            mapOf(
                "email" to email,
                "password" to password,
                "name" to name
            )
        )
    }

    fun addStories(
        token: String,
        deskripsi: RequestBody?,
        image: File?
    ): Flow<MyResource<AddStoryResponse>> =
        safeApiCall {
            api.addStories(
                "Bearer $token",
                image?.asRequestBody()
                    ?.let { MultipartBody.Part.createFormData("photo", image.name, it) },
                deskripsi
            )
        }

    fun fetchAllStory(token: String) = safeApiCall {
        api.fetchStories("Bearer $token", location = 1)
    }


}
