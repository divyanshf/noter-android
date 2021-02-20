package com.example.noter.data.model

import android.os.Parcel
import android.os.Parcelable

data class Note(
        var id: String?,
        var title: String?,
        var content: String?,
        var starred:Boolean,
        var archived:Boolean,
        var trash:Boolean,
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(id)
                parcel.writeString(title)
                parcel.writeString(content)
                parcel.writeByte(if (starred) 1 else 0)
                parcel.writeByte(if (archived) 1 else 0)
                parcel.writeByte(if (trash) 1 else 0)
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
