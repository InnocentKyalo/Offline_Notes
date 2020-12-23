package kyalo.innocent.offlinenotes.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.NotesDatabase

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val context = application.applicationContext

    // Notes List
    private val _notesList = MutableLiveData<List<Note>>()
    val notesList: LiveData<List<Note>>
        get() = _notesList

    init {
        viewModelScope.launch {
            _notesList.value = getNotesInBackground()
        }
    }

    // Get the notes in the background
    suspend fun getNotesInBackground(): List<Note> {
        return NotesDatabase(context).getDao().getAllNotes()
    }

}