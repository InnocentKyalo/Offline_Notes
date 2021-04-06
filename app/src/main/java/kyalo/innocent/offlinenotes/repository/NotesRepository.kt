package kyalo.innocent.offlinenotes.repository

import androidx.lifecycle.LiveData
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.NotesDatabase

class NotesRepository(private val notesDatabase: NotesDatabase) {

    val listOfNotes: LiveData<List<Note>> = notesDatabase.getDao().getAllNotes()
}