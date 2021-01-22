package com.timepasslabs.positivenote

import android.app.Application
import com.timepasslabs.positivenote.data.NoteDatabase
import com.timepasslabs.positivenote.data.NoteRepository

class PositiveNoteApplication : Application() {

	private val database by lazy { NoteDatabase.getInstance(this) }

	val repository by lazy { NoteRepository.newInstance(database.getNoteDao()) }

}