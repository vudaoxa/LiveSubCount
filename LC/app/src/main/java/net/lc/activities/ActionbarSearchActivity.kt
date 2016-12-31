package net.lc.activities

import android.os.Bundle
import com.tieudieu.fragmentstackmanager.BaseActivityFragmentStack
import kotlinx.android.synthetic.main.app_bar_search.*
import net.lc.R
import net.lc.fragments.search.MainSearchFragment

/**
 * Created by mrvu on 12/28/16.
 */
abstract class ActionBarSearchActivity : BaseActivityFragmentStack() {
    override fun initViews(savedInstanceState: Bundle?) {
        setup()
        super.initViews(savedInstanceState)
    }

    private fun setup() {
        setSupportActionBar(toolbar)

    }

    override fun getResLayout(): Int {
        return R.layout.activity_search
    }

    override fun getContentFrameId(): Int {
        return R.id.container_search
    }

    override fun getHomeClass(): Class<*> {
        return MainSearchFragment::class.java
    }


}