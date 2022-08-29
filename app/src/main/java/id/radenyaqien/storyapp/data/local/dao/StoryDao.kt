package id.radenyaqien.storyapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.radenyaqien.storyapp.data.local.entity.StoryEntity


@Dao
interface StoryDao {

    @Query("SELECT * FROM StoryEntity")
    fun getStories(): PagingSource<Int, StoryEntity>

    @Query("Delete FROM StoryEntity")
    fun deleteAllStories()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: StoryEntity)

}