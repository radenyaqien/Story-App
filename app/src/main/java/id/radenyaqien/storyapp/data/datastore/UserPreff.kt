package id.radenyaqien.storyapp.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import id.radenyaqien.storyapp.data.datastore.UserPreff.PreferencesKeys.IS_LOGIN
import id.radenyaqien.storyapp.data.datastore.UserPreff.PreferencesKeys.NAME
import id.radenyaqien.storyapp.data.datastore.UserPreff.PreferencesKeys.TOKEN
import id.radenyaqien.storyapp.data.datastore.UserPreff.PreferencesKeys.USER_ID
import id.radenyaqien.storyapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val PREF_NAME = "story_app"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)

class UserPreff @Inject constructor(
    private val context: Context
) {

    val isLogin: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGIN] ?: false
        }


    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun save(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = user.id
            preferences[NAME] = user.nama
            preferences[TOKEN] = user.token
            preferences[IS_LOGIN] = true
        }
    }

    val user: Flow<User?> = context.dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e("Preferences", "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            if (it[USER_ID] != null) {
                User(
                    it[USER_ID] ?: "",
                    it[NAME] ?: "",
                    it[TOKEN] ?: ""
                )
            } else {
                null
            }
        }

    private object PreferencesKeys {
        val IS_LOGIN = booleanPreferencesKey("is_login")
        val USER_ID = stringPreferencesKey("user_id")
        val NAME = stringPreferencesKey("name")
        val TOKEN = stringPreferencesKey("token")
    }
}