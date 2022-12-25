package id.radenyaqien.storyapp.ui.storyscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import id.radenyaqien.storyapp.domain.storiesusecase.GetStoriesPagingUsecase
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getStoriesPagingUsecase: GetStoriesPagingUsecase
) : ViewModel() {

    val data = getStoriesPagingUsecase().mapNotNull { it }.cachedIn(viewModelScope)

}




