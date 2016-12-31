package net.lc.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import com.jakewharton.rxbinding.widget.RxTextView
import com.tieudieu.util.DebugLog
import kotlinx.android.synthetic.main.app_bar_search.*
import kotlinx.android.synthetic.main.layout_input_text.*
import net.lc.fragments.search.MainSearchFragment
import net.lc.utils.Constants
import net.lc.utils.IndexTag
import net.lc.utils.InputUtil
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by mrvu on 12/28/16.
 */
class SearchActivity : ActionBarSearchActivity() {
    var mMainSearchFragment: MainSearchFragment? = null
    private var time = -1L

    private fun submitSearchSuggestion(query: String) {
        DebugLog.e("submitSearchSuggestion--------------------------------" + query)
        //send to MainSearchFragment
        mMainSearchFragment!!.onSearch(query, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar_back.setOnClickListener { onBackPressed() }
        initEdtSearch()
        mMainSearchFragment = MainSearchFragment(this)
    }

    fun superOnBackPressed() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        InputUtil.hideKeyboard(this)
        if (layout_input_text.visibility == View.GONE) {
            superOnBackPressed()
            return
        }
        mMainSearchFragment!!.onBackPressed()
    }

    override fun onMainScreenRequested() {
        fragmentStackManager.swapFragment(MainSearchFragment.newInstance(this))
    }

    override fun onFragmentEntered(fragment: Fragment?) {
        if (fragment is MainSearchFragment) {
            layout_input_text.visibility = View.VISIBLE
            toolbar_title.visibility = View.GONE
        } else {
            layout_input_text.visibility = View.GONE
            toolbar_title!!.visibility = View.VISIBLE
        }
    }

    override fun onNewScreenRequested(indexTag: Int, typeContent: String?, `object`: Any?) {
        when (indexTag) {
            IndexTag.MAIN_SEARCH -> fragmentStackManager.swapFragment(MainSearchFragment.newInstance(this))
        }
    }

    private fun initEdtSearch() {
        edt_search.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                submitSearch()
                time = System.currentTimeMillis()
                DebugLog.e("time -------------- " + time)
            }
            false
        }
        RxTextView.afterTextChangeEvents(edt_search)
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { tvChangeEvent ->
                    var ok = true
                    if (time != -1L) {
                        val currentTime = System.currentTimeMillis()
                        val l = currentTime - time
                        DebugLog.e(currentTime.toString() + " -------------  " + time + " === " + l)
                        if (l <= Constants.AUTO_LOAD_DURATION) {
                            ok = false
                        }
                    }
                    img_clear.visibility = View.VISIBLE
                    val s = tvChangeEvent.view().text.toString()
                    val text = s.trim { it <= ' ' }
                    DebugLog.e("ok----------------- " + ok)
                    if (ok) {
                        if (text.isNotEmpty()) {
                            submitSearchSuggestion(text)
                        } else {
                            img_clear.visibility = View.GONE
                        }
                    }
                }
    }

    private fun submitSearch() {
        val query = edt_search.text.toString().trim()
        if (query.isEmpty()) return
        mMainSearchFragment!!.onSearch(query, null)
        InputUtil.hideKeyboard(this)
    }


}