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
    var mStarredNotes : LiveData<List<Note>> = MutableLiveData()
    var mArchivedNotes : LiveData<List<Note>> = MutableLiveData()
    var mTrashNotes : LiveData<List<Note>> = MutableLiveData()

    fun insert(note:Note){
        viewModelScope.launch {
            notesRepository.insertNote(note)
        }
    }

    fun updateNote(note:Note){
        viewModelScope.launch {
            notesRepository.updateNote(note)
        }
    }

    fun deleteNote(note:Note){
        viewModelScope.launch {
            notesRepository.deleteNote(note)
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            mAllNotes = notesRepository.getAllNotes().asLiveData()
        }
    }

    fun getStarredNotes() {
        viewModelScope.launch {
            mStarredNotes = notesRepository.getStarredNotes().asLiveData()
        }
    }

    fun getArchivedNotes() {
        viewModelScope.launch {
            mArchivedNotes = notesRepository.getArchivedNotes().asLiveData()
        }
    }

    fun getTrashNotes() {
        viewModelScope.launch {
            mTrashNotes = notesRepository.getTrashNotes().asLiveData()
        }
    }

}