package kyalo.innocent.offlinenotes.ui.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.coroutines.launch
import kyalo.innocent.offlinenotes.R
import kyalo.innocent.offlinenotes.databinding.FragmentHomeBinding
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.NotesDatabase


class HomeFragment : Fragment() {

    private lateinit var fHomeViewModel: HomeViewModel
    private lateinit var fBinding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        fHomeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        fBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        fHomeViewModel.notesList.observe(viewLifecycleOwner, { list ->
            fBinding.notes = list
        })

        fBinding.fabCreateNote.setOnClickListener {
            val action = HomeFragmentDirections.actionCreateNote()
            Navigation.findNavController(it).navigate(action)
        }

        return fBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        // Initialize menu item
        val menuItem: MenuItem = menu.findItem(R.id.action_search)

        // Initialize the search view
        val searchView: SearchView = MenuItemCompat.getActionView(menuItem) as SearchView

        searchView.isActivated = true
        searchView.queryHint = "Search note"
        searchView.onActionViewExpanded()
        searchView.isIconified = true
        searchView.clearFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                viewLifecycleOwner.lifecycleScope.launch {
                    val localList: List<Note> = NotesDatabase(context!!).getDao().getAllNotes()

                    val filteredList = localList.filter {
                        it.title.contains(newText.toString()) ||
                                it.note.contains(newText.toString())
                    }

                    fBinding.notes = filteredList
                }

                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
}