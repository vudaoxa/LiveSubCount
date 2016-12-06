package net.lc.presenters

import com.tieudieu.util.DebugLog
import net.lc.utils.Constants
import net.lc.utils.Models
import net.lc.utils.network.RetrofitService
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by HP on 11/28/2016.
 */
object MainPresenter {
    var isRequest = false
    fun periodicRequestChannelInfo(channelName: String){
        val scheduler = Schedulers.from(Executors.newSingleThreadExecutor())
        val obs = Observable.interval(5, TimeUnit.SECONDS, scheduler)
                .flatMap { tick -> requestChannelInfo() }
                .

    }
    fun requestChannelInfo() {
        if (isRequest)
            return
        isRequest = true
        val xx = RetrofitService.instance.service.getChannelInfo(Constants.API_KEY, Constants.PART_STATISTICS, "SkyDoesMinecraft")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Models.ChannelListResponse>(){
                    override fun onCompleted() {
                        // do nothing
                        isRequest = false
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(response: Models.ChannelListResponse) {
                        DebugLog.e(response.items!!.get(0).statistics!!.videoCount)
                    }
                })

    }
}

