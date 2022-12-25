package id.radenyaqien.storyapp.ui.mapscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.radenyaqien.storyapp.domain.model.Stories
import id.radenyaqien.storyapp.domain.storiesusecase.GetStoriesUsecase
import id.radenyaqien.storyapp.util.doIfFailure
import id.radenyaqien.storyapp.util.doIfLoading
import id.radenyaqien.storyapp.util.doIfSuccess
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val getStoriesUsecase: GetStoriesUsecase,

    ) : ViewModel() {

    private val _state = MutableStateFlow(MapsUistate())
    val state get() = _state.asStateFlow()


    fun getMapStorys() {
        getStoriesUsecase().onEach {

            it.doIfFailure { error, _ ->
                _state.update { uistate ->
                    uistate.copy(
                        isLoading = false,
                        msg = error
                    )
                }
            }
            it.doIfLoading {
                _state.update { uistate ->
                    uistate.copy(
                        isLoading = true
                    )
                }
            }
            it.doIfSuccess { stories ->
                _state.update { uistate ->
                    uistate.copy(
                        isLoading = false,
                        data = stories
                    )
                }
            }

        }.launchIn(
            viewModelScope
        )
    }



    data class MapsUistate(
        val msg: String? = null,
        val isLoading: Boolean = false,
        val data: List<Stories> = emptyList()
    )
}