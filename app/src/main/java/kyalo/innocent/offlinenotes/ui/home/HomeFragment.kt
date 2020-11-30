package kyalo.innocent.offlinenotes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.coroutines.launch
import kyalo.innocent.offlinenotes.databinding.FragmentHomeBinding
import kyalo.innocent.offlinenotes.utils.BaseFragment
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.NotesDatabase


class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel

    private var fBinding: FragmentHomeBinding? = null

    private var fNotesList: List<Note>? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        fBinding = FragmentHomeBinding.inflate(inflater)

        setUpNotesList()

        fBinding!!.fabCreateNote.setOnClickListener {
             val action = HomeFragmentDirections.actionCreateNote()
            Navigation.findNavController(it).navigate(action)
        }

        return fBinding!!.root
    }

    private fun setUpNotesList() {
        launch {
            context?.let {
                val localList: List<Note> = NotesDatabase(it).getDao().getAllNotes()
                fBinding?.notes = localList
                fNotesList = localList
            }
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        // Initialize menu item
        val menuItem : MenuItem = menu.findItem(R.id.action_search)

        // Initialize the search view
        val searchView: SearchView = MenuItemCompat.getActionView(menuItem) as SearchView

        searchView.setActivated(true);
        searchView.setQueryHint("Search note");
        searchView.onActionViewExpanded();
        searchView.setIconified(true);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

               *//* adapter = NotesListAdapter(fNotesList!!)
                adapter.getFilter().filter(newText)*//*

                launch{
                    val localList: List<Note> = NotesDatabase(context!!).getDao().getAllNotes()

                    val filteredList = localList.filter { it.title.contains(newText.toString()) ||
                    it.note.contains(newText.toString())}

                    fBinding?.setNotes(filteredList)
                    fNotesList = filteredList
                }

                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
}