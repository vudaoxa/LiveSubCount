package net.lc.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import net.lc.R

/**
 * Created by HP on 11/28/2016.
 */
class NotificationsFragment : BaseFragmentStack(){
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.fragment_foo, container, false)
//        initUI()
        return view
    }
}