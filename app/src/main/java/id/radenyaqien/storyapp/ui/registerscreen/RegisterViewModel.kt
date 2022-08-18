package id.radenyaqien.storyapp.ui.registerscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.radenyaqien.storyapp.domain.authusecase.RegisterUsecase
import id.radenyaqien.storyapp.util.doIfFailure
import id.radenyaqien.storyapp.util.doIfLoading
import id.radenyaqien.storyapp.util.doIfSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val usecase: RegisterUsecase
) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        usecase(name, email, password).onEach { resource ->
            resource.doIfFailure { error, _ ->
                setMessage(error)
            }
            resource.doIfLoading {
                setLoading()
            }
            resource.doIfSuccess {
                setData(it)
            }
        }.launchIn(this)
    }

    private fun setData(data: ResponseBody) {
        _registerState.update {
            it.copy(isloading = false, user = data)
        }
    }

    private fun setLoading() {
        _registerState.update {
            it.copy(isloading = true)
        }
    }


    fun setMessage(message: String?) {
        _registerState.update {
            it.copy(error = message, isloading = false)
        }
    }

}