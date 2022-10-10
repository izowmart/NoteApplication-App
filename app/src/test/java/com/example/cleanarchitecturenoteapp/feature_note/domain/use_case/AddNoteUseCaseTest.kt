package com.example.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.example.cleanarchitecturenoteapp.feature_note.data.repository.FakeNoteRepository
import com.example.cleanarchitecturenoteapp.feature_note.domain.model.InvalidNoteException
import com.example.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class AddNoteUseCaseTest {
    lateinit var repository: FakeNoteRepository
    lateinit var addNoteUseCase: AddNoteUseCase


    @Before
    fun setup() {
        repository = FakeNoteRepository()
        addNoteUseCase = AddNoteUseCase(repository)


    }

    @Test
    fun `invalidate note title if blank, Invalidate field`(): Unit = runBlocking {
        val note = Note(
            title = "",
            content = "not blank",
            timestamp = 11,
            color = 1
        )
        try {
            addNoteUseCase(note = note)
            assert(false)
        } catch (e: InvalidNoteException) {
            assertEquals(e.message, "The title of the note can't be empty.")

        }

    }
    @Test
    fun `invalidate note content if blank, Invalidate field`(): Unit = runBlocking {
        val note = Note(
            title = "not blank",
            content = "",
            timestamp = 11,
            color = 1
        )
        try {
            addNoteUseCase(note = note)
            assert(false)
        } catch (e: InvalidNoteException) {
            assertEquals(e.message, "The content of the note can't be empty.")

        }

    }
    @Test
    fun `Validate note if all field conditions are met, add note`(): Unit = runBlocking {
        val note = Note(
            title = "not blank",
            content = "not blank",
            timestamp = 11,
            color = 1
        )
        try {
            addNoteUseCase(note = note)
            assert(true)
        } catch (e: InvalidNoteException) {
            assertEquals(e.message, "The content of the note can't be empty.")

        }

    }
}