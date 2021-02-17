package com.example.noter.data.repository

import android.util.Log
import com.example.noter.data.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

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
            if(i["trash"] == false && i["archive"] == false){
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
        }
        emit(allNotes)
    }

    suspend fun getArchivedNotes() = flow<List<Note>> {
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
            if(i["archive"] == true && i["trash"] == false){
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
        }
        emit(allNotes)
    }

    suspend fun getStarredNotes() = flow<List<Note>> {
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
            if(i["star"] == true && i["trash"] == false && i["archive"] == false){
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
        }
        emit(allNotes)
    }

    suspend fun getTrashNotes() = flow<List<Note>> {
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
            if(i["trash"] == true){
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
        }
        emit(allNotes)
    }
}