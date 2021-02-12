package com.example.noter.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.noter.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel
@Inject
constructor(
        private val notesRepository: NotesRepository
) : ViewModel()
{
//    val mAllNotes = notesRepository.getAllNotes().asLiveData()
}