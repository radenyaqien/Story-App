package id.radenyaqien.storyapp.domain.storiesusecase

import id.radenyaqien.storyapp.domain.repository.StoryRepository
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class AddStoriUsecase @Inject constructor(
    private val repo: StoryRepository
) {

    suspend operator fun invoke(deskripsi: RequestBody?, image: File?) =
        repo.addStory(deskripsi, image)

}