package id.radenyaqien.storyapp.ui.loginscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.radenyaqien.storyapp.MainDispatcherRule
import id.radenyaqien.storyapp.data.remote.model.LoginResponse
import id.radenyaqien.storyapp.data.repository.Dummy
import id.radenyaqien.storyapp.domain.authusecase.GetCurrentUserUsecase
import id.radenyaqien.storyapp.domain.authusecase.LoginUsecase
import id.radenyaqien.storyapp.domain.model.User
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
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    @Mock
    private lateinit var loginUsecase: LoginUsecase

    @Mock
    private lateinit var getCurrentUserUsecase: GetCurrentUserUsecase

    private val name = "test"
    private val password = "12345678"

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when User Is Not Loggedin`() = runTest {

        val expectedResponse: Flow<User?> = flow {
            emit(null)
        }

        Mockito.`when`(
            getCurrentUserUsecase.invoke()
        ).thenReturn(expectedResponse)

        viewModel = LoginViewModel(loginUsecase, getCurrentUserUsecase)
        val state = viewModel.loginState
        Mockito.verify(getCurrentUserUsecase)
            .invoke()
        assertNull(state.value.user)
        assertFalse(state.value.isLoggedIn)

    }

    @Test
    fun `when User Is Loggedin`() = runTest {

        val expectedResponse: Flow<User?> = flow {
            emit(Dummy.user())
        }

        Mockito.`when`(
            getCurrentUserUsecase.invoke()
        ).thenReturn(expectedResponse)

        viewModel = LoginViewModel(loginUsecase, getCurrentUserUsecase)
        val state = viewModel.loginState
        Mockito.verify(getCurrentUserUsecase)
            .invoke()
          assertNotNull(state.value.user)
        assertEquals(Dummy.user(), state.value.user)
        assertTrue(state.value.isLoggedIn)

    }

    @Test
    fun `when Login Success`() = runTest {

        val expectedResponse: Flow<MyResource<LoginResponse>> = flow {
            emit(MyResource.Success(Dummy.loginSuccessResponse()))
        }

        Mockito.`when`(
            loginUsecase.invoke(
                username = name,
                password = password
            )
        ).thenReturn(expectedResponse)
        viewModel = LoginViewModel(loginUsecase, getCurrentUserUsecase)
        val state = viewModel.loginState
        viewModel.login(
            username = name,
            password = password,
        )

        Mockito.verify(loginUsecase)
            .invoke(username = name, password = password)
        assertNotNull(state.value.user)
        assertEquals(name, state.value.user?.nama)
        assertEquals(Dummy.user(), state.value.user)
        assertTrue(state.value.isLoggedIn)
    }

    @Test
    fun `when Login Gagal`() = runTest {

        val expectedResponse: Flow<MyResource<LoginResponse>> = flow {
            emit(MyResource.Error(Dummy.loginErrorResponse().message))
        }

        Mockito.`when`(
            loginUsecase.invoke(
                username = name,
                password = password
            )
        ).thenReturn(expectedResponse)
        viewModel = LoginViewModel(loginUsecase, getCurrentUserUsecase)
        val state = viewModel.loginState
        viewModel.login(
            username = name,
            password = password,
        )

        Mockito.verify(loginUsecase)
            .invoke(username = name, password = password)
        assertNull(state.value.user)
        assertEquals("User not found", state.value.error)
        assertFalse(state.value.isLoggedIn)

    }
}