package kyalo.innocent.offlinenotes.binding_adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kyalo.innocent.offlinenotes.adapters.NotesListAdapter
import kyalo.innocent.roomdb.db.Note

@BindingAdapter("notesListBinding" )
fun setNotesToRecycler(recycler: RecyclerView, list: List<Note>?) {

    // check if the list is null
    if(list == null)
        return

    //check if the layout manager is null
    val layoutManager: RecyclerView.LayoutManager? = recycler.layoutManager
    if (layoutManager ==  null)
        recycler.layoutManager =  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    // check if adapter is null
    var listAdapter : NotesListAdapter? = recycler.adapter as NotesListAdapter?
    if (listAdapter == null) {
        listAdapter = NotesListAdapter(list)
        recycler.adapter = listAdapter
    }
}