package id.radenyaqien.storyapp.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.radenyaqien.storyapp.BuildConfig
import id.radenyaqien.storyapp.data.datastore.UserPreff
import id.radenyaqien.storyapp.data.remote.RemoteDataSource
import id.radenyaqien.storyapp.data.remote.StoryApi
import id.radenyaqien.storyapp.data.repository.AuthRepoImpl
import id.radenyaqien.storyapp.data.repository.StoriesRepoImpl
import id.radenyaqien.storyapp.domain.repository.AuthRepository
import id.radenyaqien.storyapp.domain.repository.StoryRepository
import id.radenyaqien.storyapp.util.Constant
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    @Provides
    @Singleton
    fun provideClient(): OkHttpClient = OkHttpClient()
        .newBuilder()
        .also { client ->
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(loggingInterceptor)
            } else {
                val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                    )
                    .build()
                client.connectionSpecs(listOf(spec))
            }
        }.build()


    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit
        .Builder()
        .baseUrl(Constant.BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): StoryApi = retrofit.create(StoryApi::class.java)


    @Provides
    @Singleton
    fun providePrefference(
        @ApplicationContext context: Context
    ) = UserPreff(context)


    @Provides
    @Singleton
    fun provideAuthRepo(
        remoteDataSource: RemoteDataSource,
        userPreff: UserPreff
    ): AuthRepository = AuthRepoImpl(remoteDataSource, userPreff)


    @Provides
    @Singleton
    fun providestoriesRepos(
        remoteDataSource: RemoteDataSource,
        userPreff: UserPreff
    ): StoryRepository = StoriesRepoImpl(remoteDataSource, userPreff)


}