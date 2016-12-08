package net.lc.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import com.tieudieu.util.DebugLog
import net.lc.R

/**
 * Created by HP on 11/28/2016.
 */
class LiveCountFragment : BaseFragmentStack(){
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.fragment_live_count, container, false)
//        initUI()
        return view
    }

    override fun onResume() {
        super.onResume()
        DebugLog.e("onresume-------------------------------------")
    }

    override fun onPause() {
        super.onPause()
        DebugLog.e("onpause---------------------+++")
    }

}