package id.radenyaqien.storyapp.ui.addstoryscreen

import okhttp3.ResponseBody

data class AddStoriState(
    var isLoading: Boolean = false,
    private var responseBody: ResponseBody? = null,
    var errorMessage: String? = null
) {
    var isSuccess: Boolean = responseBody != null
}