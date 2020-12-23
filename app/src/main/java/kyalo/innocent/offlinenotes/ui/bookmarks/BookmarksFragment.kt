package kyalo.innocent.offlinenotes.ui.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kyalo.innocent.offlinenotes.R
import kyalo.innocent.offlinenotes.databinding.FragmentBookmarksBinding
import kyalo.innocent.offlinenotes.utils.BaseFragment


class BookmarksFragment : BaseFragment() {

    private lateinit var bookmarksBinding: FragmentBookmarksBinding
    private lateinit var bookmarksViewModel: BookmarksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        bookmarksBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmarks, container, false)
        bookmarksViewModel = ViewModelProvider(this).get(BookmarksViewModel::class.java)

        // Observe the list of bookmarks
        bookmarksViewModel.bookmarksList.observe(viewLifecycleOwner, { bList ->
            bookmarksBinding.listOfBookmarks = bList
        })

        return bookmarksBinding.root

    }
}