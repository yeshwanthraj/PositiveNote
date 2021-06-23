package com.timepasslabs.positivenote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timepasslabs.positivenote.data.NoteRepository
import com.timepasslabs.positivenote.ui.noteDetail.NoteDetailViewModel
import com.timepasslabs.positivenote.ui.notelist.NoteListViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

class CustomViewModelFactory @Inject constructor(private val repository: NoteRepository)
	: ViewModelProvider.Factory {

	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return if(modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
			NoteDetailViewModel(repository) as T
		} else if(modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
			NoteListViewModel(repository) as T
		} else {
			throw IllegalArgumentException()
		}
	}
}