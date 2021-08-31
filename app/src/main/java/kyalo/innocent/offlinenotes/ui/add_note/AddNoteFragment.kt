package kyalo.innocent.offlinenotes.ui.add_note

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.github.dhaval2404.colorpicker.util.ColorUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch
import kyalo.innocent.offlinenotes.R
import kyalo.innocent.offlinenotes.databinding.FragmentAddNoteBinding
import kyalo.innocent.offlinenotes.utils.BaseFragment
import kyalo.innocent.roomdb.db.Note
import kyalo.innocent.roomdb.db.getAllNotesDatabase


class AddNoteFragment : BaseFragment() {

    private lateinit var fAddNoteBinding: FragmentAddNoteBinding
    private lateinit var addNoteViewModel: AddNoteViewModel
    private var localNote: Note? = null
    private var mMaterialColorCircle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this.isEnabled = true
                saveNote()
            }
        })

        // Hide the toolbar
        //(activity as AppCompatActivity?)!!.getSupportActionBar()!!.hide()
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

        tripleDots.setOnClickListener{

            showBottomSheetDialog()
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

    // Logic to save note
    private fun saveNote() {
        val noteTitle = fAddNoteBinding.edtNoteTitle.text.toString().trim()
        val noteContent = fAddNoteBinding.edtNoteContent.text.toString().trim()

        launch {
            context?.let {
                val noteTimeStamp = addNoteViewModel.formatTimeToString(addNoteViewModel.getTheCurrentTimeStamp())
                var fNote: Note? = null

                if (!noteTitle.equals("") || !noteContent.equals("")) {
                    fNote = Note(noteTitle, noteContent, false, noteTimeStamp)
                    //localNote = fNote
                }

                if(localNote == null) {
                    if ((fNote?.title != null) || (fNote?.note == null))
                        addNoteViewModel.saveNoteInBackground(fNote)
                    //it.success("Note Saved")
                } else {
                    fNote?.noteID = localNote!!.noteID
                    fNote.let { it1 -> it1?.let { it2 -> getAllNotesDatabase(it).getDao().updateNote(it2) } }
                }

                // Force the edit texts to close
                edt_note_title.clearFocus()
                edt_note_content.clearFocus()

                val navigateBackAction = AddNoteFragmentDirections.actionSaveNote()
                view?.let { it1 -> Navigation.findNavController(it1).navigate(navigateBackAction) }
            }
        }
    }

    // Logic to delete a note
    private fun deleteNote() {
        localNote?.let { context?.let { it1 -> addNoteViewModel.deleteNote(it, it1) } }
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

    // Display the Bottom Sheet & Handle onClickListeners
    private fun showBottomSheetDialog() {

        val bottomSheetDialog: BottomSheetDialog? = context?.let { BottomSheetDialog(it) }
        bottomSheetDialog?.setContentView(R.layout.note_bottom_dialog)

        val deleteLayout: LinearLayout? = bottomSheetDialog?.findViewById(R.id.delete_layout)
        val shareLayout: LinearLayout? = bottomSheetDialog?.findViewById(R.id.share_layout)
        val chooseColorLayout: LinearLayout? =
            bottomSheetDialog?.findViewById(R.id.choose_color_layout)
        val addNoteLayoutTwo: ConstraintLayout? =
            bottomSheetDialog?.findViewById(R.id.add_note_layout_two)

        chooseColorLayout?.setOnClickListener {
            MaterialColorPickerDialog
                .Builder(requireActivity())
                .setColorSwatch(ColorSwatch._500)
                .setDefaultColor(mMaterialColorCircle)
                .setColorListener(object : ColorListener {
                    override fun onColorSelected(color: Int, colorHex: String) {
                        mMaterialColorCircle = colorHex
                        add_note_layout.setBackgroundColor(color)
                        setButtonBackground(color)
                        edt_note_title.setBackgroundColor(color)
                        edt_note_content.setBackgroundColor(color)
                        chooseColorLayout.setBackgroundColor(color)
                        shareLayout?.setBackgroundColor(color)
                        deleteLayout?.setBackgroundColor(color)
                        addNoteLayoutTwo?.setBackgroundColor(color)
                    }
                })
                .setDismissListener {
                    Log.d("MaterialDialogPicker", "Handle dismiss event")
                }
                .show()
        }

        bottomSheetDialog?.show()

        deleteLayout?.setOnClickListener {
            if (localNote != null)
                deleteNote()
            navigateToHomeAndDismissBottomSheet(bottomSheetDialog)
        }

        shareLayout?.setOnClickListener {

            if (!localNote?.title.equals("") || !localNote?.note.equals("")) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${localNote?.title}\n\n" +
                            "${localNote?.note}\n\n" +
                            "Last Edited: ${localNote?.timeStamtp}")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    // Navigate to Home Fragment & dismiss bottom sheet
    private fun navigateToHomeAndDismissBottomSheet(bottomSheetDialog: BottomSheetDialog) {
        val navigateBackAction = AddNoteFragmentDirections.actionSaveNote()
        Navigation.findNavController(requireView()).navigate(navigateBackAction)
        bottomSheetDialog.dismiss()
    }

    private fun setButtonBackground(color: Int) {
        if (ColorUtil.isDarkColor(color)) {
            edt_note_title.setTextColor(Color.WHITE)
            edt_note_content.setTextColor(Color.WHITE)
        } else {
            edt_note_title.setTextColor(Color.BLACK)
            edt_note_content.setTextColor(Color.BLACK)
        }
        edt_note_title.backgroundTintList = ColorStateList.valueOf(color)
        edt_note_content.backgroundTintList = ColorStateList.valueOf(color)
    }
}