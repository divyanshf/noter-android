package com.example.noter.data.repository

import com.example.noter.data.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
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
}