package id.radenyaqien.storyapp

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.radenyaqien.storyapp.domain.model.Stories
import kotlinx.coroutines.flow.Flow

class StoryPagingSourceTest : PagingSource<Int, Flow<List<Stories>>>() {
    companion object {
        fun snapshot(items: List<Stories>): PagingData<Stories> {
            return PagingData.from(items)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Flow<List<Stories>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    override fun getRefreshKey(state: PagingState<Int, Flow<List<Stories>>>): Int {
        return 0
    }
}
