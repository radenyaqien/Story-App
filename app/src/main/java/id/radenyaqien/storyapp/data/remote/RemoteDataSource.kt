package id.radenyaqien.storyapp.data.remote

import id.radenyaqien.storyapp.util.safeApiCall
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RemoteDataSource
@Inject constructor(
    private val api: StoryApi
) {

    fun login(email: String, password: String) = safeApiCall {
        api.login(
            mapOf(
                "email" to email,
                "password" to password
            )
        )
    }

    fun register(name: String, email: String, password: String) = safeApiCall {
        api.register(
            mapOf(
                "email" to email,
                "password" to password,
                "name" to name
            )
        )
    }

    fun addStories(token: String, deskripsi: RequestBody, image: MultipartBody.Part) =
        safeApiCall {
            api.addStories(token, image, deskripsi)
        }

    fun fetchAllStory(token: String) = safeApiCall {
        api.fetchStories(token)
    }
}
