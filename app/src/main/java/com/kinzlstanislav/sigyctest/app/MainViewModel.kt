package com.kinzlstanislav.sigyctest.app

import androidx.lifecycle.ViewModel
import com.kinzlstanislav.sigyctest.app.database.CatsDao
import com.kinzlstanislav.sigyctest.core.extensions.coroutine
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class MainViewModel : ViewModel()