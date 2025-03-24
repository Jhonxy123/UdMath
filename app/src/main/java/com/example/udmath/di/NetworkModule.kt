package com.example.udmath.di

import com.example.udmath.data.repository.AuthClientService
import com.example.udmath.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


// para manejar los servicios en las dependencias
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton // indica que solo se creara una unica instancia de la dependencia durante la vida de la aplicacion
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthClientService(firebaseAuth)  // Debe devolver la implementación correcta
    }

}