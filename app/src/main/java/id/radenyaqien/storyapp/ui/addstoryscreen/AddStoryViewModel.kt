package id.radenyaqien.storyapp.ui.addstoryscreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.radenyaqien.storyapp.domain.storiesusecase.AddStoriUsecase
import id.radenyaqien.storyapp.util.doIfFailure
import id.radenyaqien.storyapp.util.doIfLoading
import id.radenyaqien.storyapp.util.doIfSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val addStoriUsecase: AddStoriUsecase
) : ViewModel() {

    var fotoUri: Uri? = null
    private val _state = MutableStateFlow(AddStoriState())
    val state = _state.asStateFlow()

    fun addStory(deskripsi: RequestBody?, image: File?) = viewModelScope.launch {
        addStoriUsecase(deskripsi, image)
            .onEach { res ->
                res.run {

                    doIfFailure { m, _ ->
                        setMessage(m)
                    }
                    doIfSuccess { respon ->
                        _state.update {
                            it.copy(responseBody = respon)
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

    fun setMessage(str: String?) {
        _state.update {
            it.copy(errorMessage = str, isLoading = false)
        }
    }


}