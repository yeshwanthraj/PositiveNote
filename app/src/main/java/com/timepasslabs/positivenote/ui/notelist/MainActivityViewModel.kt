package com.timepasslabs.positivenote.ui.notelist

import androidx.lifecycle.*
import com.timepasslabs.positivenote.data.Note
import com.timepasslabs.positivenote.data.NoteRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivityViewModel(
	private val noteRepository: NoteRepository
) : ViewModel() {

	var noteList : LiveData<List<Note>> = noteRepository.getAllNotes().asLiveData()

	fun insertNote(note : Note) = viewModelScope.launch {
		noteRepository.addNote(note)
	}

}