package kyalo.innocent.offlinenotes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kyalo.innocent.offlinenotes.BR
import kyalo.innocent.offlinenotes.R
import kyalo.innocent.offlinenotes.databinding.LayoutItemBinding
import kyalo.innocent.offlinenotes.ui.home.HomeFragmentDirections
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.NotesDatabase
import kotlin.coroutines.CoroutineContext


class NotesListAdapter(private var notesList: List<Note>, private var context: Context)
    : RecyclerView.Adapter<NotesListAdapter.NoteViewHolder>(), CoroutineScope {

    private lateinit var job: Job

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        val binding: LayoutItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_item, parent, false)

        job = Job()

        return NoteViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        //get the current note to be displayed
        val displayedNote: Note = notesList[position]

        holder.noteBinding!!.setVariable(BR.noteItem, displayedNote)
        holder.noteBinding!!.executePendingBindings()

        if (displayedNote.isBookmarked)
            holder.noteBinding?.imageBookmark?.setImageResource(R.drawable.ic_bookmark_active)
        else
            holder.noteBinding?.imageBookmark?.setImageResource(R.drawable.ic_bookmark_inactive)

        // add on note click listener
        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionCreateNote()
            action.note = displayedNote
            Navigation.findNavController(it).navigate(action)
        }

        var bookmarkState = displayedNote.isBookmarked

        holder.noteBinding!!.imageBookmark.setOnClickListener {

            if (bookmarkState) {
                holder.noteBinding!!.imageBookmark.setImageResource(R.drawable.ic_bookmark_inactive)
                bookmarkState = false

                launch(Dispatchers.IO) {
                    updateDatabase(displayedNote, bookmarkState)
                }


            } else {
                holder.noteBinding!!.imageBookmark.setImageResource(R.drawable.ic_bookmark_active)
                bookmarkState = true

                launch(Dispatchers.IO) {
                    updateDatabase(displayedNote, bookmarkState)
                }

            }
        }
    }

    private suspend fun updateDatabase(displayedNote: Note, bookmarkState: Boolean) {

        if (bookmarkState) {
            displayedNote.isBookmarked = true
            NotesDatabase(context).getDao().updateNote(displayedNote)
        } else {
            displayedNote.isBookmarked = false
            NotesDatabase(context).getDao().updateNote(displayedNote)
        }
    }

    override fun getItemCount(): Int = notesList.size

    // Note View Holder class
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteBinding: LayoutItemBinding?

        init {
            noteBinding = DataBindingUtil.bind(itemView)
        }

    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

}
