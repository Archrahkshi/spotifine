package com.archrahkshi.spotifine.ui.settings

import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.factories.UserPreferencesFactory
import com.archrahkshi.spotifine.ui.MainActivity
import com.archrahkshi.spotifine.ui.commonViews.IFullscreenMode
import com.archrahkshi.spotifine.ui.commonViews.presenters.FullscreenModePresenter
import com.archrahkshi.spotifine.ui.settings.views.IFullscreenModeCheckbox
import com.archrahkshi.spotifine.ui.settings.views.ILanguageSpinner
import com.archrahkshi.spotifine.ui.settings.views.presenters.FullscreenModeCheckboxPresenter
import com.archrahkshi.spotifine.ui.settings.views.presenters.LanguageSpinnerPresenter
import com.archrahkshi.spotifine.util.CANCELLATION_PROHIBITED
import com.archrahkshi.spotifine.util.ENGLISH_IS_NOT_SELECTED
import com.archrahkshi.spotifine.util.ENGLISH_IS_SELECTED
import com.archrahkshi.spotifine.util.INDEX_0
import com.archrahkshi.spotifine.util.INDEX_1
import com.archrahkshi.spotifine.util.LABEL_ENGLISH
import com.archrahkshi.spotifine.util.LABEL_RUSSIAN
import kotlinx.android.synthetic.main.activity_settings.btnExit
import kotlinx.android.synthetic.main.activity_settings.cbFullscreen
import kotlinx.android.synthetic.main.activity_settings.imgLang
import kotlinx.android.synthetic.main.activity_settings.spinnerLang

class SettingsActivity : AppCompatActivity(), ILanguageSpinner, IFullscreenModeCheckbox, IFullscreenMode {
    private val languageSpinnerPresenter
            by lazy { LanguageSpinnerPresenter(this) }
    private val fullscreenModeCheckboxPresenter
            by lazy { FullscreenModeCheckboxPresenter(this) }
    private val fullscreenModePresenter
            by lazy { FullscreenModePresenter(this) }


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

        /**
         * Spinner bug: the list during initialization selects the zero position,
         * to avoid this, we've to ignore this
         */
        var i = 0
        spinnerLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                i++
                if (i > 1) {
                    when (position) {
                        INDEX_0 -> languageSpinnerPresenter.setLanguage(ENGLISH_IS_SELECTED)
                        INDEX_1 -> languageSpinnerPresenter.setLanguage(ENGLISH_IS_NOT_SELECTED)
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        cbFullscreen.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val alertDialog: AlertDialog

            val dialogOnPositiveClickListener: DialogInterface.OnClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    fullscreenModeCheckboxPresenter.setFullscreenMode(cbFullscreen.isChecked)
                    val intent = Intent(applicationContext, MainActivity::class.java).apply {
                        addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS).
                        addFlags(FLAG_ACTIVITY_CLEAR_TASK).
                        addFlags(FLAG_ACTIVITY_NEW_TASK)
                    }
                    startActivity(intent)
                }

            val dialogOnNegativeClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    fullscreenModeCheckboxPresenter.setSelectedFullscreenMode()
                }

            builder.
            setMessage(R.string.message_restart).
            setCancelable(CANCELLATION_PROHIBITED).
            setPositiveButton(R.string.label_restart, dialogOnPositiveClickListener).
            setNegativeButton(R.string.label_cancel, dialogOnNegativeClickListener)

            alertDialog = builder.create()
            alertDialog.show()
        }

        btnExit.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS).
                addFlags(FLAG_ACTIVITY_CLEAR_TASK).
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }

    /**
     * Language spinner implementation
     */

    override fun setLanguage(isEnglishLanguageSelected: Boolean) {
        spinnerLang.setSelection(if (isEnglishLanguageSelected) INDEX_0 else INDEX_1)
        imgLang.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                if (isEnglishLanguageSelected) R.drawable.england else R.drawable.russia
            )
        )
    }

    /**
     * Fullscreen mode checkbox implementation
     */

    override fun setFullscreenModeCheckboxSelection(isFullscreenModeSelected: Boolean) {
        cbFullscreen.isChecked = isFullscreenModeSelected
    }

    /**
     * Fullscreen mode implementation
     */

    override fun setFullscreenMode(isFullscreenModeSelected: Boolean) {
        if (isFullscreenModeSelected) {
            setTheme(R.style.fullscreen)
        } else {
            setTheme(R.style.spotifine)
        }
    }
}