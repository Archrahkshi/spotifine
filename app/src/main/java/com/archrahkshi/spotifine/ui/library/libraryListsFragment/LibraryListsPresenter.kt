package com.archrahkshi.spotifine.ui.library.libraryListsFragment

import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.ui.commonViews.ToolbarImpl
import com.archrahkshi.spotifine.util.ALBUMS
import com.archrahkshi.spotifine.util.LIST_TYPE
import com.archrahkshi.spotifine.util.NAME
import kotlinx.android.synthetic.main.toolbar.imageViewBack
import kotlinx.android.synthetic.main.toolbar.textViewToolbarText

class LibraryListsPresenter(private val fragment: LibraryListsFragment) {
    private val args = fragment.requireArguments()

    private val toolBarImpl by lazy {
        ToolbarImpl(
            fragment.requireActivity().textViewToolbarText,
            fragment.requireActivity().imageViewBack
        )
    }

    private fun showBackButton() = toolBarImpl.applyBackButton(false)
    private fun hideBackButton() = toolBarImpl.applyBackButton(true)
    private fun setToolbarTitle(title: String) = toolBarImpl.setTitle(title)

    internal fun applyToolbar() {
        val name = args.getString(NAME)
        if (args.getString(LIST_TYPE) == ALBUMS && name != null) {
            setToolbarTitle(name)
            showBackButton()
        } else {
            setToolbarTitle(fragment.getString(R.string.title_library))
            hideBackButton()
        }
    }
}
