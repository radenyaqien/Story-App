package id.radenyaqien.storyapp.ui.storyscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.radenyaqien.storyapp.domain.authusecase.GetCurrentUserUsecase
import id.radenyaqien.storyapp.domain.storiesusecase.GetStoriesUsecase
import id.radenyaqien.storyapp.util.doIfFailure
import id.radenyaqien.storyapp.util.doIfLoading
import id.radenyaqien.storyapp.util.doIfSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val usecase: GetStoriesUsecase,
    private val getCurrentUserUsecase: GetCurrentUserUsecase
) : ViewModel() {

    val currentUser = getCurrentUserUsecase()

    private val _datastate = MutableStateFlow(DataState())
    val datastate = _datastate.asStateFlow()

    fun stories() = viewModelScope.launch {
        usecase().onEach { resource ->
            resource.doIfFailure { error, data ->
                _datastate.update {
                    it.copy(error = error)
                }
            }

            resource.doIfLoading {
                _datastate.update {
                    it.copy(isloading = true)
                }
            }

            resource.doIfSuccess { data ->
                _datastate.update {
                    it.copy(isloading = false, user = data)
                }
            }
        }
    }

    init {
        stories()
    }
}