package com.archrahkshi.spotifine.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
    
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutPlayer,
            LyricsFragment(false)
        ).commit()
    }
}