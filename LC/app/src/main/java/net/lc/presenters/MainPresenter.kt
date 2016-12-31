package net.lc.presenters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import net.lc.fragments.main.LiveCountFragment
import net.lc.utils.Models
import net.lc.utils.network.RxRetrofitService


/**
 * Created by HP on 11/28/2016.
 */
class MainPresenter {
    var isRequest = false

    fun requestChannelInfo(liveCountFragment: LiveCountFragment, apiKey: String, part: String, channelName: String) {
        if (isRequest)
            return
        isRequest = true
        val disposableObserver = object : DisposableObserver<Models.ChannelListResponse>() {
            override fun onComplete() {
                // do nothing
                isRequest = false
            }

            override fun onError(e: Throwable) {
                isRequest = false
            }

            override fun onNext(response: Models.ChannelListResponse) {
//                DebugLog.e(response.items!!.get(0).statistics!!.videoCount)
                liveCountFragment.update(response.items!!)
            }
        }
        RxRetrofitService.instance.rxApiServices.requestChannelInfo(apiKey, part, channelName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        liveCountFragment.mDisposables.add(disposableObserver)
    }
}

