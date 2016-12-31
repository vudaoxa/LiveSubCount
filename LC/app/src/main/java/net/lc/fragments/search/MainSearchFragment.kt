package net.lc.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import kotlinx.android.synthetic.main.fragment_main_search.*
import net.lc.R
import net.lc.activities.SearchActivity
import net.lc.adapters.search.MainSearchPagerAdapter
import net.lc.interfaces.IBack
import net.lc.interfaces.ICallbackSearch

/**
 * Created by mrvu on 12/28/16.
 */
class MainSearchFragment(val searchActivity: SearchActivity) : BaseFragmentStack(), ICallbackSearch, IBack {
    //    val vp_main_search: CustomViewPager by bindView(R.id.vp_main_search)
    companion object {
        fun newInstance(searchActivity: SearchActivity): MainSearchFragment {
            val fragment = MainSearchFragment(searchActivity)
            searchActivity.mMainSearchFragment = fragment
            return fragment
        }
    }

    private var mMainSearchPagerAdapter: MainSearchPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        DebugLog.e((vp_main_search == null).toString())
        mMainSearchPagerAdapter = MainSearchPagerAdapter(childFragmentManager)
//        vp_main_search.offscreenPageLimit=3
        vp_main_search.setPagingEnabled(false)
        vp_main_search.adapter = mMainSearchPagerAdapter
    }

    override fun onSearch(query: String, pageToken: String?) {
//        DebugLog.e((vp_main_search == null).toString())
        if (vp_main_search != null) {
            vp_main_search.currentItem = 0
        }

    }

    override fun onBackPressed() {
        if (vp_main_search == null) return
        if (vp_main_search.currentItem == 0) {
            searchActivity.superOnBackPressed()
        } else {
            vp_main_search.currentItem = 0
        }
    }
}