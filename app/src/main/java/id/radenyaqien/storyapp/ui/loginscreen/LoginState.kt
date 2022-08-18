package id.radenyaqien.storyapp.ui.loginscreen

import id.radenyaqien.storyapp.domain.model.User

data class LoginState(
    private val user: User? = null,
    val isloading: Boolean = false,
    val error: String? = null
) {
    val isLoggedIn: Boolean get() = user != null
}