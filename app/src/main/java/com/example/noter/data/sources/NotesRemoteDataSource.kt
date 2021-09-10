package com.example.noter.data.sources

import com.example.noter.data.model.Note
import com.example.noter.data.model.Result
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotesRemoteDataSource
@Inject
constructor(
        firebaseFirestore: FirebaseFirestore,
        private val firebaseAuth: FirebaseAuth
)
{
    private var usersCollection = firebaseFirestore.collection("users")

    suspend fun insertNote(note:Note) {
        val user = firebaseAuth.currentUser
        try {
            usersCollection
                    .document(user?.uid!!)
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
                    .document(user?.uid!!)
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
                    .document(user?.uid!!)
                    .collection("notes")
                    .document(note.id.toString())
                    .delete()
                    .await()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun getAllNotes() = flow {
        emit(Result.Progress)
        val result: List<Note>
        try {
            val user = firebaseAuth.currentUser
            val snap = usersCollection
                    .document(user?.uid!!)
                    .collection("notes")
                    .whereEqualTo("trash", false)
                    .whereEqualTo("archive", false)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

            result = createArrayFromMap(snap.documents)
            emit(Result.Success(result))
        }catch (e:Exception){
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
    }

    suspend fun getArchivedNotes() = flow {
        emit(Result.Progress)
        val result: List<Note>
        try {
            val user = firebaseAuth.currentUser
            val snap = usersCollection
                    .document(user?.uid!!)
                    .collection("notes")
                    .whereEqualTo("trash", false)
                    .whereEqualTo("archive", true)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

            result = createArrayFromMap(snap.documents)
            emit(Result.Success(result))
        }catch (e:Exception){
            e.printStackTrace()
            emit(Result.Error("Something went wrong!"))
        }
    }

    suspend fun getStarredNotes() = flow {
        emit(Result.Progress)
        val result: List<Note>
        try {
            val user = firebaseAuth.currentUser
            val snap = usersCollection
                    .document(user?.uid!!)
                    .collection("notes")
                    .whereEqualTo("star", true)
                    .whereEqualTo("trash", false)
                    .whereEqualTo("archive", false)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

            result = createArrayFromMap(snap.documents)
            emit(Result.Success(result))
        }catch (e:Exception){
            e.printStackTrace()
            emit(Result.Error("Something went wrong!"))
        }
    }

    suspend fun getTrashNotes() = flow {
        emit(Result.Progress)
        val result: List<Note>
        try {
            val user = firebaseAuth.currentUser
            val snap = usersCollection
                    .document(user?.uid!!)
                    .collection("notes")
                    .whereEqualTo("trash", true)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

            result = createArrayFromMap(snap.documents)
            emit(Result.Success(result))
        }catch (e:Exception){
            e.printStackTrace()
            emit(Result.Error("Something went wrong!"))
        }
    }

    suspend fun searchNotes(query:String) = flow {
        emit(Result.Progress)
        val searchResult: List<Note>
        try {
            val user = firebaseAuth.currentUser
            val snap = usersCollection
                    .document(user?.uid!!)
                    .collection("notes")
                    .whereEqualTo("trash", false)
                    .whereEqualTo("archive", false)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

            val snapDocuments = snap
                    .documents
                    .filter {
                        it["title"].toString().contains(query, true) || it["content"].toString().contains(query, true)
                    }

            searchResult = createArrayFromMap(snapDocuments as MutableList<DocumentSnapshot>)
            emit(Result.Success(searchResult))
        }catch (e:Exception){
            e.printStackTrace()
            emit(Result.Error("Something went wrong!"))
        }
    }

    private fun createArrayFromMap(documents: MutableList<DocumentSnapshot>): List<Note>{
        val allNotes = ArrayList<Note>()

        for(i in documents){
            var note:Note
            try {
                note = Note(
                        i.id,
                        i["title"] as String,
                        i["content"] as String,
                        i["star"] as Boolean,
                        i["archive"] as Boolean,
                        i["trash"] as Boolean,
                        i["edited"] as Boolean,
                        i["timestamp"] as Timestamp
                )
                allNotes.add(note)
            }catch (e:Exception){
                note = Note(
                        i.id,
                        i["title"] as String,
                        i["content"] as String,
                        i["star"] as Boolean,
                        i["archive"] as Boolean,
                        i["trash"] as Boolean,
                        false,
                        i["timestamp"] as Timestamp
                )
                allNotes.add(note)
            }
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
        hashMap["edited"] = note.edited
        hashMap["timestamp"] = note.timestamp as Timestamp

        return hashMap
    }
}