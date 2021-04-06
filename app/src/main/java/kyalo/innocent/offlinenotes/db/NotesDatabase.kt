package kyalo.innocent.roomdb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun getDao(): NoteDao
}

// initialize the database
private lateinit var INSTANCE: NotesDatabase

fun getAllNotesDatabase(context: Context): NotesDatabase {

    // Run inside synchronized block to ensure it's thread safe
    synchronized(NotesDatabase::class.java) {

        if(!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NotesDatabase::class.java,
                "notes_db"
            ).build()
        }

        return INSTANCE
    }
}


