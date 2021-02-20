package com.example.noter.data.sources

import android.util.Log
import com.example.noter.data.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotesRemoteDataSource
@Inject
constructor(
        firebaseFirestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
)
{
    private var user = firebaseAuth.currentUser
    private var notesCollection = firebaseFirestore.collection("users").document(user?.email!!).collection("notes")


    suspend fun insertNote(note:Note) {
        notesCollection
                .add(createMapFromNote(note))
                .await()
    }

    suspend fun updateNote(note:Note){
        notesCollection.document(note.id.toString())
                .update(createMapFromNote(note))
                .await()
    }

    suspend fun deleteNote(note:Note){
        notesCollection.document(note.id.toString())
                .delete()
                .await()
    }

    suspend fun getAllNotes() = flow {
        val snap = notesCollection
                .whereEqualTo("trash", false)
                .whereEqualTo("archive", false)
                .get()
                .await()

        emit(createArrayFromMap(snap.documents))
    }

    suspend fun getArchivedNotes() = flow {
        val snap = notesCollection
                .whereEqualTo("trash", false)
                .whereEqualTo("archive", true)
                .get()
                .await()

        emit(createArrayFromMap(snap.documents))
    }

    suspend fun getStarredNotes() = flow {
        val snap = notesCollection
                .whereEqualTo("star", true)
                .whereEqualTo("trash", false)
                .whereEqualTo("archive", false)
                .get()
                .await()

        emit(createArrayFromMap(snap.documents))
    }

    suspend fun getTrashNotes() = flow {
        val snap = notesCollection
                .whereEqualTo("trash", true)
                .get()
                .await()

        emit(createArrayFromMap(snap.documents))
    }

    suspend fun searchNotes(query:String) = flow {
        val snap = notesCollection
                .whereEqualTo("trash", false)
                .whereEqualTo("archive", false)
                .get()
                .await()

        val snapDocuments = snap
                .documents
                .filter {
                    it["title"].toString().contains(query, true) || it["content"].toString().contains(query, true)
                }

        val searchResult = createArrayFromMap(snapDocuments as MutableList<DocumentSnapshot>)
        Log.i("Search", "$searchResult")
        emit(searchResult)
    }

    private fun createArrayFromMap(documents: MutableList<DocumentSnapshot>): List<Note>{
        val allNotes = ArrayList<Note>()

        for(i in documents){
            val note = Note(
                    i.id,
                    i["title"] as String,
                    i["content"] as String,
                    i["star"] as Boolean,
                    i["archive"] as Boolean,
                    i["trash"] as Boolean
            )
            allNotes.add(note)
        }

        return allNotes
    }

    private fun createMapFromNote(note: Note):HashMap<String, Any>{
        val hashMap = HashMap<String, Any>()
        hashMap["title"] = note.title.toString()
        hashMap["content"] = note.content.toString()
        hashMap["archive"] = note.archived
        hashMap["star"] = note.starred
        hashMap["trash"] = note.trash

        return hashMap
    }
}