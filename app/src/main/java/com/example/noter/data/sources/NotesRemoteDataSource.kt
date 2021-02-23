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
        private val firebaseFirestore: FirebaseFirestore,
        private val firebaseAuth: FirebaseAuth
)
{
    private var usersCollection = firebaseFirestore.collection("users")

    suspend fun insertNote(note:Note) {
        val user = firebaseAuth.currentUser
        try {
            usersCollection
                    .document(user?.email!!)
                    .collection("notes")
                    .add(createMapFromNote(note))
                    .await()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun updateNote(note:Note){
        try {
            val user = firebaseAuth.currentUser
            usersCollection
                    .document(user?.email!!)
                    .collection("notes")
                    .document(note.id.toString())
                    .update(createMapFromNote(note))
                    .await()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun deleteNote(note:Note){
        try {
            val user = firebaseAuth.currentUser
            usersCollection
                    .document(user?.email!!)
                    .collection("notes")
                    .document(note.id.toString())
                    .delete()
                    .await()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun getAllNotes() = flow {
        var result:List<Note> = ArrayList()
        try {
            val user = firebaseAuth.currentUser
            val snap = usersCollection
                    .document(user?.email!!)
                    .collection("notes")
                    .whereEqualTo("trash", false)
                    .whereEqualTo("archive", false)
                    .get()
                    .await()

            result = createArrayFromMap(snap.documents)
        }catch (e:Exception){
            e.printStackTrace()
        }
        emit(result)
    }

    suspend fun getArchivedNotes() = flow {
        var result:List<Note> = ArrayList()
        try {
            val user = firebaseAuth.currentUser
            val snap = usersCollection
                    .document(user?.email!!)
                    .collection("notes")
                    .whereEqualTo("trash", false)
                    .whereEqualTo("archive", true)
                    .get()
                    .await()

            result = createArrayFromMap(snap.documents)

        }catch (e:Exception){
            e.printStackTrace()
        }
        emit(result)
    }

    suspend fun getStarredNotes() = flow {
        var result:List<Note> = ArrayList()
        try {
            val user = firebaseAuth.currentUser
            val snap = usersCollection
                    .document(user?.email!!)
                    .collection("notes")
                    .whereEqualTo("star", true)
                    .whereEqualTo("trash", false)
                    .whereEqualTo("archive", false)
                    .get()
                    .await()

            result = createArrayFromMap(snap.documents)

        }catch (e:Exception){
            e.printStackTrace()
        }
        emit(result)
    }

    suspend fun getTrashNotes() = flow {
        var result:List<Note> = ArrayList()
        try {
            val user = firebaseAuth.currentUser
            val snap = usersCollection
                    .document(user?.email!!)
                    .collection("notes")
                    .whereEqualTo("trash", true)
                    .get()
                    .await()

            result = createArrayFromMap(snap.documents)

        }catch (e:Exception){
            e.printStackTrace()
        }
        emit(result)
    }

    suspend fun searchNotes(query:String) = flow {
        var searchResult:List<Note> = ArrayList()
        try {
            val user = firebaseAuth.currentUser
            val snap = usersCollection
                    .document(user?.email!!)
                    .collection("notes")
                    .whereEqualTo("trash", false)
                    .whereEqualTo("archive", false)
                    .get()
                    .await()

            val snapDocuments = snap
                    .documents
                    .filter {
                        it["title"].toString().contains(query, true) || it["content"].toString().contains(query, true)
                    }

            searchResult = createArrayFromMap(snapDocuments as MutableList<DocumentSnapshot>)

        }catch (e:Exception){
            e.printStackTrace()
        }
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