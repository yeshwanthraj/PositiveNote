package com.timepasslabs.positivenote.ui.noteDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timepasslabs.positivenote.data.Note
import com.timepasslabs.positivenote.data.NoteRepository
import com.timepasslabs.positivenote.di.NoteDetailScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@NoteDetailScope
class NoteDetailViewModel @Inject constructor(
	private val noteRepository: NoteRepository
) : ViewModel() {

	fun addNote(note : Note) {
		viewModelScope.launch {
			noteRepository.addNote(note)
		}
	}

	fun deleteNote(note : Note) {
		viewModelScope.launch {
			noteRepository.deleteNote(note)
		}
	}

	override fun onCleared() {
		super.onCleared()
		viewModelScope.cancel()
	}

}