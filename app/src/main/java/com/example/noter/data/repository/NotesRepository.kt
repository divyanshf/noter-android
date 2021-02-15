package com.example.noter.data.repository

import android.util.Log
import com.example.noter.data.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

class NotesRepository
@Inject
constructor(
        private var firebaseFirestore: FirebaseFirestore,
        private var firebaseAuth: FirebaseAuth
){
    private var user = firebaseAuth.currentUser

    suspend fun getAllNotes() = flow<List<Note>> {
        val allNotes = ArrayList<Note>()
        Log.i("Notes", "Before")
        val snap = firebaseFirestore.collection("users")
                .document(user?.email!!)
                .collection("notes")
                .get()
                .await()
        Log.i("Notes", "After")
        val data = snap.documents
        for(i in data){
            Log.i("Note", i.toString())
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
        emit(allNotes)
    }
}