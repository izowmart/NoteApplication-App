package com.example.cleanarchitecturenoteapp.di

import android.app.Application
import androidx.room.Room
import com.example.cleanarchitecturenoteapp.feature_note.data.data_source.NoteDatabase
import com.example.cleanarchitecturenoteapp.feature_note.data.repository.NoteRepositoryImpl
import com.example.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import com.example.cleanarchitecturenoteapp.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModuleTest {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase{
        return Room.inMemoryDatabaseBuilder(
            app,
            NoteDatabase::class.java
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository{
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            addNoteUseCase = AddNoteUseCase(repository),
        )
    }
}