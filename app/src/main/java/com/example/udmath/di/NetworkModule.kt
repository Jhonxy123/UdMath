package com.example.udmath.di

import com.example.udmath.data.repository.AuthClientService
import com.example.udmath.data.repository.MateriasClenteService
import com.example.udmath.data.repository.RecursosClienteService
import com.example.udmath.domain.repository.AuthRepository
import com.example.udmath.domain.repository.MateriaRepository
import com.example.udmath.domain.repository.RecursoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()


    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository = AuthClientService(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideRecursoRepository(
        firestore: FirebaseFirestore
    ): RecursoRepository = RecursosClienteService(firestore)

    @Provides
    @Singleton
    fun provideMateriaRepository(
        firestore: FirebaseFirestore
    ): MateriaRepository = MateriasClenteService(firestore)

}
