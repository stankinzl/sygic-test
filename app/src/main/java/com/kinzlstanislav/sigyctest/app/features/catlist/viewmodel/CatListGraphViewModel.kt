package com.kinzlstanislav.sigyctest.app.features.catlist.viewmodel

import com.kinzlstanislav.sigyctest.app.data.Cat
import com.kinzlstanislav.sigyctest.app.database.CatsDao
import com.kinzlstanislav.sigyctest.app.mappers.CatsMapper
import com.kinzlstanislav.sigyctest.app.network.ApiService
import com.kinzlstanislav.sigyctest.app.network.data.CatResponse
import com.kinzlstanislav.sigyctest.core.extensions.coroutine
import com.kinzlstanislav.sigyctest.core.extensions.isConnectionError
import com.kinzlstanislav.sigyctest.core.extensions.uiCoroutine
import com.kinzlstanislav.sigyctest.core.helpers.NetworkHelper
import com.kinzlstanislav.sigyctest.core.nestednavigation.BaseNestedGraphViewModel
import com.kinzlstanislav.sigyctest.core.nestednavigation.NestedNavigation
import com.kinzlstanislav.sigyctest.core.nestednavigation.NestedNavigation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException

@KoinViewModel
class CatListGraphViewModel(
    private val apiService: ApiService,
    private val catsDao: CatsDao,
    private val catsMapper: CatsMapper,
    private val networkHelper: NetworkHelper
) : BaseNestedGraphViewModel<NestedNavigation>() {

    object NestedNavigationNoCats : NestedNavigation()

    private var upcomingPage = 1

    val catsFlow: Flow<List<Cat>> = catsDao.observeCatEntries().distinctUntilChanged().map {
        catsMapper.map(from = it)
    }
    @Suppress("PropertyName")
    private val _catsRefreshingFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val catsRefreshingFlow: Flow<Boolean> = _catsRefreshingFlow

    init {
        uiCoroutine { // Nuking tables while blocking thread works only on Main
            catsDao.nukeTable() // Doing this on every app launch just to restart the lifecycle of the app and show loading
            fetchNextCatPage()
        }
    }

    private var collectingCatsJob: Job? = null

    private fun startCollectingCatsIfNotAlready() {
        if (collectingCatsJob?.isActive == true) return
        collectingCatsJob = coroutine {
            catsFlow.collectLatest { mappedCats ->
                if (mappedCats.isNotEmpty()) {
                    _nestedNavigationFlow.emit(GenericContent)
                } else {
                    _nestedNavigationFlow.emit(NestedNavigationNoCats)
                }
            }
        }
    }

    fun refreshAllCatPages() {
        coroutine {
            try {
                _catsRefreshingFlow.emit(true)
                if (!networkHelper.isDeviceConnectedToInternet()) {
                    throw ConnectException()
                }
                mutableListOf<Deferred<List<CatResponse?>?>>().apply {
                    for (p in 1 until upcomingPage) {
                        add(async {
                            try {
                                apiService.fetchCats(page = p)
                            } catch (e: Exception) {
                                Timber.e(e)
                                null
                            }
                        })
                    }
                }.awaitAll().filterNotNull().flatten().filterNotNull().let { catsResponse ->
                    catsDao.insertOrUpdate(catsResponse)
                }
            } catch (e: Exception) {
                Timber.e(e)
                handleError(e)
            } finally {
                _catsRefreshingFlow.emit(false)
            }
        }
    }

    private var nextCatPageFetchingJob: Job? = null

    fun fetchNextCatPage() {
        if (nextCatPageFetchingJob?.isActive == true) return // Don't fetch another page while one is being fetched
        nextCatPageFetchingJob = coroutine {
            try {
                if (!networkHelper.isDeviceConnectedToInternet()) {
                    throw ConnectException()
                }
                if (catsDao.getAll()?.isNotEmpty() != true) {
                    _nestedNavigationFlow.emit(GenericLoading)
                }
                val catsResponse = apiService.fetchCats(page = upcomingPage)?.filterNotNull()
                requireNotNull(catsResponse)
                catsDao.insertOrUpdate(catsResponse)
                upcomingPage += 1
                startCollectingCatsIfNotAlready()
            } catch (e: Exception) {
                Timber.e(e)
                handleError(e)
            }
        }
    }

    private suspend fun handleError(exception: Exception) {
        if (catsDao.getAll()?.isNotEmpty() == true) return
        when {
            exception.isConnectionError() -> {
                _nestedNavigationFlow.emit(GenericNoConnectionError)
            }
            exception is HttpException -> {
                _nestedNavigationFlow.emit(GenericServerError)
            }
            else -> _nestedNavigationFlow.emit(GenericError)
        }
    }
}