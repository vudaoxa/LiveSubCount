package net.lc.fragments.search


import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmObject
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_search_suggestions.*
import net.lc.R
import net.lc.activities.SearchActivity
import net.lc.adapters.search.SearchHistoryRvAdapter
import net.lc.adapters.search.SearchSuggestionRvAdapter
import net.lc.fragments.MBaseFragment
import net.lc.holders.BaseViewHolder
import net.lc.models.*
import net.lc.presenters.MRealmPresenter
import net.lc.presenters.SearchPresenter
import net.lc.utils.CallbackBottomMessage
import net.lc.utils.Constants
import net.lc.utils.InputUtils
import rx.subscriptions.CompositeSubscription

/**
 * Created by mrvu on 12/28/16.
 */
class FirstSuggestionFragment(val mMainSearchFragment: MainSearchFragment) : MBaseFragment(),
        ICallbackOnClick, ICallbackSearch,
        ILoadData, IBackListener, IConnect, ICallbackRealmResultChange {
    companion object {
        fun newInstance(mMainSearchFragment: MainSearchFragment): FirstSuggestionFragment {
            val fragment = FirstSuggestionFragment(mMainSearchFragment)
            mMainSearchFragment.mCallbackSearch = fragment
            mMainSearchFragment.mBackListener = fragment
            return fragment
        }
    }

    private var mRealmPresenter: MRealmPresenter? = null
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
        mRealmPresenter = MRealmPresenter()
        mSearchPresenter?.mRealmPresenter = mRealmPresenter
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
    override fun onDestroy() {
        super.onDestroy()
        mDisposables.dispose()
        mSubscription.unsubscribe()
    }

    fun initFields() {
        mSearchSuggestionRvAdapter = SearchSuggestionRvAdapter(activity, null, this)
        mSearchHistoryRvAdapter = SearchHistoryRvAdapter(activity, null, this)
        rv_search_history.layoutManager = LinearLayoutManager(activity)
        rv_search_history.adapter = mSearchHistoryRvAdapter
        rv_search_result.layoutManager = LinearLayoutManager(activity)
        rv_search_result.adapter = mSearchSuggestionRvAdapter
        setupOnLoadMore(rv_search_result, mCallBackLoadMoreSearchResult)
        showLoading(false)
    }

    fun requestSearchSuggestion() {
        mSearchPresenter?.requestSearchSuggestion(this, Constants.API_KEY, Constants.PART_SNIPPET, Constants.TYPE_CHANNEL
                , mSearchRequest?.nextPageToken, mSearchRequest!!.query)
    }

    fun loadSearchHistory() {
        mRealmPresenter?.apply {
            loadSearchHistory(this@FirstSuggestionFragment)
        }
    }

    override fun <T : RealmObject> onChange(realmResult: RealmResults<T>) {
        mSearchHistoryRvAdapter?.data = realmResult as? RealmResults<SearchQueryRealm>?
        mSearchHistoryRvAdapter?.notifyDataSetChanged()
    }

    override fun onClick(position: Int, event: Int) {
        when (event) {
            BaseViewHolder.ACTION_CLICK_CHANNEL -> {
                val itemInfo = mSearchSuggestionRvAdapter?.data!![position]
                if (activity is SearchActivity) {
                    (activity as SearchActivity).onSearchResultReceived(itemInfo)
                }
            }
            BaseViewHolder.ACTION_CLICK_SEARCH_HISTORY -> {
                rv_search_history.visibility = View.GONE
                val item = mSearchHistoryRvAdapter?.data!![position]
                if (activity is SearchActivity) {
                    (activity as SearchActivity).onSearchHistoryClicked(item)
                }
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
                if (countLoadmore > 0) {
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
            mCallbackDismiss.isShow = true
            showMessageBottom(mCallbackDismiss)
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
        if (mSearchSuggestionRvAdapter!!.isLoading) {
            mSearchSuggestionRvAdapter?.data?.removeAt(mSearchSuggestionRvAdapter!!.itemCount - 1)
            mSearchSuggestionRvAdapter?.notifyItemRemoved(mSearchSuggestionRvAdapter!!.itemCount)
            mSearchSuggestionRvAdapter?.isLoading = false
        }
    }
}