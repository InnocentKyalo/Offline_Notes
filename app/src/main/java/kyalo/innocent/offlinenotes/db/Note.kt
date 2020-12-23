package kyalo.innocent.roomdb.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(

        val title: String,
        val note: String,
        var isBookmarked: Boolean = false
) : Serializable{
    @PrimaryKey(autoGenerate = true)
    var noteID : Int = 0
}