package com.example.noter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "note")
data class Note(
        @PrimaryKey @NotNull val id:String,
        val title:String,
        val content:String,
        val starred:Boolean,
        val archived:Boolean,
        val trash:Boolean
)
