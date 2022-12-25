package id.radenyaqien.storyapp.util

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.RoundedCornersTransformation
import id.radenyaqien.storyapp.R
import id.radenyaqien.storyapp.data.remote.model.LoginResult
import id.radenyaqien.storyapp.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException


fun <Api> safeApiCall(
    apiCall: suspend () -> Api
) = flow<MyResource<Api>> {
    emit(MyResource.Loading())
    runCatching {
        apiCall.invoke()
    }.onSuccess {
        emit(MyResource.Success(it))
    }.onFailure {
        Log.e("safeApiCall2: ", it.message, it)
        when (it) {
            is HttpException -> {
                val res = it.response()?.errorBody()?.string()?.let { it1 -> JSONObject(it1) }
                val error = res?.getString("message")
                emit(
                    MyResource.Error(
                        error ?: "Terjadi Kesalahan"
                    )
                )
            }
            else -> {
                emit(MyResource.Error("Terjadi kesalahan"))
            }
        }
    }
}.flowOn(Dispatchers.IO)

inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}

inline fun <reified T, reified R> MyResource<T>.map(transform: (T) -> R): MyResource<R> {
    return when (this) {
        is MyResource.Success -> MyResource.Success(transform(requireNotNull(data)))
        is MyResource.Error -> MyResource.Error(message ?: "Terjadi Kesalahan")
        is MyResource.Loading -> MyResource.Loading()
    }
}

inline fun <reified T> MyResource<T>.doIfFailure(callback: (error: String?, data: T?) -> Unit) {
    if (this is MyResource.Error) {
        callback(message, data)
    }
}

inline fun <reified T> MyResource<T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is MyResource.Success) {
        callback(requireNotNull(data))
    }
}

inline fun <reified T> MyResource<T>.doIfLoading(callback: () -> Unit) {
    if (this is MyResource.Loading) {
        callback()
    }
}

fun LoginResult.toUser() = User(
    id = this.userId,
    nama = this.name,
    token = this.token
)



@BindingAdapter("setImageUrl")
fun ImageView.loadImage(url: String) {
    load(url) {

        crossfade(true)
        transformations(RoundedCornersTransformation(20f))
        placeholder(R.drawable.ic_launcher_background)
        error(R.drawable.ic_launcher_background)
    }
}

