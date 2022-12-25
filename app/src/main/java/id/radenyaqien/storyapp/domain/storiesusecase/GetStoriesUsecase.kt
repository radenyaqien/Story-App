package id.radenyaqien.storyapp.domain.storiesusecase

import id.radenyaqien.storyapp.domain.authusecase.GetCurrentUserUsecase
import id.radenyaqien.storyapp.domain.repository.StoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetStoriesUsecase @Inject constructor(
    private val storyRepository: StoryRepository,
    private val getCurrentUserUsecase: GetCurrentUserUsecase
) {
    operator fun invoke() = getCurrentUserUsecase().mapNotNull { it?.token }.flatMapLatest {
        storyRepository.getStories(it)
    }

}