package com.archrahkshi.spotifine.data.factories

import android.content.Context
import com.archrahkshi.spotifine.data.providerImpls.UserPreferencesImpl

class UserPreferencesFactory private constructor() {
    companion object {
        var instance: UserPreferencesImpl? = null
        fun provide(context: Context) = UserPreferencesImpl(context).also {
            instance = it
        }
    }
}
