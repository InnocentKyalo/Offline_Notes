package kyalo.innocent.offlinenotes.ui.add_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.note_bottom_dialog.*
import kyalo.innocent.offlinenotes.R
import kyalo.innocent.offlinenotes.adapters.NotesListAdapter


class NoteBottomSheetFragment : BottomSheetDialogFragment() {


    private val isBottomSheetVisible: Boolean = true
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {



        return inflater.inflate(R.layout.note_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       /* goHome?.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.nav_home)
            dismiss()
        }*/
        delete_layout.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.nav_home)
            dismiss()
        }

    }
}