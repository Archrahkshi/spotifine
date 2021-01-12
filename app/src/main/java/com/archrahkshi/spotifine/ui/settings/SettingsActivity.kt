package com.archrahkshi.spotifine.ui.settings

import android.app.Instrumentation
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.ui.MainActivity
import com.archrahkshi.spotifine.ui.commonViews.IFullscreenMode
import com.archrahkshi.spotifine.ui.commonViews.IToolbar
import com.archrahkshi.spotifine.ui.commonViews.presenters.FullscreenModePresenter
import com.archrahkshi.spotifine.ui.settings.views.IFullscreenModeCheckbox
import com.archrahkshi.spotifine.ui.settings.views.ILanguageSpinner
import com.archrahkshi.spotifine.ui.settings.views.presenters.FullscreenModeCheckboxPresenter
import com.archrahkshi.spotifine.ui.settings.views.presenters.LanguageSpinnerPresenter
import com.archrahkshi.spotifine.ui.settings.views.presenters.ToolbarPresenter
import com.archrahkshi.spotifine.util.CANCELLATION_PROHIBITED
import com.archrahkshi.spotifine.util.ENGLISH_IS_NOT_SELECTED
import com.archrahkshi.spotifine.util.ENGLISH_IS_SELECTED
import com.archrahkshi.spotifine.util.INDEX_0
import com.archrahkshi.spotifine.util.INDEX_1
import com.archrahkshi.spotifine.util.LABEL_ENGLISH
import com.archrahkshi.spotifine.util.LABEL_RUSSIAN
import kotlinx.android.synthetic.main.activity_settings.checkBoxFullscreen
import kotlinx.android.synthetic.main.activity_settings.imageViewFlag
import kotlinx.android.synthetic.main.activity_settings.spinnerLang
import kotlinx.android.synthetic.main.activity_settings.textViewExit
import kotlinx.android.synthetic.main.toolbar.imageViewBack
import kotlinx.android.synthetic.main.toolbar.imageViewSettings
import kotlinx.android.synthetic.main.toolbar.textViewToolbarText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsActivity :
    AppCompatActivity(),
    IToolbar,
    ILanguageSpinner,
    IFullscreenModeCheckbox,
    IFullscreenMode {
    private val languageSpinnerPresenter by lazy { LanguageSpinnerPresenter(this) }
    private val fullscreenModeCheckboxPresenter by lazy { FullscreenModeCheckboxPresenter(this) }
    private val fullscreenModePresenter by lazy { FullscreenModePresenter(this) }
    private val toolbarPresenter by lazy { ToolbarPresenter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullscreenModePresenter.setSelectionFullscreenMode()
        setContentView(R.layout.activity_settings)

        spinnerLang.adapter = ArrayAdapter(
            applicationContext,
            R.layout.support_simple_spinner_dropdown_item,
            listOf(LABEL_ENGLISH, LABEL_RUSSIAN)
        )

        languageSpinnerPresenter.setSelectedLanguage()
        fullscreenModeCheckboxPresenter.setSelectedFullscreenMode()
        toolbarPresenter.setupToolbar()

        imageViewBack.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
            }
        }

        /**
         * Spinner bug: the list selects the zero position during initialization.
         * To avoid this, we have to ignore this
         */
        var i = 0
        spinnerLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                i++
                if (i > 1) when (position) {
                    INDEX_0 -> languageSpinnerPresenter.setLanguage(ENGLISH_IS_SELECTED)
                    INDEX_1 -> languageSpinnerPresenter.setLanguage(ENGLISH_IS_NOT_SELECTED)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Timber.i("Our Detekt, who art in heaven, hallowed be thy name!")
            }
        }

        checkBoxFullscreen.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(R.string.message_restart)
                .setCancelable(CANCELLATION_PROHIBITED)
                .setPositiveButton(R.string.restart) { _, _ ->
                    fullscreenModeCheckboxPresenter.setFullscreenMode(checkBoxFullscreen.isChecked)
                    startActivity(
                        Intent(applicationContext, MainActivity::class.java).apply {
                            addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS).addFlags(
                                FLAG_ACTIVITY_CLEAR_TASK
                            ).addFlags(FLAG_ACTIVITY_NEW_TASK)
                        }
                    )
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    fullscreenModeCheckboxPresenter.setSelectedFullscreenMode()
                }
                .create()
                .show()
        }

        textViewExit.setOnClickListener {
            startActivity(
                Intent(applicationContext, MainActivity::class.java).apply {
                    addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                    addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
    }

    /**
     * Toolbar implementation
     */

    override fun setTitle(title: String) {
        textViewToolbarText.text = title
    }

    override fun showBackButton(isShown: Boolean) {
        imageViewBack.visibility = View.VISIBLE
    }

    override fun hideSettingsButton() {
        imageViewSettings.visibility = View.GONE
    }

    /**
     * Language spinner implementation
     */

    override fun setLanguage(isEnglishSelected: Boolean) {
        spinnerLang.setSelection(if (isEnglishSelected) INDEX_0 else INDEX_1)
        imageViewFlag.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                if (isEnglishSelected) R.drawable.england else R.drawable.russia
            )
        )
    }

    /**
     * Fullscreen mode checkbox implementation
     */

    override fun setFullscreenModeCheckboxSelection(isFullscreenModeSelected: Boolean) {
        checkBoxFullscreen.isChecked = isFullscreenModeSelected
    }

    /**
     * Fullscreen mode implementation
     */

    override fun setFullscreenMode(isFullscreenModeSelected: Boolean) {
        setTheme(if (isFullscreenModeSelected) R.style.fullscreen else R.style.spotifine)
    }
}
