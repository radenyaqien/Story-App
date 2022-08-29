package id.radenyaqien.storyapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.radenyaqien.storyapp.data.local.dao.RemoteKeysDao
import id.radenyaqien.storyapp.data.local.dao.StoryDao
import id.radenyaqien.storyapp.data.local.entity.RemoteKeys
import id.radenyaqien.storyapp.data.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeys::class], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun remotKeysDao(): RemoteKeysDao
}