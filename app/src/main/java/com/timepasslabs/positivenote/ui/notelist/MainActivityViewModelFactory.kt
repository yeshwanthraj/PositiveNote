package com.timepasslabs.positivenote.ui.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timepasslabs.positivenote.data.NoteRepository
import java.lang.IllegalArgumentException

class MainActivityViewModelFactory(
	private val noteRepository: NoteRepository
) : ViewModelProvider.Factory {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
			return MainActivityViewModel(noteRepository) as T
		}
		throw IllegalArgumentException("Invalid viewmodel")
	}
}