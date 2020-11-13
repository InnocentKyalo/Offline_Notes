package kyalo.innocent.offlinenotes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.coroutines.launch
import kyalo.innocent.offlinenotes.utils.BaseFragment
import kyalo.innocent.offlinenotes.databinding.FragmentHomeBinding
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.NotesDatabase

class HomeFragment : BaseFragment()  {

    private lateinit var homeViewModel: HomeViewModel
    private var fBinding: FragmentHomeBinding? = null

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
            context?.let{
                val localList: List<Note> = NotesDatabase(it).getDao().getAllNotes()
                fBinding?.setNotes(localList)
            }
        }
    }
}