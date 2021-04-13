package kyalo.innocent.offlinenotes.ui.add_note

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.note_bottom_dialog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kyalo.innocent.offlinenotes.R
import kyalo.innocent.offlinenotes.utils.BOTTOM_SHEET_INTENT_KEY
import kyalo.innocent.offlinenotes.utils.BROADCAST_KEY
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.getAllNotesDatabase
import kotlin.coroutines.CoroutineContext

class NoteBottomSheetFragment(override val coroutineContext: CoroutineContext) : BottomSheetDialogFragment(),
        CoroutineScope {

    var bottomSheetNote: Note? = null
    private lateinit var job: Job

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        job = Job()

        return inflater.inflate(R.layout.note_bottom_dialog, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dismiss()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments.let {
            bottomSheetNote = it?.let { it1 -> NoteBottomSheetFragmentArgs.fromBundle(it1).bottomSheetNote }
        }

        context?.let { Toasty.info(it, bottomSheetNote?.title.toString(), Toasty.LENGTH_LONG).show() }

        delete_layout.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.nav_home)
            dismiss()
        }

       NavHostFragment.findNavController(this).previousBackStackEntry?.savedStateHandle?.set("key", "Delete)")
    }

    // Logic to delete a note
    private fun deleteNote() {
        launch(Dispatchers.IO) {

            context?.let { getAllNotesDatabase(it).getDao().deleteNote(bottomSheetNote!!) }

            val navigateBackAction = AddNoteFragmentDirections.actionSaveNote()
            Navigation.findNavController(requireView()).navigate(navigateBackAction)
        }
    }
}