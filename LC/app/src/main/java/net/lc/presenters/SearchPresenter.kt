package net.lc.presenters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import net.lc.fragments.search.FirstSuggestionFragment
import net.lc.models.ChannelListResponse
import net.lc.models.SearchListResponse
import net.lc.models.SearchQueryRealm
import net.lc.network.RxRetrofitService
import net.lc.network.models.RetrofitException
import net.lc.utils.Constants

/**
 * Created by mrvu on 12/28/16.
 */
class SearchPresenter {
    var isRequest = false
    var mRealmPresenter: MRealmPresenter? = null
    companion object {
    }

    //when user typing in search bar
    fun requestSearchSuggestion(mFirstSuggestionFragment: FirstSuggestionFragment, apiKey: String, part: String, type: String,
                                pageToken: String?, query: String) {
        if (isRequest) return
        isRequest = true
        val disposableObserver = object : DisposableObserver<SearchListResponse>() {
            override fun onComplete() {
                isRequest = false
            }

            override fun onError(e: Throwable?) {
                if (mFirstSuggestionFragment.isAdded && mFirstSuggestionFragment.isVisible) {
                    val error = e as RetrofitException
                    when (error.kind) {
                        RetrofitException.Kind.NETWORK -> {
                            mFirstSuggestionFragment.onNoInternetConnection()
                        }
                        else -> {
                            mFirstSuggestionFragment.onLoadDataFailure()
                        }
                    }
                    mFirstSuggestionFragment.hideLoadMoreIndicator()

                }
                isRequest = false
            }

            override fun onNext(t: SearchListResponse?) {
                if (mFirstSuggestionFragment.isAdded && mFirstSuggestionFragment.isVisible) {
                    if (t != null) {
                        val searchResults = t.items
                        if (searchResults!!.isNotEmpty()) {
                            mFirstSuggestionFragment.hideLoadMoreIndicator()
                            val ids = searchResults.map { it -> it.idInfo?.channelId!! }.toMutableList().joinToString(", ", "", "")
                            requestSuggestionChannelsInfo(mFirstSuggestionFragment, t, apiKey,
                                    Constants.PART_STATISTICS, ids)
//                            mRealmPresenter?.saveSearchQuery(query)
                            val searchQueryRealm = SearchQueryRealm(query, System.currentTimeMillis())
                            if (mFirstSuggestionFragment.isFirstData) {
                                mRealmPresenter?.saveObject(searchQueryRealm)
//                                mRealmPresenter?.saveSearchQuery(query)
                            }
                        } else {
                            mFirstSuggestionFragment.isEmptyData()
                        }
                    } else {
                        mFirstSuggestionFragment.onLoadDataFailure()
                    }
                }
            }
        }

        RxRetrofitService.instance.rxApiServices.requestSearch(apiKey, part, pageToken, query, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mFirstSuggestionFragment.mDisposables.add(disposableObserver)
    }

    //ids was gathered from @requestSearchSuggestion
    fun requestSuggestionChannelsInfo(mFirstSuggestionFragment: FirstSuggestionFragment,
                                      searchListResponse: SearchListResponse?, apiKey: String, part: String, ids: String?) {
        isRequest = true
        val disposableObserver = object : DisposableObserver<ChannelListResponse>() {
            override fun onComplete() {
                // do nothing
                isRequest = false
            }

            override fun onError(e: Throwable) {
                if (mFirstSuggestionFragment.isAdded && mFirstSuggestionFragment.isVisible) {
                    val error = e as RetrofitException
                    when (error.kind) {
                        RetrofitException.Kind.NETWORK -> {
                            mFirstSuggestionFragment.onNoInternetConnection()
                        }
                        else -> {
                            mFirstSuggestionFragment.onLoadDataFailure()
                        }
                    }
                    mFirstSuggestionFragment.hideLoadMoreIndicator()

                }
                isRequest = false
            }

            override fun onNext(response: ChannelListResponse) {
                val searchResults = searchListResponse?.items
                val channelStatistics = response.channelsInfo!!
                for (i in searchResults!!.indices) {
                    val j = channelStatistics.indexOfFirst { it -> it.id == searchResults[i].idInfo?.channelId }
                    searchResults[i].statistics = channelStatistics[j].statistics
                }
                mFirstSuggestionFragment.bindData(searchListResponse!!)
            }
        }

        RxRetrofitService.instance.rxApiServices.requestChannelsInfo(apiKey, part, null, ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mFirstSuggestionFragment.mDisposables.add(disposableObserver)
    }


}