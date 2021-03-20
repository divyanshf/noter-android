package com.example.noter.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

data class Note(
        var id: String?,
        var title: String?,
        var content: String?,
        var starred:Boolean,
        var archived:Boolean,
        var trash:Boolean,
        var edited:Boolean,
        var timestamp: Timestamp?
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readParcelable(Timestamp::class.java.classLoader)) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(id)
                parcel.writeString(title)
                parcel.writeString(content)
                parcel.writeByte(if (starred) 1 else 0)
                parcel.writeByte(if (archived) 1 else 0)
                parcel.writeByte(if (trash) 1 else 0)
                parcel.writeByte(if (edited) 1 else 0)
                parcel.writeParcelable(timestamp, flags)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Note> {
                override fun createFromParcel(parcel: Parcel): Note {
                        return Note(parcel)
                }

                override fun newArray(size: Int): Array<Note?> {
                        return arrayOfNulls(size)
                }
        }
}
