package net.lc.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import com.tieudieu.util.DebugLog
import io.reactivex.disposables.CompositeDisposable
import net.lc.R
import net.lc.presenters.MainPresenter

/**
 * Created by HP on 11/28/2016.
 */
class LiveCountFragment : BaseFragmentStack(){
    val mDisposables = CompositeDisposable()
    var mMainPresenter: MainPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainPresenter = MainPresenter(this)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.fragment_live_count, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        DebugLog.e("onresume-------------------------------------")
        mMainPresenter!!.periodicRequestChannelInfo("PewDiePie")
//    mMainPresenter!!.requestChannelInfo()
    }

    override fun onPause() {
        super.onPause()
        DebugLog.e("onpause---------------------+++")
        mDisposables.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        DebugLog.e("onDestroy--------------------------")

    }
}