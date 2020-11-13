package kyalo.innocent.offlinenotes.ui.add_note

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch
import kyalo.innocent.offlinenotes.utils.BaseFragment
import kyalo.innocent.offlinenotes.R
import kyalo.innocent.offlinenotes.databinding.FragmentAddNoteBinding
import kyalo.innocent.offlinenotes.utils.success
import kyalo.innocent.offlinenotes.ui.AddNoteFragmentArgs
import kyalo.innocent.offlinenotes.ui.AddNoteFragmentDirections
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.NotesDatabase

class AddNoteFragment : BaseFragment() {

    private lateinit var fAddNoteBinding : FragmentAddNoteBinding

    private var localNote : Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fAddNoteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_note, container, false)

        setHasOptionsMenu(true)

        return fAddNoteBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments.let {
            localNote = AddNoteFragmentArgs.fromBundle(it!!).MyNote
            edt_note_title.setText(localNote?.title)
            edt_note_content.setText(localNote?.note)
        }

        val fabAddNote : FloatingActionButton = fab_save_note
        fabAddNote.setOnClickListener { view ->

            val noteTitle = edt_note_title.text.toString().trim()
            val noteContent = edt_note_content.text.toString().trim()

            if (noteTitle.isEmpty()) {
                edt_note_title.error = "Note title is empty"
                edt_note_title.requestFocus()
                return@setOnClickListener
            }

            if (noteContent.isEmpty()) {
                edt_note_content.error = "Note content is empty"
                edt_note_content.requestFocus()
                return@setOnClickListener
            }

            launch {
                context?.let {
                    val fNote = Note(noteTitle, noteContent)

                    if(localNote == null) {
                        NotesDatabase(it).getDao().saveNote(fNote)
                        it.success("Note Saved")
                    } else {
                        fNote.noteID = localNote!!.noteID
                        NotesDatabase(it).getDao().updateNote(fNote)
                        it.success("Note Updated")
                    }

                    val navigateBackAction = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view).navigate(navigateBackAction)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note_delete, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {

            R.id.action_delete -> {

                if(localNote != null)
                    deleteNote()
                else {
                    val navigateBackAction = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(requireView()).navigate(navigateBackAction)
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteNote() {

        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("This note will be deleted permanently")
            setPositiveButton("OK") {_,_ ->

                launch {

                    NotesDatabase(context).getDao().deleteNote(localNote!!)

                    val navigateBackAction = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(requireView()).navigate(navigateBackAction)
                }
            }
            setNegativeButton("Cancel") {_,_ ->

            }
        }.create().show()
    }
}