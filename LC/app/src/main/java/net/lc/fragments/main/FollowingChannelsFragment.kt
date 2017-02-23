package net.lc.fragments.main

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdListener
import com.tieudieu.util.DebugLog
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.realm.RealmObject
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_following_channels.*
import net.lc.adapters.FollowingChannelsRvAdapter
import net.lc.fragments.MBaseFragment
import net.lc.holders.BaseViewHolder
import net.lc.models.*
import net.lc.presenters.FollowingPresenter
import net.lc.presenters.MRealmPresenter
import net.lc.presenters.SettingsPresenter
import net.lc.utils.*
import net.live.sub.R
import rx.subscriptions.CompositeSubscription
import vn.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener
import java.util.concurrent.TimeUnit

/**
 * Created by HP on 11/28/2016.
 */
class FollowingChannelsFragment(val mMainFragment: MainFragment) : MBaseFragment(), ICallbackOnClick,
        ICallbackRealmResultChange, ICallbackEditFav
        , ILoadData, IConnect, ICallbackNotis, ICallbackToggleSettings {
    private val listenerLoadChecklist = View.OnClickListener {
        showLoading(true)
        showMessageBottom(mCallbackDismiss)
//        dispose()
        loadLC()
    }
    private val duration = 5L
    var editClicked = false
    private var startLoad: Boolean = false
    private var d: Disposable? = null
    //    private val valve = PublishProcessor.create<Boolean>()
    private var valveOpened = false
    val mCallbackLoadChecklist = CallbackBottomMessage(true, R.string.internet_no_conection, R.string.redo,
            true, listenerLoadChecklist)
    var mCallbackChange: InitialNotificationsFragment? = null
    private var mCallbackTouch: RecyclerTouchListener? = null
    private var mFollowingChannelsRvAdapter: FollowingChannelsRvAdapter? = null
    private var mFollowingPresenter: FollowingPresenter? = null
    val mDisposables = CompositeDisposable()
    val mSubscription = CompositeSubscription()

    companion object {
        fun newInstance(mainFragment: MainFragment): FollowingChannelsFragment {
            val fragment = FollowingChannelsFragment(mainFragment)
            mainFragment.mCallbackEditFav = fragment
            SettingsPresenter.followingChannelsFragment = fragment
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFollowingPresenter = FollowingPresenter()
        MRealmPresenter.removalCallback = this
        mInterstitialAd = initInterAds(activity, adListener)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_following_channels, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadFollowingChannels()
    }

    override fun onResume() {
        super.onResume()
        rv.addOnItemTouchListener(mCallbackTouch)
        if (!valveOpened) {
            toggle(d, true)
        }
    }

    override fun onPause() {
        super.onPause()
        rv.removeOnItemTouchListener(mCallbackTouch)
        toggle(d, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose()
        MRealmPresenter.realmClose()
    }

    fun dispose() {
        DebugLog.e("dispose-----------")
        d?.dispose()
        mDisposables.clear()
        mSubscription.unsubscribe()
        valveOpened = false
    }

    fun initFields() {
        showLoading(true)
        val layoutManager = LinearLayoutManager(activity)
        mFollowingChannelsRvAdapter = FollowingChannelsRvAdapter(activity, null, this)
        rv.adapter = mFollowingChannelsRvAdapter
        rv.layoutManager = layoutManager
        mCallbackTouch = RecyclerTouchListener(activity, rv)
        mCallbackTouch!!.setIndependentViews(R.id.rowButton)
                .setViewsToFade(R.id.rowButton)
                .setClickable(object : RecyclerTouchListener.OnRowClickListener {
                    override fun onRowClicked(position: Int) {
                        mCallbackTouch!!.closeVisibleBG(null)
                    }

                    override fun onIndependentViewClicked(independentViewID: Int, position: Int) {
                        mCallbackTouch!!.openSwipeOptions(position - layoutManager.findFirstVisibleItemPosition())
                    }
                })
                .setSwipeable(R.id.rowFG, R.id.rowBG) { viewID, position -> }
    }

    override fun onToggleSettings() {
        mFollowingChannelsRvAdapter?.apply {
            data?.apply {
                if (isNotEmpty())
                    notifyDataSetChanged()
            }
        }
    }

    override fun onEditClicked(edit: Int) {
        DebugLog.e("onEditClicked-------------$edit")
        editClicked = edit == 1
        if (editClicked) {
            mCallbackTouch!!.closeVisibleBG(null)
        }
        mFollowingChannelsRvAdapter?.notifyDataSetChanged()
    }

    override fun onClick(position: Int, event: Int) {
        mPosition = position
        mEvent = event
//        action()
        val x = rand(4)
        if (x < 2) {
            action()
        } else {
            if (!showInterAds(mInterstitialAd)) {
                action()
            }
        }
    }

    fun action() {
        val item = mFollowingChannelsRvAdapter?.data?.get(mPosition)
        item?.apply {
            when (mEvent) {
                BaseViewHolder.ACTION_CLICK_FOLLOWING_CHANNEL -> {
                    sendHit(CATEGORY_ACTION, ACTION_CLICK_FOLLOWING_ITEM)
                    mMainFragment.onSearchResultRealmReceived(this)
                }
                BaseViewHolder.ACTION_CLICK_FOLLOWING_CHANNEL_REMOVAL -> {
                    sendHit(CATEGORY_ACTION, ACTION_REMOVE_FOLLOWING_ITEM)
                    mCallbackTouch!!.closeVisibleBG(null)
                    MRealmPresenter.updateSearchResultRealm(channelId, channelTitle, subscriberCount, description, 0, -1L, thumbnailUrl, false)
                }
                BaseViewHolder.ACTION_CLICK_FOLLOWING_CHANNEL_BELL -> {
                    Handler().postDelayed({
                        sendHit(CATEGORY_ACTION, ACTION_BELL)
                        DebugLog.e("ACTION_CLICK_FOLLOWING_CHANNEL_BELL-------------")
                        screenManager!!.onNewScreenRequested(IndexTags.INIT_NOTIS, this@FollowingChannelsFragment, this)
                    }, 500)
                }
                BaseViewHolder.ACTION_CLICK_FOLLOWING_CHANNEL_PLAY -> {
                    sendHit(CATEGORY_ACTION, ACTION_PLAY)
                    DebugLog.e("ACTION_CLICK_FOLLOWING_CHANNEL_PLAY---------------")
                    MRealmPresenter.updateChannelAutoLC(channelId!!, 1 - autoLC!!)
                }
            }
        }
    }

    fun onSubmitSuccess() {
        mFollowingChannelsRvAdapter?.apply {
            notifyItemChanged(mPosition)
        }
    }

    fun loadFollowingChannels() {
        MRealmPresenter.apply {
            loadFollowingChannels(this@FollowingChannelsFragment)
        }
    }

    override fun getMinSize() = 0
    override fun getMaxSize() = 20
    override fun <T> isEmptyData(list: MutableList<T>?): Boolean {
        return list == null || list.size <= getMinSize()
    }

    override fun hideLoadMoreIndicator() {
        //nothing
    }

    override fun isEmptyData() {
        showLoading(false)
        mCallbackDismiss.apply {
            if (mFollowingChannelsRvAdapter!!.itemCount <= getMinSize()) {
                isShow = true
                content = R.string.empty_data
            } else {
                isShow = false
            }
            showMessageBottom(this)
        }
    }

    override fun <T : RealmObject> onChange(realmResult: RealmResults<T>) {
        val tmp = realmResult as? RealmResults<SearchResultRealm>?
        tmp?.apply {
            mFollowingChannelsRvAdapter?.apply {
                data = map { it.clone() }
                notifyDataSetChanged()
                isEmptyData()
                mCallbackChange?.apply {
                    data?.apply {
                        val i = indexOfFirst { it.channelId == rSearchResult.channelId }
                        if (i != -1) {
                            rSearchResult.subscriberCount = this[i].subscriberCount
                            onChange()
                        }
                    }
                }
            }
        }
    }

    fun loadLC() {
        if (valveOpened) {
            return
        }
        startLoad = true
        val consumer: Consumer<Long>?
        consumer = Consumer {
            if (valveOpened) {
                //statistics
                DebugLog.e(it.toString() + "-----")
                pediodicLoadLC()
            }
        }
        d = Flowable.interval(0, duration, TimeUnit.SECONDS)
//                .compose(FlowableTransformers.valve<Long>(valve, true)) xx
                .subscribe(consumer)
        toggle(d, true)
    }

    fun pediodicLoadLC() {
        mFollowingPresenter?.apply {
            if (isRequest) return
            val searchResultsRealm = mFollowingChannelsRvAdapter!!.data!!.filter { it.autoLC == 1 }.toList()
            val ids = mFollowingChannelsRvAdapter!!.data!!.map { it.channelId }.toList().joinToString(", ", "", "")
            loadLC(this@FollowingChannelsFragment, searchResultsRealm, Constants.API_KEY, Constants.PART_STATISTICS, ids)
        }
    }

    fun toggle(d: Disposable?, on: Boolean) {
        d?.apply {
            if (isDisposed) {
                return
            }
            valveOpened = on
//            valve.onNext(on) xx
        }
    }

    fun hideLoading() {
        if (startLoad) {
            showLoading(false)
            startLoad = false
        }
    }

    override fun onLoadDataFailure() {
        mCallbackLoadChecklist.content = R.string.error_conection
        onLoadDataFailure(mCallbackLoadChecklist)
        dispose()
    }

    override fun onNoInternetConnection() {
        mCallbackLoadChecklist.content = R.string.internet_no_conection
        onNoInternetConnection(mCallbackLoadChecklist)
        dispose()
    }

    override fun onNotisCheck(rSearchResult: RSearchResult) {
        rSearchResult.apply {
            if (onNotisCheck(activity)) {
                MRealmPresenter.apply {
                    updateChannelNotis(rSearchResult)
                }
            }
        }
    }

    fun onNotisSuccess() {
        val audioSetting = SettingsPresenter.getSettingsMapItem(1)
        if (audioSetting.checked)
            AudioPlayer.getInstance(activity).playAudio(AudioConstants.PATH_NOTI, false)
    }

    val adListener = object : AdListener() {
        override fun onAdClosed() {
            DebugLog.e("onAdClosed--------------")
            sendHit(CATEGORY_ACTION, ACTION_onAdClosed)
            action()
            requestNewInterstitial(mInterstitialAd)
        }

        override fun onAdFailedToLoad(p0: Int) {
            sendHit(CATEGORY_ACTION, mapAdsErrors.get(p0) as String)
        }

        override fun onAdOpened() {
            sendHit(CATEGORY_ACTION, ACTION_onAdOpened)
        }

        override fun onAdLoaded() {
            sendHit(CATEGORY_ACTION, ACTION_onAdLoaded)
        }

        override fun onAdLeftApplication() {
            sendHit(CATEGORY_ACTION, ACTION_onAdLeftApplication)
        }
    }
}