package com.archrahkshi.spotifine.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: Login Activity
        buttonProceed.setOnClickListener {
            startActivity(Intent(this, MusicLibraryActivity::class.java))
        }
    }
}