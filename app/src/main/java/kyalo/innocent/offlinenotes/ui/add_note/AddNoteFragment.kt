package kyalo.innocent.offlinenotes.ui.add_note

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import kotlinx.coroutines.launch
import kyalo.innocent.offlinenotes.R
import kyalo.innocent.offlinenotes.databinding.FragmentAddNoteBinding
import kyalo.innocent.offlinenotes.utils.BOTTOM_SHEET_INTENT_KEY
import kyalo.innocent.offlinenotes.utils.BROADCAST_KEY
import kyalo.innocent.offlinenotes.utils.BaseFragment
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.getAllNotesDatabase


class AddNoteFragment : BaseFragment() {

    private lateinit var fAddNoteBinding: FragmentAddNoteBinding
    private lateinit var addNoteViewModel: AddNoteViewModel
    private var localNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this.isEnabled = true
                saveNote()
            }
        })
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fAddNoteBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_note,
                container,
                false)


        addNoteViewModel = ViewModelProvider(this).get(AddNoteViewModel::class.java)

        setHasOptionsMenu(true)

        return fAddNoteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tripleDots: ImageButton = view.findViewById(R.id.triple_dots_action)
        val notesBottomSheet = NoteBottomSheetFragment(coroutineContext)

        tripleDots.setOnClickListener{
            notesBottomSheet.bottomSheetNote = localNote
            notesBottomSheet.show(parentFragmentManager, "BottomFragment")
        }

        // Get Note arguments from previous fragment
        arguments.let {
            localNote = AddNoteFragmentArgs.fromBundle(it!!).note
            fAddNoteBinding.edtNoteTitle.setText(localNote?.title)
            fAddNoteBinding.edtNoteContent.setText(localNote?.note)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note_delete, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {

            R.id.action_delete -> {

                if (localNote != null)
                    deleteNote()
                else {
                    val navigateBackAction = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(requireView()).navigate(navigateBackAction)
                }

            }

            /*R.id.action_bookmark -> {

                var bookmarked = localNote?.isBookmarked
                //bookmarkMenu.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmark_active)
                if (bookmarked == true) {
                    bookmarkMenu.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmark_active)
                    bookmarked = true
                } else {
                    bookmarkMenu.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmark_inactive)
                    bookmarked = false
                }

                val bookmarkView: View? = view?.findViewById(R.id.action_bookmark)

                bookmarkView?.setOnClickListener {

                }
            }*/
        }
        return super.onOptionsItemSelected(item)
    }

    // Logic to save note
    private fun saveNote() {
        val noteTitle = fAddNoteBinding.edtNoteTitle.text.toString().trim()
        val noteContent = fAddNoteBinding.edtNoteContent.text.toString().trim()

        launch {
            context?.let {
                val noteTimeStamp = addNoteViewModel.formatTimeToString(addNoteViewModel.getTheCurrentTimeStamp())
                var fNote: Note? = null

                if (!noteTitle.equals("") && !noteContent.equals("") && !noteTimeStamp.equals(""))
                    fNote = Note(noteTitle, noteContent, false, noteTimeStamp)

                if(localNote == null) {
                    if (fNote != null)
                        addNoteViewModel.saveNoteInBackground(fNote)
                    //it.success("Note Saved")
                } else {
                    fNote?.noteID = localNote!!.noteID
                    fNote.let { it1 -> it1?.let { it2 -> getAllNotesDatabase(it).getDao().updateNote(it2) } }
                }

                val navigateBackAction = AddNoteFragmentDirections.actionSaveNote()
                view?.let { it1 -> Navigation.findNavController(it1).navigate(navigateBackAction) }
            }
        }
    }

    // Logic to delete a note
    private fun deleteNote() {

        AlertDialog.Builder(requireContext()).apply {
            setTitle("Are you sure?")
            setMessage("This note will be deleted permanently")
            setPositiveButton("OK") { _, _ ->

                launch {

                    getAllNotesDatabase(context).getDao().deleteNote(localNote!!)

                    val navigateBackAction = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(requireView()).navigate(navigateBackAction)
                }
            }
            setNegativeButton("Cancel") { _, _ ->

            }
        }.create().show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //enable menu
        setHasOptionsMenu(true)

        requireActivity()
                .onBackPressedDispatcher
                .addCallback(this){
                    //true means that the callback is enabled
                    this.isEnabled = true
                    saveNote()
                }
    }

    private val someBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val receivedString = intent.getStringArrayExtra(BOTTOM_SHEET_INTENT_KEY)
            if (receivedString?.equals("delete") == true) {
                deleteNote()
                Toast.makeText(getContext(), receivedString.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(someBroadcastReceiver,
                IntentFilter(BROADCAST_KEY))
        }
    }

    override fun onPause() {
        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(someBroadcastReceiver) }
        super.onPause()
    }
}