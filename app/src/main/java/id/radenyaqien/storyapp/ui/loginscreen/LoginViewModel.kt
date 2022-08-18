package id.radenyaqien.storyapp.ui.loginscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.radenyaqien.storyapp.domain.authusecase.GetCurrentUserUsecase
import id.radenyaqien.storyapp.domain.authusecase.LoginUsecase
import id.radenyaqien.storyapp.domain.model.User
import id.radenyaqien.storyapp.util.MyResource
import id.radenyaqien.storyapp.util.toUser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUsecase: LoginUsecase,
    getUser: GetCurrentUserUsecase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    init {
        getUser().onEach {
            setUserLogin(it)
        }.launchIn(viewModelScope)

    }

    fun login(username: String, password: String) = viewModelScope.launch {
        loginUsecase(username, password).onEach { res ->
            when (res) {
                is MyResource.Success -> {
                    setUserLogin(res.data?.loginResult?.toUser())
                }
                is MyResource.Error -> {
                    setMessage(res.message)
                }
                is MyResource.Loading -> {
                    setLoading()
                }
            }
        }.launchIn(this)
    }

    private fun setUserLogin(res: User?) {
        _loginState.update {
            it.copy(
                isloading = false,
                user = res
            )
        }
    }

    private fun setLoading() {
        _loginState.update {
            it.copy(
                isloading = true
            )
        }
    }

    fun setMessage(str: String?) {
        _loginState.update {
            it.copy(
                error = str,
                isloading = false
            )
        }
    }

}