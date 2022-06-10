package id.radenyaqien.storyapp.ui.detilscreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetilStoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        viewModelScope.launch {
           val storyId =  savedStateHandle.get<String>(CURRENTSTORYID) ?: return@launch
            Log.d("Viewmodel", "story: $storyId")
        }
    }

    companion object {
        const val CURRENTSTORYID = "Stories"
    }


}