package id.radenyaqien.storyapp.ui.addstoryscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.radenyaqien.storyapp.MainDispatcherRule
import id.radenyaqien.storyapp.data.remote.model.AddStoryResponse
import id.radenyaqien.storyapp.domain.storiesusecase.AddStoriUsecase
import id.radenyaqien.storyapp.util.MyResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import okhttp3.RequestBody
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddStoryViewModel

    @Mock
    private lateinit var requestBody: RequestBody

    @Mock
    private lateinit var addStoriUsecase: AddStoriUsecase


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Add Story Success`() = runTest {

        val expectedResponse: Flow<MyResource<AddStoryResponse>> = flow {
            emit(MyResource.Success(AddStoryResponse(false, "success")))
        }
        val uri = javaClass.classLoader?.getResource("marker1.jpg")
        val testFile = uri?.path?.let { File(it) }

        Mockito.`when`(
            addStoriUsecase.invoke(requestBody, testFile)
        ).thenReturn(expectedResponse)

        viewModel = AddStoryViewModel(addStoriUsecase)
        val state = viewModel.state
        viewModel.addStory(requestBody, testFile)
        Mockito.verify(addStoriUsecase)
            .invoke(requestBody, testFile)
        assertNotNull(state.value)
        assertNull(state.value.errorMessage)
        assertTrue(state.value.isSuccess)
    }

    @Test
    fun `when Add Story Failed`() = runTest {

        val expectedResponse: Flow<MyResource<AddStoryResponse>> = flow {
            emit(MyResource.Error("file not found"))
        }
        val uri = javaClass.classLoader?.getResource("marker.jpg")
        val testFile = uri?.path?.let { File(it) }

        Mockito.`when`(
            addStoriUsecase.invoke(requestBody, testFile)
        ).thenReturn(expectedResponse)

        viewModel = AddStoryViewModel(addStoriUsecase)
        val state = viewModel.state
        viewModel.addStory(requestBody, testFile)
        Mockito.verify(addStoriUsecase)
            .invoke(requestBody, testFile)
        assertNotNull(state.value)

        assertEquals("file not found", state.value.errorMessage)
        assertFalse(state.value.isSuccess)
    }


}