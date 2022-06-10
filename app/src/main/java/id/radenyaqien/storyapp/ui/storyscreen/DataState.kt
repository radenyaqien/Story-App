package id.radenyaqien.storyapp.ui.storyscreen

import id.radenyaqien.storyapp.domain.model.Stories

data class DataState(
    val user: List<Stories> = emptyList(),
    val isloading: Boolean = false,
    val error: String? = null
)
