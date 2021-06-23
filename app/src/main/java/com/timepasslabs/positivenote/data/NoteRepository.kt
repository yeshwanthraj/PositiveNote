package com.timepasslabs.positivenote.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addNote(note: Note) {
        noteDao.insertNote(note)
    }

    fun getAllNotes() : Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    @SuppressWarnings("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteNote(note: Note) {
        noteDao.deleteNoteList(listOf(note))
    }

    @WorkerThread
    suspend fun deleteNotes(notes : List<Note>) {
        noteDao.deleteNoteList(notes)
    }
}