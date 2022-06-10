package id.radenyaqien.storyapp.domain.authusecase

import id.radenyaqien.storyapp.domain.model.User
import id.radenyaqien.storyapp.domain.repository.AuthRepository
import javax.inject.Inject

class SaveUserUsecase @Inject constructor(
    private val userRepository: AuthRepository
) {
    suspend operator fun invoke(user: User) = userRepository.saveUser(user)

}