package kyalo.innocent.offlinenotes.models

import android.view.View
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import kyalo.innocent.offlinenotes.R
import kyalo.innocent.offlinenotes.databinding.BookmarksLayoutItemBinding
import kyalo.innocent.roomdb.db.Note

class NotesItem : BindableItem<BookmarksLayoutItemBinding>() {

    val note: Note? = null

    override fun getLayout(): Int {
        return R.layout.bookmarks_layout_item
    }

    override fun initializeViewBinding(view: View): BookmarksLayoutItemBinding {
        return BookmarksLayoutItemBinding.bind(view)
    }

    override fun bind(viewBinding: BookmarksLayoutItemBinding, position: Int) {
        viewBinding.bookmarkItem = note
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

}