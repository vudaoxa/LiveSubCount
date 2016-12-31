package net.lc.fragments.search

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.api.services.youtube.model.SearchListResponse
import com.google.api.services.youtube.model.SearchResult
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_search_suggestions.*
import kotlinx.android.synthetic.main.layout_progress_view.*
import net.lc.R
import net.lc.adapters.search.SearchSuggestionRvAdapter
import net.lc.fragments.MBaseFragment
import net.lc.interfaces.ICallbackLoadMore
import net.lc.interfaces.ICallbackOnClick
import net.lc.interfaces.ICallbackSearch
import net.lc.interfaces.ILoadData
import net.lc.presenters.SearchPresenter

/**
 * Created by mrvu on 12/28/16.
 */
class FirstSuggestionFragment : MBaseFragment(), ICallbackOnClick, ICallbackSearch, ICallbackLoadMore, ILoadData {
    private var mSearchPresenter: SearchPresenter? = null
    private var mSearchSuggestionRvAdapter: SearchSuggestionRvAdapter? = null
    private var isFirstData = true
    var nextPageToken: String? = null
    var query: String? = null
    val disposables = CompositeDisposable()
    private val onClickListenerLoadChecklist = View.OnClickListener {
        showLoading(true)
        showMessageBottom(false, R.string.OK, 0, false, null)
        loadChecklists()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSearchPresenter = SearchPresenter()
        mSearchSuggestionRvAdapter = SearchSuggestionRvAdapter(activity, null, this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_search_suggestions, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    fun initFields() {
        rv.layoutManager = LinearLayoutManager(activity)
        setupOnLoadMore(rv, this)
    }

    fun loadChecklists() {
        mSearchPresenter!!.requestSearchSuggestion(this, query!!, nextPageToken)
    }

    override fun onClick(position: Int, event: Int) {

    }

    override fun onSearch(query: String, pageToken: String?) {
        isFirstData = true
        showMessageBottom(false, R.string.OK, 0, false, null)
        setupOnLoadMore(rv, this)
        loadChecklists()
    }

    fun bindData(searchListResponse: SearchListResponse) {
        val searchResultItems = searchListResponse.items
        hideLoadMoreIndicator()
        showLoading(false)
        if (isFirstData) {
            isFirstData = false
            mSearchSuggestionRvAdapter = SearchSuggestionRvAdapter(activity, searchResultItems, this)
            rv.adapter = mSearchSuggestionRvAdapter

        } else if (searchResultItems.isNotEmpty()) {
            mSearchSuggestionRvAdapter!!.addAllItems(searchResultItems)
            mSearchSuggestionRvAdapter!!.notifyDataSetChanged()
        }
        if (isEmptyData(searchResultItems)) {
            isEmptyData()
        } else {
            nextPageToken = searchListResponse.nextPageToken
        }
    }


    override fun onLoadMore() {
        if (!mSearchPresenter!!.isRequest) {
            loadChecklists()
            if (avi.visibility == View.GONE) {
                mSearchSuggestionRvAdapter!!.addItem(SearchResult())
                mSearchSuggestionRvAdapter!!.isLoading = true
                Handler().post { mSearchSuggestionRvAdapter!!.notifyItemInserted(mSearchSuggestionRvAdapter!!.itemCount - 1) }
            }
        }
    }

    override fun getMinSize() = 0

    override fun <T> isEmptyData(list: MutableList<T>?): Boolean {
        return list == null || list.size <= getMinSize()
    }

    override fun isEmptyData() {
        if (mSearchSuggestionRvAdapter!!.itemCount <= getMinSize()) {
            showMessageBottom(true, R.string.empty_data, R.string.OK,
                    false, onClickListenerDismiss)
        }
    }

    override fun onLoadDataFailure() {
        onLoadDataFailure(onClickListenerLoadChecklist)
    }

    override fun hideLoadMoreIndicator() {

    }

    override fun showLoading(isShow: Boolean) {
        if (mSearchSuggestionRvAdapter != null && mSearchSuggestionRvAdapter!!.isLoading) {
            return
        }
        super.showLoading(isShow)
    }
}