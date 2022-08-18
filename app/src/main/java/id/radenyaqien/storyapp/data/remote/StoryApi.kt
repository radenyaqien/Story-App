package id.radenyaqien.storyapp.data.remote

import id.radenyaqien.storyapp.data.remote.model.LoginResponse
import id.radenyaqien.storyapp.data.remote.model.StoriesModel
import id.radenyaqien.storyapp.util.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface StoryApi {

    @FormUrlEncoded
    @POST(Constant.REGISTER_ENDPOINT)
    suspend fun register(
        @FieldMap body: Map<String, String>
    ): ResponseBody

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
    ): ResponseBody


    @GET(Constant.STORIES_ENDPOINT)
    suspend fun fetchStories(
        @Header("Authorization") token: String
    ): StoriesModel
}