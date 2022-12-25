package id.radenyaqien.storyapp.ui.registerscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.radenyaqien.storyapp.MainDispatcherRule
import id.radenyaqien.storyapp.data.remote.model.RegisterResponse
import id.radenyaqien.storyapp.data.repository.Dummy
import id.radenyaqien.storyapp.domain.authusecase.RegisterUsecase
import id.radenyaqien.storyapp.util.MyResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var registerViewModel: RegisterViewModel

    @Mock
    private lateinit var registerUsecase: RegisterUsecase

    private val name = "test"
    private val email = "test@email.com"
    private val password = "12345678"

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(
            registerUsecase
        )
    }


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Test
    fun `when Register Success`() = runTest {
        val state = registerViewModel.registerState
        val expectedResponse: Flow<MyResource<RegisterResponse>> = flow {
            emit(MyResource.Success(Dummy.registerSuccessResponse()))
        }

        Mockito.`when`(
            registerUsecase.invoke(
                name = name,
                email = email,
                password = password
            )
        ).thenReturn(expectedResponse)

        registerViewModel.register(
            name = name,
            email = email,
            password = password,
        )

        Mockito.verify(registerUsecase)
            .invoke(name = name, email = email, password = password)
        assertNotNull(state.value.msg)
        assertEquals("UserCreated", state.value.msg)
        assertTrue(state.value.isRegisterSuccess)
    }

    @Test
    fun `when Register Gagal`() = runTest {
        val state = registerViewModel.registerState
        val expectedResponse: Flow<MyResource<RegisterResponse>> = flow {
            emit(MyResource.Error(Dummy.registerErrorResponse().message))
        }

        Mockito.`when`(
            registerUsecase.invoke(
                name = name,
                email = email,
                password = password
            )
        ).thenReturn(expectedResponse)

        registerViewModel.register(
            name = name,
            email = email,
            password = password,
        )

        Mockito.verify(registerUsecase)
            .invoke(name = name, email = email, password = password)
        assertNotNull(state.value.msg)
        assertEquals("unexpected error Occured", state.value.msg)
        assertEquals(false, state.value.isRegisterSuccess)
    }
}