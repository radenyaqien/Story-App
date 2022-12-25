package id.radenyaqien.storyapp.data.remote.model


import com.squareup.moshi.Json

data class AddStoryResponse(
    @Json(name = "error")
    val error: Boolean,
    @Json(name = "message")
    val message: String
)