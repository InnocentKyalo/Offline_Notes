package kyalo.innocent.offlinenotes.binding_adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kyalo.innocent.offlinenotes.adapters.BookmarksAdapter
import kyalo.innocent.roomdb.db.Note

@BindingAdapter("bookmarksListBinding")
fun setBookmarksList(recyclerView: RecyclerView?, list: MutableList<Note>?) {
    // check if the list is null
    if (list == null)
        return

    // check if the layout manager is null
    val layoutManager: RecyclerView.LayoutManager? = recyclerView?.layoutManager
    if (layoutManager == null) {
        if (recyclerView != null) {
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    // check if adapter is null
    var bookmarksAdapter: BookmarksAdapter? = recyclerView?.adapter as BookmarksAdapter?
    if (bookmarksAdapter == null) {
        bookmarksAdapter = recyclerView?.context?.let { BookmarksAdapter(list, it) }
        recyclerView?.adapter = bookmarksAdapter
    }
}