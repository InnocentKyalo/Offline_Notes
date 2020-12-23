package kyalo.innocent.offlinenotes.ui.add_note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.NotesDatabase

class AddNoteViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    suspend fun saveNoteInBackground(note: Note?) {

        viewModelScope.launch {
            if (note?.title != null) {
                NotesDatabase(context).getDao().saveNote(note)
            }
        }
    }
}