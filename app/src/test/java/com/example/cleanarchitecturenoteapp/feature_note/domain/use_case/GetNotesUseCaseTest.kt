package com.example.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.example.cleanarchitecturenoteapp.feature_note.data.repository.FakeNoteRepository
import com.example.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.example.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNotesUseCaseTest {
    private lateinit var getNotesUseCase: GetNotesUseCase
    private lateinit var repository: FakeNoteRepository

    @Before
    fun setUp() {
        repository = FakeNoteRepository()
        getNotesUseCase = GetNotesUseCase(repository)

        // create a fake data to populate our mutable list
        val notesToInsert = mutableListOf<Note>()
        ('a'..'z').forEachIndexed { index, c ->
            notesToInsert.add(
                Note(
                    title = c.toString(),
                    content = c.toString(),
                    timestamp = index.toLong(),
                    color = index
                )
            )
        }
        notesToInsert.shuffle() // shuffle the list to make them out of order
        runBlocking {
            notesToInsert.forEach {
                repository.insertNote(it)
            }

        }

    }

    @Test
    fun `Order notes by title ascending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Title(OrderType.Ascending)).first()

        for (i in 0..notes.size-2 ){
            assertThat(notes[i].title).isLessThan(notes[i+1].title)
        }
    }
    @Test
    fun `Order notes by title descending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Title(OrderType.Descending)).first()

        for (i in 0..notes.size-2 ){
            assertThat(notes[i].title).isGreaterThan(notes[i+1].title)
        }
    }
    @Test
    fun `Order notes by date ascending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Date(OrderType.Ascending)).first()

        for (i in 0..notes.size-2 ){
            assertThat(notes[i].timestamp).isLessThan(notes[i+1].timestamp)
        }
    }
    @Test
    fun `Order notes by date descending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Date(OrderType.Descending)).first()

        for (i in 0..notes.size-2 ){
            assertThat(notes[i].timestamp).isGreaterThan(notes[i+1].timestamp)
        }
    }
    @Test
    fun `Order notes by color ascending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Color(OrderType.Ascending)).first()

        for (i in 0..notes.size-2 ){
            assertThat(notes[i].color).isLessThan(notes[i+1].color)
        }
    }
    @Test
    fun `Order notes by color descending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Color(OrderType.Descending)).first()

        for (i in 0..notes.size-2 ){
            assertThat(notes[i].color).isGreaterThan(notes[i+1].color)
        }
    }
}


