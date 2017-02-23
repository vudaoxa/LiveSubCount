package net.lc.presenters

import com.tieudieu.util.DebugLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import net.lc.fragments.main.LiveCountFragment
import net.lc.models.ChannelListResponse
import net.lc.models.SearchListResponse
import net.lc.models.SearchQueryRealm
import net.lc.network.RxRetrofitService
import net.lc.network.models.RetrofitException
import net.lc.utils.Constants


/**
 * Created by HP on 11/28/2016.
 */
class LCPresenter {
//    var mRealmPresenter: MRealmPresenter? = null

    //when user click user name of LiveCountFragment
    fun requestQSearchLC(mLiveCountFragment: LiveCountFragment, apiKey: String, part: String, type: String,
                         pageToken: String?, query: String) {
        val disposableObserver = object : DisposableObserver<SearchListResponse>() {
            override fun onComplete() {
            }

            override fun onError(e: Throwable?) {
                mLiveCountFragment.apply {
                    if (isAdded && isVisible) {
                        val error = e as RetrofitException
                        when (error.kind) {
                            RetrofitException.Kind.NETWORK -> {
                                onNoInternetConnection()
                            }
                            else -> {
                                onLoadDataFailure()
                            }
                        }
                    }
                }
            }

            override fun onNext(t: SearchListResponse?) {
                mLiveCountFragment.apply {
                    if (isAdded && isVisible) {
                        t?.apply {
                            val searchResults = t.items
                            searchResults?.apply {
                                if (searchResults.isNotEmpty()) {
                                    val ids = searchResults.map { it.idInfo?.channelId!! }.toMutableList().joinToString(", ", "", "")
                                    requestQSearchChannelsInfo(mLiveCountFragment, t, apiKey,
                                            Constants.PART_STATISTICS, ids)
                                    val searchQueryRealm = SearchQueryRealm(query.toLowerCase(), System.currentTimeMillis())
                                    MRealmPresenter.saveObject(searchQueryRealm)
                                } else {
                                    isEmptyQSearchResult()
                                }
                            } ?: let {
                                isEmptyQSearchResult()
                            }
                        } ?: let {
                            onLoadDataFailure()
                        }
                    }
                }
            }
        }

        RxRetrofitService.instance.rxApiServices.requestSearch(apiKey, part, pageToken, query, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mLiveCountFragment.mDisposables.add(disposableObserver)
    }

    //ids was gathered from @requestQSearchLC
    fun requestQSearchChannelsInfo(mLiveCountFragment: LiveCountFragment, searchListResponse: SearchListResponse?,
                                   apiKey: String, part: String, ids: String?) {
        val disposableObserver = object : DisposableObserver<ChannelListResponse>() {
            override fun onComplete() {
                // do nothing
            }

            override fun onError(e: Throwable) {
                mLiveCountFragment.apply {
                    if (isAdded && isVisible) {
                        val error = e as RetrofitException
                        when (error.kind) {
                            RetrofitException.Kind.NETWORK -> {
                                onNoInternetConnection()
                            }
                            else -> {
                                onLoadDataFailure()
                            }
                        }
                        showLoading(false)
                    }
                }
            }

            override fun onNext(response: ChannelListResponse) {
                mLiveCountFragment.apply {
                    if (isAdded && isVisible) {
                        val searchResults = searchListResponse?.items
                        val channelStatistics = response.channelsInfo!!
                        for (i in searchResults!!.indices) {
                            val j = channelStatistics.indexOfFirst { it -> it.id == searchResults[i].idInfo?.channelId }
                            searchResults[i].statistics = channelStatistics[j].statistics
                        }
                        val searchResult = searchResults.maxBy { it.statistics!!.subscriberCount!!.toInt() }!!

                        onSearchResultReceived(searchResult)
                    }
                }

            }
        }

        RxRetrofitService.instance.rxApiServices.requestChannelsInfo(apiKey, part, null, ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mLiveCountFragment.mDisposables.add(disposableObserver)
    }

    var time = -1L
    fun requestChannelsInfo(mLiveCountFragment: LiveCountFragment,
                            apiKey: String, part: String, ids: String?, forUsrName: String?, duration: Long) {
        DebugLog.e("requestChannelsInfo++++=+=====++++++++++=1")
        if (duration != -1L) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - time >= duration) {
                time = currentTime
            } else {
                DebugLog.e("requestChannelsInfo-----return")
                return
            }
        }
        DebugLog.e("requestChannelsInfo++++=+=====++++++++++=----------------2")
        val disposableObserver = object : DisposableObserver<ChannelListResponse>() {
            override fun onComplete() {
                // do nothing
            }

            override fun onError(e: Throwable) {
                mLiveCountFragment.apply {
                    if (isAdded && isVisible) {
                        val error = e as RetrofitException
                        when (error.kind) {
                            RetrofitException.Kind.NETWORK -> {
                                onNoInternetConnection()
                            }
                            else -> {
                                onLoadDataFailure()
                            }
                        }
                        showLoading(false)
                    }
                }
            }

            override fun onNext(response: ChannelListResponse) {
                DebugLog.e("requestChannelsInfo-----------------------")
                mLiveCountFragment.apply {
                    if (isAdded && isVisible) {
                        update(response, part)
                    }
                }
            }
        }

        val id = if (forUsrName == null) ids else null
        RxRetrofitService.instance.rxApiServices.requestChannelsInfo(apiKey, part, forUsrName, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mLiveCountFragment.mDisposables.add(disposableObserver)
    }


    fun requestRandomChannel(mLiveCountFragment: LiveCountFragment, currentDate: String) {
        val disposableObserver = object : DisposableObserver<String>() {
            override fun onComplete() {
                // do nothing
            }

            override fun onError(e: Throwable) {
                mLiveCountFragment.apply {
                    if (isAdded && isVisible) {
                        val error = e as RetrofitException
                        when (error.kind) {
                            RetrofitException.Kind.NETWORK -> {
                                onNoInternetConnection()
                            }
                            else -> {
                                onLoadRandomChannelFailure()
                            }
                        }
                    }
                }
            }

            override fun onNext(response: String) {
                mLiveCountFragment.apply {
                    if (isAdded && isVisible) {
                        onFeaturedResponse(response)
                    }
                }
            }
        }
        RxRetrofitService.instance.rxxApiServices.requestFeaturedFileLC(currentDate)
                .flatMap { Observable.just(it.string()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mLiveCountFragment.mDisposables.add(disposableObserver)
    }
}

