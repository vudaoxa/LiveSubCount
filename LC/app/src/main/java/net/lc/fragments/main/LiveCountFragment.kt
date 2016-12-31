package net.lc.fragments.main

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewSwitcher
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import com.tieudieu.util.DebugLog
import hu.akarnokd.rxjava2.operators.FlowableTransformers
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.fragment_live_count.*
import net.lc.R
import net.lc.presenters.MainPresenter
import net.lc.utils.Constants
import net.lc.utils.Models
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by HP on 11/28/2016.
 */
class LiveCountFragment : BaseFragmentStack() {
    val mDisposables = CompositeDisposable()
    var mMainPresenter: MainPresenter? = null
    val valve = PublishProcessor.create<Boolean>()
    var x: Boolean? = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainPresenter = MainPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_live_count, container, false)
        return view
    }

    var num = 0
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        val animIn = AnimationUtils.loadAnimation(activity,
//                android.R.anim.fade_in)
//        val animOut = AnimationUtils.loadAnimation(activity,
//                android.R.anim.fade_out)
//        sw_count.setInAnimation(animIn)
//        sw_count.setOutAnimation(animOut)
        sw_count.setFactory(mFactory)
        btn_ytb_view.setOnClickListener {
            //            sw_count.setText(num.toString())
//            num++
        }
        Flowable.interval(0, 5, TimeUnit.SECONDS)
                .compose(FlowableTransformers.valve<Long>(valve, true))
                .subscribe(object : Consumer<Long> {
                    @Throws(Exception::class)
                    override fun accept(count: Long?) {
                        DebugLog.e("Count = " + count!!)
                        if (x!!) {
                            mMainPresenter!!.requestChannelInfo(this@LiveCountFragment, Constants.API_KEY, Constants.PART_STATISTICS, "SkyDoesMinecraft")
                        }

                    }
                })
    }

    override fun onResume() {
        super.onResume()
        DebugLog.e("onresume-------------------------------------")
        toggle()
//        mMainPresenter!!.periodicRequestChannelInfo("PewDiePie")
//    mMainPresenter!!.requestChannelInfo()
    }

    override fun onPause() {
        super.onPause()
        DebugLog.e("onpause---------------------+++")
        toggle()
//
    }

    override fun onDestroy() {
        super.onDestroy()
        DebugLog.e("onDestroy--------------------------")
        mDisposables.dispose()
    }

    fun update(items: ArrayList<Models.ItemInfo>) {
        sw_count.setText(items.get(0).statistics!!.subscriberCount)
    }

    fun toggle() {
        x = !x!!
        valve.onNext(x)
    }

    private val mFactory = ViewSwitcher.ViewFactory {
        // Create a new TextView
        val t = TextView(activity)
        t.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
//        t.setTextAppearance(activity, android.R.style.TextAppearance_Large)
        t.setTextColor(Color.WHITE)
        t.textSize = 40F
        t
    }
}