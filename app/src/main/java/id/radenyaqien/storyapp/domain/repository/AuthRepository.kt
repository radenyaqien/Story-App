package id.radenyaqien.storyapp.domain.repository

import id.radenyaqien.storyapp.data.remote.model.LoginResponse
import id.radenyaqien.storyapp.domain.model.User
import id.radenyaqien.storyapp.util.MyResource
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<MyResource<LoginResponse>>
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<MyResource<ResponseBody>>

    suspend fun logout()

    fun getCurrentUser(): Flow<User>

    fun isLoggedIn(): Flow<Boolean>
    suspend fun saveUser(user: User)
}