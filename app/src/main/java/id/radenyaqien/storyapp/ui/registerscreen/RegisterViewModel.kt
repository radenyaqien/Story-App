package id.radenyaqien.storyapp.ui.registerscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.radenyaqien.storyapp.domain.authusecase.RegisterUsecase
import id.radenyaqien.storyapp.util.MyResource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val usecase: RegisterUsecase
) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    suspend fun register(name: String, email: String, password: String) {
        usecase(name, email, password).onEach { resource ->
            when (resource) {
                is MyResource.Error -> _registerState.update {
                    it.copy(error = resource.message)
                }
                is MyResource.Loading -> _registerState.update {
                    it.copy(isloading = true)
                }
                is MyResource.Success -> _registerState.update {
                    it.copy(isloading = false, user = resource.data)
                }
            }
        }.launchIn(viewModelScope)
    }

}