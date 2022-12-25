package id.radenyaqien.storyapp.ui.mapscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.radenyaqien.storyapp.MainDispatcherRule
import id.radenyaqien.storyapp.data.repository.Dummy
import id.radenyaqien.storyapp.domain.model.Stories
import id.radenyaqien.storyapp.domain.storiesusecase.GetStoriesUsecase
import id.radenyaqien.storyapp.util.MyResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {


    private lateinit var viewModel: MapsViewModel
    private val dummyStories = Dummy.generateDummyStories()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var getStories: GetStoriesUsecase


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Test
    fun `when get Data Success`() = runTest {

        val expectedResponse: Flow<MyResource<List<Stories>>> = flow {
            emit(MyResource.Success(dummyStories))
        }
        Mockito.`when`(
            getStories.invoke()
        ).thenReturn(expectedResponse)
        viewModel = MapsViewModel(getStories)
        val state = viewModel.state
        viewModel.getMapStorys()

        Mockito.verify(getStories)
            .invoke()
        assertTrue(state.value.data.isNotEmpty())
        assertEquals(dummyStories, state.value.data)
        assertEquals(dummyStories.size, state.value.data.size)
        assertNull(state.value.msg)

    }


    @Test
    fun `when data kosong`() = runTest {

        val expectedResponse: Flow<MyResource<List<Stories>>> = flow {
            emit(MyResource.Error("No data"))
        }

        Mockito.`when`(
            getStories()
        ).thenReturn(expectedResponse)
        viewModel = MapsViewModel(getStories)
        val state = viewModel.state
        viewModel.getMapStorys()
        Mockito.verify(getStories)
            .invoke()
        assertTrue(state.value.data.isEmpty())
        assertEquals("No data", state.value.msg)
    }
}