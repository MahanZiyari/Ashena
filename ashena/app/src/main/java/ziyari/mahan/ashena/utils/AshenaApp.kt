package ziyari.mahan.ashena.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AshenaApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant()
    }
}