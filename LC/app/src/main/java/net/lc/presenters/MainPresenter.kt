package net.lc.presenters

import com.tieudieu.util.DebugLog
import net.lc.utils.network.RetrofitService
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by HP on 11/28/2016.
 */
object MainPresenter {
    fun requestChannelInfo() {
        val xx = RetrofitService.instance.getChannelInfo("SkyDoesMinecraft")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { channelListResponse ->
                            DebugLog.e(channelListResponse.items!!.get(0).statistics!!.videoCount)
                        },
                        { e ->
                            e.printStackTrace()
                        }
                )

    }
}