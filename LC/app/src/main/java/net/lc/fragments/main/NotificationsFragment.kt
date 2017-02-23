package net.lc.fragments.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdListener
import com.tieudieu.util.DebugLog
import io.realm.RealmObject
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_notis.*
import net.lc.adapters.NotisRvAdapter
import net.lc.fragments.MBaseFragment
import net.lc.holders.BaseViewHolder
import net.lc.models.*
import net.lc.presenters.MRealmPresenter
import net.lc.presenters.SettingsPresenter
import net.lc.utils.*
import net.live.sub.R
import rx.subscriptions.CompositeSubscription
import vn.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener

/**
 * Created by HP on 11/28/2016.
 */
class NotificationsFragment(val mMainFragment: MainFragment) : MBaseFragment(), ICallbackOnClick,
        ICallbackRealmResultChange, ICallbackClear, ILoadData, ICallbackNotis, ICallbackToggleSettings,
        ICallBackBadge {

    private val listenerLoadChecklist = View.OnClickListener {
        showLoading(true)
        showMessageBottom(mCallbackDismiss)
        loadNotis()
    }
    val mCallbackLoadChecklist = CallbackBottomMessage(true, R.string.internet_no_conection, R.string.redo,
            true, listenerLoadChecklist)
    private var mCallbackTouch: RecyclerTouchListener? = null
    private var mNotisRvAdapter: NotisRvAdapter? = null
    val mSubscription = CompositeSubscription()

    companion object {
        fun newInstance(mainFragment: MainFragment): NotificationsFragment {
            val fragment = NotificationsFragment(mainFragment)
            SettingsPresenter.notificationsFragment = fragment
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MRealmPresenter.badgeCallback = this
        mInterstitialAd = initInterAds(activity, adListener)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_notis, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadNotis()
    }

    override fun onResume() {
        super.onResume()
        rv.addOnItemTouchListener(mCallbackTouch)
    }

    override fun onPause() {
        super.onPause()
        rv.removeOnItemTouchListener(mCallbackTouch)
    }

    override fun onDestroy() {
        super.onDestroy()
        MRealmPresenter.realmClose()
        dispose()
    }

    fun dispose() {
        mSubscription.unsubscribe()
    }

    fun loadNotis() {
        MRealmPresenter.apply {
            loadNotis(this@NotificationsFragment)
        }
    }

    fun initFields() {
        showLoading(true)
        val layoutManager = LinearLayoutManager(activity)
        mNotisRvAdapter = NotisRvAdapter(activity, null, this)
        rv.adapter = mNotisRvAdapter
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

    var mSize = -1
    var first = true
    override fun <T : RealmObject> onChange(realmResult: RealmResults<T>) {
        DebugLog.e("onChange------------------")
        val tmp = realmResult as? RealmResults<SearchResultRealm>?
        tmp.apply {
            val tmpMap = this!!.map { it.clone() }.toMutableList()
            mNotisRvAdapter?.apply {
                data = tmpMap
                notifyDataSetChanged()
                isEmptyData()
                mSize = size
                if (first) {
                    updateBadge()
                    first = false
                }
            }
        }
    }

    override fun updateBadge() {
        mMainFragment.setBadge(mSize)
    }

    override fun onToggleSettings() {
        mNotisRvAdapter?.apply {
            data?.apply {
                if (isNotEmpty())
                    notifyDataSetChanged()
            }
        }
    }

    override fun isEmptyData() {
        showLoading(false)
        mCallbackDismiss.apply {
            if (mNotisRvAdapter!!.itemCount <= getMinSize()) {
                isShow = true
                content = R.string.empty_data
            } else {
                isShow = false
            }
            showMessageBottom(this)
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


    override fun onClick(position: Int, event: Int) {
        mPosition = position
        mEvent = event
//        showInterAds()
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
        val item = mNotisRvAdapter?.data?.get(mPosition)
        item?.apply {
            when (mEvent) {
                BaseViewHolder.ACTION_CLICK_FOLLOWING_CHANNEL -> {
                    sendHit(CATEGORY_ACTION, ACTION_CLICK_NOTIS_ITEM)
                    mMainFragment.onSearchResultRealmReceived(this)
                }
                BaseViewHolder.ACTION_CLICK_FOLLOWING_CHANNEL_REMOVAL -> {
                    sendHit(CATEGORY_ACTION, ACTION_REMOVE_NOTIS_ITEM)
                    mCallbackTouch!!.closeVisibleBG(null)
                    noti = 0
                    MRealmPresenter.updateChannelNotisRemoval(this)
                }
            }
        }
    }

    override fun onClear() {

    }

    fun onLoadDataFailure() {
        mCallbackLoadChecklist.content = R.string.error_load_notis
        onLoadDataFailure(mCallbackLoadChecklist)
        dispose()
    }

    override fun onNotisCheck(rSearchResult: RSearchResult) {
        mNotisRvAdapter?.apply {
            data?.apply {
                if (contains(rSearchResult)) {
                    notifyItemChanged(indexOf(rSearchResult))
                } else {
                    add(0, rSearchResult)
                    notifyItemInserted(0)
                }
            }
        }
    }

    val adListener = object : AdListener() {
        override fun onAdClosed() {
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