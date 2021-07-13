package com.timepasslabs.positivenote.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Note (
    var title : String = "",
    var details : String = "",
    var timestamp : Long,
    var lastUpdate: Long,
    @PrimaryKey(autoGenerate = true) val id : Long = 0
) : Parcelable {



    override fun equals(other: Any?): Boolean {
        return (other is Note) && id == other.id
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + details.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + lastUpdate.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }

}