package com.timepasslabs.positivenote.ui.noteDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timepasslabs.positivenote.data.NoteRepository
import java.lang.IllegalArgumentException

class NoteDetailViewModelFactory(
    private val noteRepository: NoteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            return NoteDetailViewModel(noteRepository) as T
        }
        throw IllegalArgumentException("Invalid viewmodel")
    }
}