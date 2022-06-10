package id.radenyaqien.storyapp.ui.registerscreen

import okhttp3.ResponseBody

data class RegisterState(
    val user: ResponseBody? = null,
    val isloading: Boolean = false,
    val error: String? = null
)