package id.radenyaqien.storyapp.domain.authusecase

import id.radenyaqien.storyapp.data.remote.model.RegisterResponse
import id.radenyaqien.storyapp.domain.repository.AuthRepository
import id.radenyaqien.storyapp.util.MyResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUsecase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Flow<MyResource<RegisterResponse>> =
        repository.register(name, email, password)
}