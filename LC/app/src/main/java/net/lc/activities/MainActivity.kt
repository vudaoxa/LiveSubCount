package net.lc.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import com.tieudieu.util.DebugLog
import net.lc.fragments.main.MainFragment
import net.lc.models.ICallbackFollowing
import net.lc.models.ICallbackSearchResult
import net.lc.models.SearchResult
import net.lc.utils.Constants
import net.lc.utils.IndexTags

class MainActivity : ActionBarMainActivity() {
    var mCallbackSearchResult: ICallbackSearchResult? = null
    var mCallbackFollowing: ICallbackFollowing? = null
    override fun onMainScreenRequested() {
        fragmentStackManager.clearStack()
        fragmentStackManager.swapFragment(MainFragment.newInstance(this))
    }

    override fun goSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivityForResult(intent, Constants.ACTIVITY_SEARCH_CODE)
    }

    override fun onTwitter() {

    }

    override fun onFollowing() {
        mCallbackFollowing?.onFollowing()
    }
    override fun onFragmentEntered(fragment: Fragment?) {
        if ((fragment as BaseFragmentStack).showBackButton()) {
            showBtnBack()
        }

    }

    override fun onNewScreenRequested(indexTag: Int, typeContent: String?, `object`: Any?) {
        when (indexTag) {
            IndexTags.FRAGMENT_MAIN -> {
                fragmentStackManager.clearStack()
                fragmentStackManager.swapFragment(MainFragment.newInstance(this))
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()
        DebugLog.e("onStop--------------------------")
//        Realm.getDefaultInstance().close()
    }
    override fun onDestroy() {
        super.onDestroy()
        DebugLog.e("onDestroy-------------------------")
//        Realm.getDefaultInstance().close()
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
