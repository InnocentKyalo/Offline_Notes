package kyalo.innocent.offlinenotes.ui.add_note

import android.app.Application
import android.content.Context
import android.text.format.DateFormat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.NotesDatabase
import kyalo.innocent.roomdb.db.getAllNotesDatabase
import java.util.*

class AddNoteViewModel(application: Application) : AndroidViewModel(application) {
    private val context by lazy { application.applicationContext }

    // Logic to save note in database
    suspend fun saveNoteInBackground(note: Note?) {

        viewModelScope.launch {
            if (note?.title != null) {
                getAllNotesDatabase(context).getDao().saveNote(note)
            }
        }
    }

    // Get the current time stamp
    fun getTheCurrentTimeStamp(): Long{
        return System.currentTimeMillis()
    }

    // Format time to String
    fun formatTimeToString(time: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = time * 1000L
        val date = DateFormat.format("dd-MM-yyyy", calendar).toString()
        return date
    }

    fun deleteNote(note: Note, context: Context) {
        viewModelScope.launch {
            context?.let { getAllNotesDatabase(context).getDao().deleteNote(note!!) }
        }
    }
}