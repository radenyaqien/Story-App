package id.radenyaqien.storyapp.ui.storyscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.radenyaqien.storyapp.domain.authusecase.GetCurrentUserUsecase
import id.radenyaqien.storyapp.domain.model.Stories
import id.radenyaqien.storyapp.domain.storiesusecase.GetStoriesUsecase
import id.radenyaqien.storyapp.util.doIfFailure
import id.radenyaqien.storyapp.util.doIfLoading
import id.radenyaqien.storyapp.util.doIfSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getStoriesUsecase: GetStoriesUsecase,
    getCurrentUserUsecase: GetCurrentUserUsecase
) : ViewModel() {


    private val _datastate = MutableStateFlow(HomeUiState())
    val datastate = _datastate.asStateFlow()

    private val userlogin = getCurrentUserUsecase.invoke()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun stories() = viewModelScope.launch {

        userlogin.mapNotNull { it?.token }.flatMapLatest { token ->
            getStoriesUsecase(token)
        }.onEach { resource ->
            resource.doIfFailure { error, _ ->
                setMessage(error)
            }
            resource.doIfLoading {
                setLoading()
            }
            resource.doIfSuccess { data ->
                setDataStories(data)
            }
        }.launchIn(this)

    }

    private fun setLoading() {
        _datastate.update {
            it.copy(isLoading = true)
        }
    }

    fun setMessage(str: String?) {
        _datastate.update {
            it.copy(
                isLoading = false,
                error = str
            )
        }
    }

    private fun setDataStories(data: List<Stories>) {
        _datastate.update {
            it.copy(isLoading = false, listData = data)
        }
    }

    init {
        stories()
    }
}