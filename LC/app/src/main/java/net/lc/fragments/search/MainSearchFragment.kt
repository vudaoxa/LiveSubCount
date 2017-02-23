package net.lc.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import kotlinx.android.synthetic.main.fragment_main_search.*
import net.lc.activities.SearchActivity
import net.lc.adapters.search.MainSearchPagerAdapter
import net.lc.models.IBackListener
import net.lc.models.ICallbackSearch
import net.live.sub.R


/**
 * Created by mrvu on 12/28/16.
 */
class MainSearchFragment(val searchActivity: SearchActivity) : BaseFragmentStack(),
        ICallbackSearch, IBackListener {
    companion object {
        fun newInstance(searchActivity: SearchActivity): MainSearchFragment {
            val fragment = MainSearchFragment(searchActivity)
            searchActivity.mCallbackSearch = fragment
            searchActivity.mBackListener = fragment
            return fragment
        }
    }

    var mCallbackSearch: ICallbackSearch? = null
    var mBackListener: IBackListener? = null
    private var mMainSearchPagerAdapter: MainSearchPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_main_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mMainSearchPagerAdapter = MainSearchPagerAdapter(this, childFragmentManager)
//        vp_main_search.offscreenPageLimit=3
        vp_main_search.setPagingEnabled(false)
        vp_main_search.adapter = mMainSearchPagerAdapter
    }

    override fun onSearch(query: String, pageToken: String?) {
        vp_main_search.currentItem = 0
        mCallbackSearch?.onSearch(query, pageToken)
    }

    override fun onBackPressed() {
        if (vp_main_search.currentItem == 0) {
            mBackListener?.onBackPressed()
//            searchActivity.superOnBackPressed()
        } else {
            vp_main_search.currentItem = 0
        }
    }
}