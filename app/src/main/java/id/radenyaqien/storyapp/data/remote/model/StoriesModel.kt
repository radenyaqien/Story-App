package id.radenyaqien.storyapp.data.remote.model

import com.squareup.moshi.Json

data class StoriesModel(

	@Json(name="listStory")
	val listStory: List<ListStoryItem>,

	@Json(name="error")
	val error: Boolean,

	@Json(name="message")
	val message: String
)

data class ListStoryItem(

	@Json(name="photoUrl")
	val photoUrl: String,

	@Json(name="createdAt")
	val createdAt: String,

	@Json(name="name")
	val name: String,

	@Json(name="description")
	val description: String,

	@Json(name="lon")
	val lon: Double,

	@Json(name="id")
	val id: String,

	@Json(name="lat")
	val lat: Double
)
