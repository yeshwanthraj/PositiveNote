package com.timepasslabs.positivenote.data

import android.os.Parcelable
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

private const val TAG = "Note"

@Entity(tableName = "notes")
@Parcelize
data class Note (
    var title : String = "",
    var details : String = "",
    var date : Calendar,
    var lastUpdate: Long,
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