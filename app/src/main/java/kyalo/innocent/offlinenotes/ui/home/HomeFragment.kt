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
import kyalo.innocent.offlinenotes.adapters.NotesListAdapter
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

        val item = menu.findItem(R.id.action_search)
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        item.setTitle("Search your notes")

        val searchView = item.actionView as SearchView?

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null)
                    searchDatabase(query)

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                // Here is where we are going to implement the filter logic
                query?.let { searchDatabase(it) }

                return true
            }
        })
        searchView?.setOnClickListener { }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        fHomeViewModel.searchNotes(searchQuery).observe(viewLifecycleOwner, { usersQuery ->

            //val myAdapter: NotesListAdapter?
            fBinding.notes = usersQuery
//            myAdapter = activity?.applicationContext?.let { NotesListAdapter(usersQuery, it) }
//            fBinding.notesListReycler.adapter?.notifyDataSetChanged()
//            fBinding.notesListReycler.adapter = myAdapter
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
}