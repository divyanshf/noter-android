package com.example.noter.data.repository

import com.example.noter.data.model.Note
import com.example.noter.data.model.Result
import com.example.noter.data.sources.NotesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepository
@Inject
constructor(
        private val notesRemoteDataSource: NotesRemoteDataSource
){
    suspend fun insertNote(note:Note){
        notesRemoteDataSource.insertNote(note)
    }

    suspend fun updateNote(note:Note){
        notesRemoteDataSource.updateNote(note)
    }

    suspend fun deleteNote(note:Note){
        notesRemoteDataSource.deleteNote(note)
    }

    suspend fun getAllNotes() : Flow<Result<List<Note>>>{
        return notesRemoteDataSource.getAllNotes()
    }

    suspend fun getArchivedNotes() : Flow<Result<List<Note>>>{
        return notesRemoteDataSource.getArchivedNotes()
    }

    suspend fun getStarredNotes() : Flow<Result<List<Note>>>{
        return notesRemoteDataSource.getStarredNotes()
    }

    suspend fun getTrashNotes() : Flow<Result<List<Note>>>{
        return notesRemoteDataSource.getTrashNotes()
    }

    suspend fun searchNotes(query:String) : Flow<Result<List<Note>>>{
        return notesRemoteDataSource.searchNotes(query)
    }
}