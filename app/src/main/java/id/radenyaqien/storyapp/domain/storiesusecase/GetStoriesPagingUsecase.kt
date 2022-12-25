package id.radenyaqien.storyapp.domain.storiesusecase

import androidx.paging.map
import id.radenyaqien.storyapp.domain.authusecase.GetCurrentUserUsecase
import id.radenyaqien.storyapp.domain.repository.StoryRepository
import id.radenyaqien.storyapp.util.asStory
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetStoriesPagingUsecase @Inject constructor(
    private val repository: StoryRepository,
    private val getCurrentUserUsecase: GetCurrentUserUsecase
) {

    operator fun invoke() = flow {
        emitAll(
            repository.getStoriesPagingData(getCurrentUserUsecase().mapNotNull { it?.token }
                .first())
        )
    }.map {
        it.map { entity ->
            entity.asStory()
        }
    }
}