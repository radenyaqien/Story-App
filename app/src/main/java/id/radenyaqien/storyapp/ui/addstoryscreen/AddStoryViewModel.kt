package id.radenyaqien.storyapp.ui.addstoryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.radenyaqien.storyapp.domain.storiesusecase.AddStoriUsecase
import id.radenyaqien.storyapp.util.doIfFailure
import id.radenyaqien.storyapp.util.doIfLoading
import id.radenyaqien.storyapp.util.doIfSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val addStoriUsecase: AddStoriUsecase
) : ViewModel() {

    private val _state = MutableStateFlow(AddStoriState())
    val state = _state.asStateFlow()

    fun addStory(deskripsi: RequestBody, image: MultipartBody.Part) = viewModelScope.launch {
        addStoriUsecase(deskripsi, image)
            .onEach { res ->
                res.run {

                    doIfFailure { m, d ->
                        _state.update {
                            it.copy(errorMessage = message)
                        }
                    }
                    doIfSuccess {
                        _state.update {
                            it.copy(isSuccess = true)
                        }
                    }
                    doIfLoading {
                        _state.update {
                            it.copy(isLoading = true)
                        }
                    }
                }
            }.launchIn(this)
    }


}