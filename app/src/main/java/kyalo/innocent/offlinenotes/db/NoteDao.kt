package kyalo.innocent.roomdb.db

import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    suspend fun saveNote(note : Note)

    @Query("SELECT * FROM note ORDER BY noteID DESC")
    suspend fun getAllNotes() : List<Note>

    // add multiple notes
    @Insert
    suspend fun addMultipleNotes(vararg note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note : Note)
}