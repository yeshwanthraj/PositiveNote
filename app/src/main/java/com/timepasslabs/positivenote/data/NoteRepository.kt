package com.timepasslabs.positivenote.data

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class NoteRepository private constructor(private val noteDao: NoteDao) {

    companion object {

        @Volatile var INSTANCE : NoteRepository? = null

        fun newInstance(noteDao: NoteDao) : NoteRepository =
            INSTANCE?: synchronized(this) {
                INSTANCE?: NoteRepository(noteDao).also { INSTANCE = it }
            }
    }

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
        noteDao.deleteNote(note)
    }
}