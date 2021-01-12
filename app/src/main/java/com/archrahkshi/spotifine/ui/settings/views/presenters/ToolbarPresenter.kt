package com.archrahkshi.spotifine.ui.settings.views.presenters

import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.ui.commonViews.IToolbar
import com.archrahkshi.spotifine.ui.settings.SettingsActivity

class ToolbarPresenter(private val activity: SettingsActivity, private val viewState: IToolbar) {
    fun setupToolbar() {
        with(viewState) {
            setTitle(activity.getString(R.string.label_settings))
            showBackButton(true)
            hideSettingsButton()
        }
    }
}
