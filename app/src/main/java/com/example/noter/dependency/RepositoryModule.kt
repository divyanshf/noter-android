package com.example.noter.dependency

import com.example.noter.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(firebaseFirestore: FirebaseFirestore, firebaseAuth: FirebaseAuth):UserRepository {
        return UserRepository(firebaseFirestore, firebaseAuth)
    }

}