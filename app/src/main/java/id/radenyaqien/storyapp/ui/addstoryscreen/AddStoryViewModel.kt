package id.radenyaqien.storyapp.ui.addstoryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.radenyaqien.storyapp.domain.storiesusecase.AddStoriUsecase
import id.radenyaqien.storyapp.util.doIfFailure
import id.radenyaqien.storyapp.util.doIfLoading
import id.radenyaqien.storyapp.util.doIfSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val addStoriUsecase: AddStoriUsecase
) : ViewModel() {


    private val _state = MutableStateFlow(AddStoriState())
    val state = _state.asStateFlow()

    fun addStory(deskripsi: RequestBody?, image: File?) = viewModelScope.launch {
        addStoriUsecase(deskripsi, image)
            .collect { res ->
                res.run {

                    doIfFailure { m, _ ->
                        setMessage(m)
                    }
                    doIfSuccess { respon ->
                        _state.update {
                            it.copy(isSuccess = !respon.error)
                        }
                    }
                    doIfLoading {
                        _state.update {
                            it.copy(isLoading = true)
                        }
                    }
                }
            }
    }

    fun setMessage(str: String?) {
        _state.update {
            it.copy(errorMessage = str, isLoading = false)
        }
    }


}