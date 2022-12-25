package id.radenyaqien.storyapp.ui.storyscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import id.radenyaqien.storyapp.MainDispatcherRule
import id.radenyaqien.storyapp.StoryPagingSourceTest
import id.radenyaqien.storyapp.data.repository.Dummy
import id.radenyaqien.storyapp.domain.model.Stories
import id.radenyaqien.storyapp.domain.storiesusecase.GetStoriesPagingUsecase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val dummyStories = Dummy.generateDummyStories()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var getStoriesPagingUsecase: GetStoriesPagingUsecase


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()



    @Test
    fun getDataPaging() = runTest {
        val snapshotData = StoryPagingSourceTest.snapshot(dummyStories)
        val expectedResponse: Flow<PagingData<Stories>> = flow {
            emit(snapshotData)
        }

        Mockito.`when`(getStoriesPagingUsecase.invoke()).thenReturn(expectedResponse)
        viewModel = HomeViewModel(getStoriesPagingUsecase)
        val differ = AsyncPagingDataDiffer(
            diffCallback = diffCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher
        )

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.data.collect {
                differ.submitData(it)
            }
        }

        verify(getStoriesPagingUsecase).invoke()
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories, differ.snapshot())
        collectJob.cancel()

    }


    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Stories>() {
        override fun areItemsTheSame(oldItem: Stories, newItem: Stories): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Stories, newItem: Stories): Boolean {
            return oldItem.id == newItem.id
        }
    }


}