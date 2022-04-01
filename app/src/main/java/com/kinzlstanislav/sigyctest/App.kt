package com.kinzlstanislav.sigyctest

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import timber.log.Timber

@Suppress("unused") // it is used in AndroidManifest
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
           modules(defaultModule)
        }.androidContext(this@App)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}