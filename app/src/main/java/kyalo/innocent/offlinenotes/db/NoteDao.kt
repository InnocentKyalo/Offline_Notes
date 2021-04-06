package kyalo.innocent.roomdb.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    suspend fun saveNote(note: Note)

    @Query("SELECT * FROM note ORDER BY noteID DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE isBookmarked")
    suspend fun getBookmarkedNotes(): MutableList<Note>

    // add multiple notes
    @Insert
    suspend fun addMultipleNotes(vararg note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note WHERE title || note LIKE :searchQuery")
    fun searchNote(searchQuery: String): LiveData<List<Note>>
}