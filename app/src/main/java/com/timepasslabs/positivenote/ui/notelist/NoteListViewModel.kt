package com.timepasslabs.positivenote.ui.notelist

import androidx.lifecycle.*
import com.timepasslabs.positivenote.data.Note
import com.timepasslabs.positivenote.data.NoteRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NoteListViewModel(private val noteRepository: NoteRepository) : ViewModel() {

	var noteList : LiveData<List<Note>> = noteRepository.getAllNotes().asLiveData()

	fun deleteNote(noteList : List<Note>) {
		viewModelScope.launch {
			noteRepository.deleteNotes(noteList)
		}
	}

	override fun onCleared() {
		super.onCleared()
		viewModelScope.cancel()
	}

}