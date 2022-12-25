package id.radenyaqien.storyapp.data.repository

import id.radenyaqien.storyapp.data.remote.model.LoginResponse
import id.radenyaqien.storyapp.data.remote.model.LoginResult
import id.radenyaqien.storyapp.data.remote.model.RegisterResponse
import id.radenyaqien.storyapp.domain.model.Stories
import id.radenyaqien.storyapp.domain.model.User

object Dummy {


    fun generateDummyStories(): List<Stories> {
        val storyList = ArrayList<Stories>()
        for (i in 0..10) {
            val story = Stories(
                id = "story-$i",
                name = "name $i",
                description = "description $i",
                photoUrl = "https://images.unsplash.com/photo-1657214059175-53cb22261d38?ixlib=rb-4.0.3&dl=samsung-memory-k5eFm1f2esQ-unsplash.jpg&w=640&q=80&fm=jpg&crop=entropy&cs=tinysrgb",
                lat = -6.895173,
                lon = 107.607925,
                createdAt = "2022-01-01 01:30:45"
            )
            storyList.add(story)
        }
        return storyList
    }

    fun user(): User {
        return User(
            id = "user-122",
            nama = "test",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVV6TlJuM2Fkam84MXRhS0EiLCJpYXQiOjE2Njg1Njc0Njd9.mwTzjr3jyxPdy28Eznx8ma6jhJ8DyxvrQple6QIgeUY"
        )
    }

    fun loginSuccessResponse() = LoginResponse(
        error = false,
        message = "success",
        loginResult = LoginResult(
            name = "test",
            userId = "user-122",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVV6TlJuM2Fkam84MXRhS0EiLCJpYXQiOjE2Njg1Njc0Njd9.mwTzjr3jyxPdy28Eznx8ma6jhJ8DyxvrQple6QIgeUY"
        )
    )

    fun loginErrorResponse() = LoginResponse(
        error = true,
        message = "User not found",
        loginResult = null
    )

    fun registerErrorResponse() = RegisterResponse(
        error = true,
        message = "unexpected error Occured"
    )

    fun registerSuccessResponse() = RegisterResponse(
        error = false,
        message = "UserCreated"
    )
}