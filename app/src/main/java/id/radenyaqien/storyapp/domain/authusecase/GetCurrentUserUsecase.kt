package id.radenyaqien.storyapp.domain.authusecase

import id.radenyaqien.storyapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUsecase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke() = authRepository.getCurrentUser()
}