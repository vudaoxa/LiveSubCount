package net.lc.fragments.search


import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdListener
import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmObject
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_search_suggestions.*
import kotlinx.android.synthetic.main.item_history_clear.*
import net.lc.activities.SearchActivity
import net.lc.adapters.search.SearchHistoryRvAdapter
import net.lc.adapters.search.SearchSuggestionRvAdapter
import net.lc.fragments.MBaseFragment
import net.lc.holders.BaseViewHolder
import net.lc.models.*
import net.lc.presenters.MRealmPresenter
import net.lc.presenters.SearchPresenter
import net.lc.utils.*
import net.live.sub.R
import rx.subscriptions.CompositeSubscription
import vn.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener

/**
 * Created by mrvu on 12/28/16.
 */
class FirstSuggestionFragment(val mMainSearchFragment: MainSearchFragment) : MBaseFragment(),
        ICallbackOnClick, ICallbackSearch,
        ILoadData, IBackListener, IConnect, ICallbackRealmResultChange, ICallbackClear {
    companion object {
        fun newInstance(mMainSearchFragment: MainSearchFragment): FirstSuggestionFragment {
            val fragment = FirstSuggestionFragment(mMainSearchFragment)
            mMainSearchFragment.mCallbackSearch = fragment
            mMainSearchFragment.mBackListener = fragment
            return fragment
        }
    }

    private var mCallbackTouch: RecyclerTouchListener? = null
    //    private var mRealmPresenter: MRealmPresenter? = null
    private var mSearchPresenter: SearchPresenter? = null
    private var mSearchSuggestionRvAdapter: SearchSuggestionRvAdapter? = null
    private var mSearchHistoryRvAdapter: SearchHistoryRvAdapter? = null
    var isFirstData = true
    private var mSearchRequest: SearchRequest? = null
    val mDisposables = CompositeDisposable()
    val mSubscription = CompositeSubscription()
    private val listenerLoadChecklist = View.OnClickListener {
        showLoading(true)
        showMessageBottom(mCallbackDismiss)
        requestSearchSuggestion()
    }

    val mCallbackLoadChecklist = CallbackBottomMessage(true, R.string.internet_no_conection, R.string.redo,
            true, listenerLoadChecklist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSearchPresenter = SearchPresenter()
        mInterstitialAd = initInterAds(activity, adListener)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_search_suggestions, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadSearchHistory()
    }

    override fun onResume() {
        super.onResume()
        rv_search_history.addOnItemTouchListener(mCallbackTouch)
    }

    override fun onPause() {
        super.onPause()
        rv_search_history.removeOnItemTouchListener(mCallbackTouch)
    }
    override fun onDestroy() {
        super.onDestroy()
        mDisposables.dispose()
        mSubscription.unsubscribe()
    }

    fun initFields() {
        mSearchSuggestionRvAdapter = SearchSuggestionRvAdapter(activity, null, this)
        mSearchHistoryRvAdapter = SearchHistoryRvAdapter(activity, null, this)
        val layoutManager = LinearLayoutManager(activity)
        rv_search_history.layoutManager = layoutManager
        rv_search_history.adapter = mSearchHistoryRvAdapter
        rv_search_result.layoutManager = LinearLayoutManager(activity)
        rv_search_result.adapter = mSearchSuggestionRvAdapter
        mCallbackTouch = RecyclerTouchListener(activity, rv_search_history)
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
        setupOnLoadMore(rv_search_result, mCallBackLoadMoreSearchResult)
        showLoading(false)
    }

    fun requestSearchSuggestion() {
        mSearchPresenter?.requestSearchSuggestion(this, Constants.API_KEY, Constants.PART_SNIPPET, Constants.TYPE_CHANNEL
                , mSearchRequest?.nextPageToken, mSearchRequest!!.query)
    }

    fun loadSearchHistory() {
        MRealmPresenter.apply {
            loadSearchHistory(this@FirstSuggestionFragment)
        }
    }

    override fun <T : RealmObject> onChange(realmResult: RealmResults<T>) {
        mSearchHistoryRvAdapter?.data = realmResult as? RealmResults<SearchQueryRealm>?
        mSearchHistoryRvAdapter?.notifyDataSetChanged()
    }

    override fun onClick(position: Int, event: Int) {
        mPosition = position
        mEvent = event
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
        when (mEvent) {
            BaseViewHolder.ACTION_CLICK_CHANNEL -> {
                sendHit(CATEGORY_ACTION, ACTION_CLICK_SEARCH_RESULT_ITEM)
                val itemInfo = mSearchSuggestionRvAdapter?.data!![mPosition]
                if (activity is SearchActivity) {
                    (activity as SearchActivity).onSearchResultReceived(itemInfo)
                }
            }
            BaseViewHolder.ACTION_CLICK_SEARCH_HISTORY -> {
                rv_search_history.visibility = View.GONE
                val item = mSearchHistoryRvAdapter?.data!![mPosition]
                if (activity is SearchActivity) {
                    (activity as SearchActivity).onSearchHistoryClicked(item)
                }
            }
            BaseViewHolder.ACTION_CLICK_FOLLOWING_CHANNEL_REMOVAL -> {
                mCallbackTouch!!.closeVisibleBG(null)
                //remove this history item
//                MRealmPresenter.updateSearchResultRealm(channelId, channelTitle, subscriberCount, description, 0, -1L, thumbnailUrl, false)
            }
        }
    }

    override fun onSearch(query: String, pageToken: String?) {
        callbackShowRv.onShow(rv_search_history, false)
        callbackShowRv.onShow(rv_search_result, true)
        mSearchSuggestionRvAdapter?.data?.clear()
        showLoading(true)
        mCallBackLoadMoreSearchResult.countLoadmore = 0
        isFirstData = true
        mSearchRequest = SearchRequest(query, null, pageToken)
        showMessageBottom(mCallbackDismiss)
        setupOnLoadMore(rv_search_result, mCallBackLoadMoreSearchResult)
        requestSearchSuggestion()
    }

    override fun onClear() {
        mCallbackTouch!!.closeVisibleBG(null)
    }
    fun bindData(searchListResponse: SearchListResponse) {
        hideLoadMoreIndicator()
        showLoading(false)
        val searchResultItems = searchListResponse.items
        if (isFirstData) {
            isFirstData = false
            mSearchSuggestionRvAdapter = SearchSuggestionRvAdapter(activity, searchResultItems, this)
            rv_search_result.adapter = mSearchSuggestionRvAdapter

        } else if (searchResultItems!!.isNotEmpty()) {
            mSearchSuggestionRvAdapter?.data?.addAll(searchResultItems)
            mSearchSuggestionRvAdapter?.notifyDataSetChanged()
        }
        if (isEmptyData(searchResultItems)) {
            isEmptyData()
        } else {
            mSearchRequest?.nextPageToken = searchListResponse.nextPageToken
            mSearchRequest?.prevPageToken = searchListResponse.prevPageToken
        }
    }

    override fun onBackPressed() {
        if (rv_search_history.visibility == View.GONE) {
            callbackShowRv.onShow(rv_search_result, false)
            callbackShowRv.onShow(rv_search_history, true)
            mSearchHistoryRvAdapter?.notifyDataSetChanged()
        } else {
            if (activity is SearchActivity) {
                (activity as SearchActivity).superOnBackPressed()
            }
        }
    }

    val mCallBackLoadMoreSearchResult = object : ICallbackLoadMore() {
        override fun onLoadMore() {
            if (!mSearchPresenter!!.isRequest) {
                if (mSearchRequest?.nextPageToken == null) {
                    return
                }
                requestSearchSuggestion()
                countLoadmore++
                if (countLoadmore > 1) {
                    InputUtils.hideKeyboard(activity)
                }
                mSearchSuggestionRvAdapter?.isLoading = true
                mSearchSuggestionRvAdapter?.data?.add(SearchResult())
                Handler().post { mSearchSuggestionRvAdapter?.notifyItemInserted(mSearchSuggestionRvAdapter!!.itemCount - 1) }
            }
        }
    }

    val callbackShowRv = object : ICallbackShowRv {
        override fun onShow(rv: RecyclerView, isShow: Boolean) {
            rv.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }
    override fun getMinSize() = 0
    override fun getMaxSize() = 20
    override fun <T> isEmptyData(list: MutableList<T>?): Boolean {
        return list == null || list.size <= getMinSize()
    }

    override fun isEmptyData() {
        showLoading(false)
        if (mSearchSuggestionRvAdapter!!.itemCount <= getMinSize()) {
            mCallbackDismiss.apply {
                isShow = true
                showMessageBottom(this)
            }
        }
    }

    override fun onLoadDataFailure() {
        mCallbackLoadChecklist.content = R.string.error_conection
        onLoadDataFailure(mCallbackLoadChecklist)
    }

    override fun onNoInternetConnection() {
        mCallbackLoadChecklist.content = R.string.internet_no_conection
        onNoInternetConnection(mCallbackLoadChecklist)
    }

    override fun hideLoadMoreIndicator() {
        mSearchSuggestionRvAdapter?.apply {
            if (isLoading) {
                data?.apply {
                    val l = itemCount
                    if (l > 0) {
                        removeAt(l - 1)
                        notifyItemRemoved(l)
                    }
                }
                isLoading = false
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