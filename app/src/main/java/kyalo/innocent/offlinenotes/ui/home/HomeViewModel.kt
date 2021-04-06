package kyalo.innocent.offlinenotes.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kyalo.innocent.offlinenotes.repository.NotesRepository
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.getAllNotesDatabase

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val notesDatabase = getAllNotesDatabase(application.applicationContext)
    private val notesRepository: NotesRepository = NotesRepository(notesDatabase)

    // Notes List
    val notesList = notesRepository.listOfNotes

    init {
        viewModelScope.launch {

        }
    }

    // Get all Notes from DB
    private fun refreshNotesList() {
    }

    // Query database for notes
    fun searchNotes(searchQuery: String): LiveData<List<Note>> = notesDatabase.getDao().searchNote(searchQuery)


}