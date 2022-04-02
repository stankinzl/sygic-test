package com.kinzlstanislav.sigyctest

import androidx.multidex.MultiDexApplication
import com.kinzlstanislav.sigyctest.core.extensions.processLifecycleOwner
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.DatabaseModuleModule
import org.koin.ksp.generated.NetworkModuleModule
import org.koin.ksp.generated.defaultModule
import timber.log.Timber

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
           modules(defaultModule, NetworkModuleModule, DatabaseModuleModule)
        }.androidContext(this@App)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}