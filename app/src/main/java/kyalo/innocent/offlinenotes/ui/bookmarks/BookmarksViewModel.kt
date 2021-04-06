package kyalo.innocent.offlinenotes.ui.bookmarks

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.NotesDatabase
import kyalo.innocent.roomdb.db.getAllNotesDatabase

class BookmarksViewModel(application: Application) : AndroidViewModel(application) {

    val myContext: Context by lazy { application.applicationContext }

    private var _bookmarksList = MutableLiveData<List<Note>>()

    val bookmarksList: LiveData<List<Note>>
        get() = _bookmarksList

    init {
        viewModelScope.launch {
            _bookmarksList.value = getNotes()
        }
    }


    suspend fun getNotes(): MutableList<Note> = getAllNotesDatabase(myContext)
            .getDao().getBookmarkedNotes()

}