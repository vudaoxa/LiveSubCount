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
    //    var isRequest = false
    var mRealmPresenter: MRealmPresenter? = null

    //when user click user name of LiveCountFragment
    fun requestQSearchLC(mLCFragment: LiveCountFragment, apiKey: String, part: String, type: String,
                         pageToken: String?, query: String) {
//        if (isRequest) return
//        isRequest = true
        val disposableObserver = object : DisposableObserver<SearchListResponse>() {
            override fun onComplete() {
//                isRequest = false
            }

            override fun onError(e: Throwable?) {
                if (mLCFragment.isAdded && mLCFragment.isVisible) {
                    val error = e as RetrofitException
                    when (error.kind) {
                        RetrofitException.Kind.NETWORK -> {
                            mLCFragment.onNoInternetConnection()
                        }
                        else -> {
                            mLCFragment.onLoadDataFailure()
                        }
                    }
                }
//                isRequest = false
            }

            override fun onNext(t: SearchListResponse?) {
                if (mLCFragment.isAdded && mLCFragment.isVisible) {
                    t?.apply {
                        val searchResults = t.items
                        searchResults?.apply {
                            if (searchResults.isNotEmpty()) {
                                val ids = searchResults.map { it -> it.idInfo?.channelId!! }.toMutableList().joinToString(", ", "", "")
                                requestQSearchChannelsInfo(mLCFragment, t, apiKey,
                                        Constants.PART_STATISTICS, ids)
//                                mRealmPresenter?.saveSearchQuery(query)
                                val searchQueryRealm = SearchQueryRealm(query, System.currentTimeMillis())
                                mRealmPresenter?.saveObject(searchQueryRealm)
                            } else {
                                mLCFragment.isEmptyQSearchResult()
                            }
                        } ?: let {
                            mLCFragment.isEmptyQSearchResult()
                        }
                    } ?: let {
                        mLCFragment.onLoadDataFailure()
                    }
                }
            }
        }

        RxRetrofitService.instance.rxApiServices.requestSearch(apiKey, part, pageToken, query, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mLCFragment.mDisposables.add(disposableObserver)
    }

    //ids was gathered from @requestQSearchLC
    fun requestQSearchChannelsInfo(mLiveCountFragment: LiveCountFragment, searchListResponse: SearchListResponse?,
                                   apiKey: String, part: String, ids: String?) {
//        isRequest = true
        val disposableObserver = object : DisposableObserver<ChannelListResponse>() {
            override fun onComplete() {
                // do nothing
//                isRequest = false
            }

            override fun onError(e: Throwable) {
                if (mLiveCountFragment.isAdded && mLiveCountFragment.isVisible) {
                    val error = e as RetrofitException
                    when (error.kind) {
                        RetrofitException.Kind.NETWORK -> {
                            mLiveCountFragment.onNoInternetConnection()
                        }
                        else -> {
                            mLiveCountFragment.onLoadDataFailure()
                        }
                    }
                    mLiveCountFragment.showLoading(false)

                }
//                isRequest = false
            }

            override fun onNext(response: ChannelListResponse) {
                val searchResults = searchListResponse?.items
                val channelStatistics = response.channelsInfo!!
                for (i in searchResults!!.indices) {
                    val j = channelStatistics.indexOfFirst { it -> it.id == searchResults[i].idInfo?.channelId }
                    searchResults[i].statistics = channelStatistics[j].statistics
                }
                mLiveCountFragment.onSearchResultReceived(searchResults.maxBy { it.statistics!!.subscriberCount!!.toInt() }!!)
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
                            apiKey: String, part: String, ids: String?, singleRequest: Boolean, duration: Long) {
        DebugLog.e("requestChannelsInfo++++=+=====++++++++++=1")
//        if (singleRequest && isRequest)
//            return
//        isRequest = true
        val currentTime = System.currentTimeMillis()
        if (currentTime - time >= duration) {
            time = currentTime
        } else {
            DebugLog.e("requestChannelsInfo-----return")
            return
        }
        DebugLog.e("requestChannelsInfo++++=+=====++++++++++=----------------2")
        val disposableObserver = object : DisposableObserver<ChannelListResponse>() {
            override fun onComplete() {
                // do nothing
//                isRequest = false
            }

            override fun onError(e: Throwable) {
                if (mLiveCountFragment.isAdded && mLiveCountFragment.isVisible) {
                    val error = e as RetrofitException
                    when (error.kind) {
                        RetrofitException.Kind.NETWORK -> {
                            mLiveCountFragment.onNoInternetConnection()
                        }
                        else -> {
                            mLiveCountFragment.onLoadDataFailure()
                        }
                    }
                    mLiveCountFragment.showLoading(false)

                }
//                isRequest = false
            }

            override fun onNext(response: ChannelListResponse) {
                DebugLog.e("requestChannelsInfo-----------------------")
                mLiveCountFragment.update(response, part)
            }
        }

        RxRetrofitService.instance.rxApiServices.requestChannelsInfo(apiKey, part, null, ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mLiveCountFragment.mDisposables.add(disposableObserver)
    }


    fun requestRandomChannel(mLiveCountFragment: LiveCountFragment, currentDate: String) {
//        if(isRequest) return
//        isRequest=true
        val disposableObserver = object : DisposableObserver<String>() {
            override fun onComplete() {
                // do nothing
//                isRequest = false
            }

            override fun onError(e: Throwable) {
                if (mLiveCountFragment.isAdded && mLiveCountFragment.isVisible) {
                    val error = e as RetrofitException
                    when (error.kind) {
                        RetrofitException.Kind.NETWORK -> {
                            mLiveCountFragment.onLoadRandomChannelFailure()
                        }
                        else -> {
                            mLiveCountFragment.onLoadRandomChannelFailure()
                        }
                    }
                }
//                isRequest = false
            }

            override fun onNext(response: String) {
                mLiveCountFragment.onFeaturedResponse(response)
            }
        }
        RxRetrofitService.instance.rxxApiServices.requestFeaturedFileLC(currentDate)
                .flatMap { it -> Observable.just(it.string()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mLiveCountFragment.mDisposables.add(disposableObserver)
    }
}

