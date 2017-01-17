package net.lc.fragments.main

//import net.lc.utils.RealmUtils
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tieudieu.util.DebugLog
import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmObject
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_following_channels.*
import net.lc.R
import net.lc.adapters.FollowingChannelsRvAdapter
import net.lc.fragments.MBaseFragment
import net.lc.holders.BaseViewHolder
import net.lc.models.ICallbackOnClick
import net.lc.models.ICallbackRealmResultChange
import net.lc.models.ILoadData
import net.lc.models.SearchResultRealm
import net.lc.presenters.MRealmPresenter
import rx.subscriptions.CompositeSubscription
import vn.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener

/**
 * Created by HP on 11/28/2016.
 */
class FollowingChannelsFragment(val mMainFragment: MainFragment) : MBaseFragment(), ICallbackOnClick, ICallbackRealmResultChange
        , ILoadData {
    private var mCallbackTouch: RecyclerTouchListener? = null
    private var mFollowingChannelsRvAdapter: FollowingChannelsRvAdapter? = null
    private var mRealmPresenter: MRealmPresenter? = null
    val mDisposables = CompositeDisposable()
    val mSubscription = CompositeSubscription()

    companion object {
        fun newInstance(mainFragment: MainFragment): FollowingChannelsFragment {
            val fragment = FollowingChannelsFragment(mainFragment)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRealmPresenter = MRealmPresenter()
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
    }

    override fun onPause() {
        super.onPause()
        rv.removeOnItemTouchListener(mCallbackTouch)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.dispose()
        mSubscription.unsubscribe()
    }

    fun initFields() {
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

    override fun onClick(position: Int, event: Int) {
        val item = mFollowingChannelsRvAdapter?.data?.get(position)
        item?.apply {
            when (event) {
                BaseViewHolder.ACTION_CLICK_FOLLOWING_CHANNEL -> {
                    mMainFragment.onSearchResultRealmReceived(this)
                }
                BaseViewHolder.ACTION_CLICK_FOLLOWING_CHANNEL_REMOVAL -> {
                    mRealmPresenter?.updateSearchResultRealm(this.channelId, this.channelTitle, 0, -1L, this.thumbnailUrl)
                }
                BaseViewHolder.ACTION_CLICK_FOLLOWING_CHANNEL_BELL -> {
                    DebugLog.e("ACTION_CLICK_FOLLOWING_CHANNEL_BELL-------------")
                }
                BaseViewHolder.ACTION_CLICK_FOLLOWING_CHANNEL_PLAY -> {
                    DebugLog.e("ACTION_CLICK_FOLLOWING_CHANNEL_PLAY---------------")
                }
            }
        }
    }


    fun loadFollowingChannels() {
        mRealmPresenter?.apply {
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
        if (mFollowingChannelsRvAdapter!!.itemCount <= getMinSize()) {
            mCallbackDismiss.isShow = true
            showMessageBottom(mCallbackDismiss)
        }
    }

    override fun <T : RealmObject> onChange(realmResult: RealmResults<T>) {
        mFollowingChannelsRvAdapter?.data = realmResult as? RealmResults<SearchResultRealm>?
        mFollowingChannelsRvAdapter?.notifyDataSetChanged()
    }
}