package id.radenyaqien.storyapp.domain.storiesusecase

import id.radenyaqien.storyapp.domain.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AddStoriUsecase @Inject constructor(
    private val repo: StoryRepository
) {

    suspend operator fun invoke(deskripsi: RequestBody, image: MultipartBody.Part) =
        repo.addStory(deskripsi, image)

}