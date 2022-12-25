package id.radenyaqien.storyapp.ui.addstoryscreen

data class AddStoriState(
    var isLoading: Boolean = false,
    var isSuccess: Boolean = false,
    var errorMessage: String? = null
)