package net.lc.presenters

import com.tieudieu.util.DebugLog
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import net.lc.fragments.LiveCountFragment
import net.lc.utils.Constants
import net.lc.utils.Models
import net.lc.utils.network.RxRetrofitService
import java.util.concurrent.TimeUnit


/**
 * Created by HP on 11/28/2016.
 */
class MainPresenter {
    var liveCountFragment: LiveCountFragment? = null

    constructor(lcFragment: LiveCountFragment) {
        liveCountFragment = lcFragment
    }

    var isRequest = false
    fun periodicRequestChannelInfo(channelName: String) {
        Flowable
                .interval(0, 30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map { requestChannelInfo() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun requestChannelInfo() {
        if (isRequest)
            return
        isRequest = true
        val disposableObserver = object : DisposableObserver<Models.ChannelListResponse>() {
            override fun onComplete() {
                // do nothing
                isRequest = false
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                isRequest = false
            }

            override fun onNext(response: Models.ChannelListResponse) {
                DebugLog.e(response.items!!.get(0).statistics!!.videoCount)

            }
        }
        RxRetrofitService.instance.service.getChannelInfo(Constants.API_KEY, Constants.PART_STATISTICS, "SkyDoesMinecraft")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        liveCountFragment!!.mDisposables.add(disposableObserver)
    }
}

