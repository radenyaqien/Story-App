package id.radenyaqien.storyapp.ui.registerscreen

import okhttp3.ResponseBody

data class RegisterState(
    private val user: ResponseBody? = null,
    val isloading: Boolean = false,
    val error: String? = null
) {
    val isRegisterSuccess: Boolean get() = user != null
}