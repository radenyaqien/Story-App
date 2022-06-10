package id.radenyaqien.storyapp.domain.storiesusecase

import id.radenyaqien.storyapp.domain.repository.StoryRepository
import javax.inject.Inject

class GetStoriesUsecase @Inject constructor(
    private val storyRepository: StoryRepository
) {
    suspend operator fun invoke() = storyRepository.getStories()

}