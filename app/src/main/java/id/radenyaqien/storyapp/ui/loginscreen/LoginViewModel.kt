package id.radenyaqien.storyapp.ui.loginscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.radenyaqien.storyapp.domain.model.User
import id.radenyaqien.storyapp.domain.authusecase.LoginUsecase
import id.radenyaqien.storyapp.util.MyResource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUsecase: LoginUsecase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()


    fun login(username: String, password: String) = viewModelScope.launch {
        loginUsecase(username, password).onEach { res ->
            when (res) {
                is MyResource.Success -> {
                    _loginState.update {
                        LoginState(
                            User(
                                res.data?.loginResult?.userId ?: "",
                                res.data?.loginResult?.name ?: "",
                                res.data?.loginResult?.token ?: ""
                            )
                        )
                    }
                }
                is MyResource.Error -> {
                    _loginState.update {
                        LoginState(
                            error = res.message
                        )
                    }
                }
                is MyResource.Loading -> {
                    _loginState.update {
                        LoginState(
                            isloading = true
                        )
                    }
                }
            }
        }.launchIn(this)
    }

}