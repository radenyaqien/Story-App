package id.radenyaqien.storyapp.domain.authusecase

import id.radenyaqien.storyapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUsecase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String) =
        repository.register(name, email, password)
}