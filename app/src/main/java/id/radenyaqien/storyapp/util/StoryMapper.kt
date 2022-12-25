package id.radenyaqien.storyapp.util

import id.radenyaqien.storyapp.data.local.entity.StoryEntity
import id.radenyaqien.storyapp.data.remote.model.StoriesModel
import id.radenyaqien.storyapp.domain.model.Stories
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val formatter = SimpleDateFormat("EEE, MMM d, yyyy", Locale.US)
fun StoriesModel.toStories(): List<Stories> {
    return this.listStory.map {
        Stories(
            photoUrl = it.photoUrl,
            id = it.id,
            name = it.name,
            description = it.description,
            createdAt = it.createdAt,
            lat = it.lat,
            lon = it.lon
        )
    }
}

fun parsing(source: String): Date? {

    return try {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.parse(source)
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}

fun StoriesModel.toStoryEntity(): List<StoryEntity> = listStory.map {
    StoryEntity(
        photoUrl = it.photoUrl,
        id = it.id,
        name = it.name,
        description = it.description,
        createdAt = it.createdAt,
        lat = it.lat,
        lon = it.lon
    )

}

fun StoryEntity.asStory(): Stories = Stories(
    photoUrl = photoUrl,
    id = id,
    name = name,
    description = description,
    createdAt = formatter.format(
        parsing(createdAt) ?: Date()
    ),
    lat = lat,
    lon = lon
)