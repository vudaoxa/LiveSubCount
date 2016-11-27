package net.lc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tieudieu.util.DebugLog
import net.lc.utils.network.RetrofitService
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        val xx = RetrofitService.instance.getChannelInfo("SkyDoesMinecraft")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { channelListResponse ->
                            DebugLog.e(channelListResponse.items!!.get(0).statistics!!.videoCount)
                        },
                        { e ->
                            e.printStackTrace()
                        }
                )
    }
}
