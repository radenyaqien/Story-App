package id.radenyaqien.storyapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity
data class StoryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double?,
    val lon: Double?
)