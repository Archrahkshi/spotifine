package com.archrahkshi.spotifine.ui.library.libraryListsFragment

import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.ui.commonViews.ToolBarImpl
import com.archrahkshi.spotifine.util.ALBUMS
import com.archrahkshi.spotifine.util.LIST_TYPE
import com.archrahkshi.spotifine.util.NAME
import kotlinx.android.synthetic.main.toolbar.imgBack
import kotlinx.android.synthetic.main.toolbar.tvTitle

class LibraryListsPresenter(private val fragment: LibraryListsFragment) {
    private val args = fragment.requireArguments()

    private val toolBarImpl by lazy {
        ToolBarImpl(
            fragment.requireActivity().tvTitle,
            fragment.requireActivity().imgBack
        )
    }

    private fun showBackButton() = toolBarImpl.applyBackButton(false)
    private fun hideBackButton() = toolBarImpl.applyBackButton(true)
    private fun setToolbarTitle(title: String) = toolBarImpl.setTitle(title)

    internal fun applyToolbar() {
        if (args.getString(LIST_TYPE) == ALBUMS && args.getString(NAME) != null) {
            setToolbarTitle(args.getString(NAME)!!)
            showBackButton()
        } else {
            setToolbarTitle(fragment.getString(R.string.title_library))
            hideBackButton()
        }
    }
}
