package net.lc.presenters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import net.lc.fragments.main.FollowingChannelsFragment
import net.lc.models.ChannelListResponse
import net.lc.models.RSearchResult
import net.lc.network.RxRetrofitService
import net.lc.network.models.RetrofitException

/**
 * Created by mrvu on 1/29/17.
 */
class FollowingPresenter {
    //    var mRealmPresenter:MRealmPresenter?=null
    var isRequest = false

    fun loadLC(mFollowingFragment: FollowingChannelsFragment, rSearchResults: List<RSearchResult>,
               apiKey: String, part: String, ids: String?) {
        isRequest = true
        val disposableObserver = object : DisposableObserver<ChannelListResponse>() {
            override fun onComplete() {
                // do nothing
                isRequest = false
            }

            override fun onError(e: Throwable) {
                mFollowingFragment.apply {
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
                        hideLoadMoreIndicator()
                    }
                }
                isRequest = false
            }

            override fun onNext(response: ChannelListResponse) {
                mFollowingFragment.apply {
                    if (isAdded && isVisible) {
                        rSearchResults.apply {
                            val channelStatistics = response.channelsInfo!!
                            channelStatistics.apply {
                                for (i in rSearchResults.indices) {
                                    val j = channelStatistics.indexOfFirst { it.id == rSearchResults[i].channelId }
                                    val currentTime = System.currentTimeMillis()
                                    val subCount = channelStatistics[j].statistics!!.subscriberCount
                                    val currentSubCount = rSearchResults[i].subscriberCount
                                    if (subCount != currentSubCount) {
                                        MRealmPresenter.apply {
                                            updateChannelSubscriberCount(channelStatistics[j].id, subCount
                                                    , currentTime)
                                        }
                                    }
                                    //notify
                                    onNotisCheck(rSearchResults[i])
                                }
                                hideLoading()
                            }
                        }
                    }
                }
            }
        }

        RxRetrofitService.instance.rxApiServices.requestChannelsInfo(apiKey, part, null, ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mFollowingFragment.mDisposables.add(disposableObserver)
    }

}