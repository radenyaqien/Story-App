package id.radenyaqien.storyapp.data.repository

import id.radenyaqien.storyapp.data.datastore.UserPreff
import id.radenyaqien.storyapp.data.remote.RemoteDataSource
import id.radenyaqien.storyapp.data.remote.model.LoginResponse
import id.radenyaqien.storyapp.domain.repository.AuthRepository
import id.radenyaqien.storyapp.domain.model.User
import id.radenyaqien.storyapp.util.MyResource
import id.radenyaqien.storyapp.util.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val dataSource: RemoteDataSource,
    private val userPreff: UserPreff
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Flow<MyResource<LoginResponse>> {
        val loginResponse = dataSource.login(email, password)
        val res = loginResponse.first().data?.loginResult
        res?.toUser()?.let { userPreff.save(it) }
        return loginResponse
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ) = dataSource.register(name, email, password)

    override suspend fun logout() {
        userPreff.clear()
    }

    override fun getCurrentUser(): Flow<User> = userPreff.user
    override fun isLoggedIn(): Flow<Boolean> {
        return userPreff.isLogin
    }

    override suspend fun saveUser(user: User) {
        userPreff.save(user)
    }

}