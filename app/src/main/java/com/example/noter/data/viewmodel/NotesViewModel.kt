package com.example.noter.data.viewmodel

import androidx.lifecycle.*
import com.example.noter.data.model.Note
import com.example.noter.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel
@Inject
constructor(
        private val notesRepository: NotesRepository
) : ViewModel()
{
    var mAllNotes : LiveData<List<Note>> = MutableLiveData()

    fun getAllNotes() {
        viewModelScope.launch {
            mAllNotes = notesRepository.getAllNotes().asLiveData()
        }
    }

}