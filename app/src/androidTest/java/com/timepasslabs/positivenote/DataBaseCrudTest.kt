package com.timepasslabs.positivenote

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.timepasslabs.positivenote.data.Note
import com.timepasslabs.positivenote.data.NoteDao
import com.timepasslabs.positivenote.data.NoteDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class)
@LargeTest
class DataBaseCrudTest {

    private val noteDao : NoteDao by lazy {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        NoteDatabase.getInstance(context).getNoteDao()
    }

    private var noteCount : Int? = 0

    private val testNote = Note("test note","test details","2020/05/12 22:25:14",System.currentTimeMillis())

    @Before
    fun initDao() {
        noteCount = getCurrentNoteCount()
    }

    @Test
    suspend fun insertNote() : Unit {
        runBlocking {
            noteDao.insertNote(testNote)
        }
        val latestCount = getCurrentNoteCount()
        assert(latestCount == noteCount?.plus(1))
    }

    @Test
    suspend fun updateNote() {
        val updatedNoteTitle = "updated ${testNote.title}"
        val updatedNote = Note(updatedNoteTitle,testNote.details,testNote.date,testNote.lastUpdate,testNote.id)
        runBlocking {
            noteDao.insertNote(updatedNote)
        }
        val storedNote = noteDao.getNote(testNote.id)
        assert(storedNote.title == updatedNoteTitle)
    }

    @Test
    suspend fun deleteNote() {
        runBlocking {
            noteDao.deleteNote(testNote)
        }
        val latestCount = getCurrentNoteCount()
        assert(latestCount == noteCount?.minus(1))
    }

    private fun getCurrentNoteCount() : Int? = runBlocking {
        noteDao.getAllNotes().asLiveData().value?.size
    }


}