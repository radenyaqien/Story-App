package id.radenyaqien.storyapp.ui.storyscreen

import id.radenyaqien.storyapp.domain.model.Stories
import id.radenyaqien.storyapp.domain.model.User

data class HomeUiState(
    val listData: List<Stories> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val userLogin: User? = null
)
