package kyalo.innocent.offlinenotes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kyalo.innocent.offlinenotes.BR
import kyalo.innocent.offlinenotes.R
import kyalo.innocent.offlinenotes.databinding.BookmarksLayoutItemBinding
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.NotesDatabase
import kotlin.coroutines.CoroutineContext

class BookmarksAdapter(private var bookmarksList: MutableList<Note>, var context: Context) :
        RecyclerView.Adapter<BookmarksAdapter.BookmarkViewHolder>(), CoroutineScope {

    private var job: Job = Job()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {

        val layoutItemBinding: BookmarksLayoutItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.bookmarks_layout_item,
                parent,
                false)


        return BookmarkViewHolder(layoutItemBinding.root)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmarkNote: Note = bookmarksList.get(position)

        holder.bookmarkBinding?.setVariable(BR.bookmarkItem, bookmarkNote)

        holder.bookmarkBinding?.executePendingBindings()

        holder.bookmarkBinding?.imageRemoveBookmark?.setOnClickListener {
            removeBookmark(bookmarkNote, position)
        }
    }

    override fun getItemCount() = bookmarksList.size

    inner class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var bookmarkBinding: BookmarksLayoutItemBinding? = DataBindingUtil.bind(itemView)
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    // Remove a bookmark
    fun removeBookmark(bookmarkNote: Note, removedPos: Int) {

        bookmarkNote.isBookmarked = false

        launch(Dispatchers.IO) {
            NotesDatabase(context).getDao().updateNote(bookmarkNote)

            withContext(Dispatchers.Main) {
                bookmarksList.removeAt(removedPos)
                notifyItemRemoved(removedPos)
                notifyDataSetChanged()
            }
        }

    }

}