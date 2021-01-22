package com.timepasslabs.positivenote.data

import android.os.Parcelable
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

private const val TAG = "Note"

@Entity(tableName = "notes")
@Parcelize
data class Note (
    val title : String = "",
    val details : String = "",
    val date : String,
    val lastUpdate: Long,
    @PrimaryKey(autoGenerate = true) val id : Int = 0
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        val isEqual = (other is Note) && this.id == other.id
        Log.d(TAG, "equals: notes are $isEqual")
        return isEqual
    }

    override fun hashCode(): Int {
        return id
    }

}