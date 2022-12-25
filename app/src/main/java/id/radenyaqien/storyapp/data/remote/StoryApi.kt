package id.radenyaqien.storyapp.data.remote

import id.radenyaqien.storyapp.data.remote.model.AddStoryResponse
import id.radenyaqien.storyapp.data.remote.model.LoginResponse
import id.radenyaqien.storyapp.data.remote.model.RegisterResponse
import id.radenyaqien.storyapp.data.remote.model.StoriesModel
import id.radenyaqien.storyapp.util.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoryApi {

    @FormUrlEncoded
    @POST(Constant.REGISTER_ENDPOINT)
    suspend fun register(
        @FieldMap body: Map<String, String>
    ): RegisterResponse

    @FormUrlEncoded
    @POST(Constant.LOGIN_ENDPOINT)
    suspend fun login(
        @FieldMap body: Map<String, String>
    ): LoginResponse

    @Multipart
    @POST(Constant.STORIES_ENDPOINT)
    suspend fun addStories(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part?,
        @Part("description") deskripsi: RequestBody?,
    ): AddStoryResponse


    @GET(Constant.STORIES_ENDPOINT)
    suspend fun fetchStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): StoriesModel


}