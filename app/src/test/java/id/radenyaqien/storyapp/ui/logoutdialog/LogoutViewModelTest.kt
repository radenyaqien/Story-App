package id.radenyaqien.storyapp.ui.logoutdialog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.radenyaqien.storyapp.MainDispatcherRule
import id.radenyaqien.storyapp.data.repository.Dummy
import id.radenyaqien.storyapp.domain.authusecase.GetCurrentUserUsecase
import id.radenyaqien.storyapp.domain.authusecase.LogoutUsecase
import id.radenyaqien.storyapp.domain.model.User
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
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LogoutViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LogoutViewModel

    @Mock
    private lateinit var logoutUsecase: LogoutUsecase

    @Mock
    private lateinit var getCurrentUserUsecase: GetCurrentUserUsecase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Test
    fun getUser() = runTest {
        var user: User? = null
        val expectedResponse: Flow<User?> = flow {
            emit(Dummy.user())
        }

        `when`(
            getCurrentUserUsecase.invoke()
        ).thenReturn(expectedResponse)

        viewModel = LogoutViewModel(logoutUsecase, getCurrentUserUsecase)
        viewModel.user.collect {
            user = it
        }

        Mockito.verify(getCurrentUserUsecase)
            .invoke()
        assertNotNull(user)
        assertEquals(Dummy.user(), user)
    }

    @Test
    fun getNullUser() = runTest {
        var user: User? = null
        val expectedResponse: Flow<User?> = flow {
            emit(null)
        }

        `when`(
            getCurrentUserUsecase.invoke()
        ).thenReturn(expectedResponse)

        viewModel = LogoutViewModel(logoutUsecase, getCurrentUserUsecase)


        viewModel.user.collect {
            user = it
        }

        Mockito.verify(getCurrentUserUsecase)
            .invoke()
        assertNull(user)

    }

    @Test
    fun logout() = runTest {
        var user: User? = null
        val expectedResponse: Flow<User?> = flow {
            emit(null)
        }

       `when`(logoutUsecase.invoke()).thenReturn(Unit)

        `when`(
            getCurrentUserUsecase.invoke()
        ).thenReturn(expectedResponse)

        viewModel = LogoutViewModel(logoutUsecase, getCurrentUserUsecase)

        viewModel.logout()
        viewModel.user.collect {
            user = it
        }

        Mockito.verify(logoutUsecase)
            .invoke()
        assertNull(user)

    }
}