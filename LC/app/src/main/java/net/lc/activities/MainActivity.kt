package net.lc.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import com.tieudieu.util.DebugLog
import kotlinx.android.synthetic.main.app_bar_search.*
import kotlinx.android.synthetic.main.item_main_action_btns.*
import kotlinx.android.synthetic.main.layout_input_text.*
import net.lc.fragments.main.FollowingChannelsFragment
import net.lc.fragments.main.InitialNotificationsFragment
import net.lc.fragments.main.MainFragment
import net.lc.models.*
import net.lc.utils.*

class MainActivity : ActionBarMainActivity(), ICallbackTabMove {
    var mCallbackSearchResult: ICallbackSearchResult? = null
    var mCallbackFollowing: ICallbackFollowing? = null
    var mCallbackSharing: ICallbackSharing? = null
    var mBackListener: IBackListener? = null
    var mCallbackSubmit: ICallbackSubmit? = null

    override fun onMainScreenRequested() {
        fragmentStackManager.clearStack()
        fragmentStackManager.swapFragment(MainFragment.newInstance(this))
    }

    override fun goSearch() {
        sendHit(CATEGORY_ACTION, ACTION_GO_SEARCH)
        val intent = Intent(this, SearchActivity::class.java)
        startActivityForResult(intent, Constants.ACTIVITY_SEARCH_CODE)
    }

    override fun onSharing() {
        sendHit(CATEGORY_ACTION, ACTION_SHARE)
        mCallbackSharing?.onSharing()
    }

    override fun onFollowing() {
        sendHit(CATEGORY_ACTION, ACTION_FOLLOWING)
        mCallbackFollowing?.onFollowing()
    }

    override fun onSubmit() {
        sendHit(CATEGORY_ACTION, ACTION_INIT_NOTIS_SUBMIT)
        mCallbackSubmit?.onSubmit()
    }

    override fun onTabMove(i: Int) {
        sendHit(CATEGORY_ACTION, ACTION_MOVE_TAB)
        when (i) {
            0, 3, 4 -> {
                tv_edit.visibility = View.GONE
                layout_btns_fixed.visibility = View.GONE
//                btn_submit.visibility=View.GONE
            }
            1 -> {
                tv_edit.startAnimation(anim)
                tv_edit.visibility = View.VISIBLE
                layout_btns_fixed.visibility = View.GONE
                btn_submit.visibility = View.GONE
            }
            2 -> {
                tv_edit.visibility = View.GONE
                layout_btns_fixed.visibility = View.VISIBLE
                layout_btns_fixed.startAnimation(anim)
            }
        }
    }

    override fun onFragmentEntered(fragment: Fragment?) {
        setToolbarTitle((fragment as BaseFragmentStack).title)
        showBtnBack(fragment.showBackButton())
        if (fragment is InitialNotificationsFragment) {
            layout_btns_fixed.visibility = View.GONE
            tv_edit.visibility = View.GONE
            btn_submit.visibility = View.VISIBLE
            btn_submit.startAnimation(anim)
        } else if (fragment is MainFragment) {
            onTabMove(fragment.index)
        }
    }

    override fun onNewScreenRequested(indexTag: Int, typeContent: String?, obj: Any?) {
        when (indexTag) {
            IndexTags.FRAGMENT_MAIN -> {
                fragmentStackManager.clearStack()
                fragmentStackManager.swapFragment(MainFragment.newInstance(this))
            }
        }
    }

    override fun onNewScreenRequested(indexTag: Int, fragment: Fragment?, obj: Any?) {
        when (indexTag) {
            IndexTags.INIT_NOTIS -> {
                fragmentStackManager.swapFragment(InitialNotificationsFragment.newInstance(
                        fragment as FollowingChannelsFragment, obj as RSearchResult))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar_back.setOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()
        DebugLog.e("onStop--------------------------")
    }

    override fun onDestroy() {
        super.onDestroy()
        DebugLog.e("onDestroy-------------------------")
    }

    override fun onBackPressed() {
        InputUtils.hideKeyboard(this)
        if (layout_input_text.visibility == View.GONE) {
            super.onBackPressed()
            return
        }
        mBackListener?.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode != Constants.ACTIVITY_SEARCH_CODE) {
            return
        }
        val searchResult = data?.extras?.getSerializable(Constants.ACTION_CLICK_SEARCH_RESULT) as SearchResult
        DebugLog.e(searchResult.snippet?.title)
        mCallbackSearchResult?.onSearchResultReceived(searchResult)
    }

}
