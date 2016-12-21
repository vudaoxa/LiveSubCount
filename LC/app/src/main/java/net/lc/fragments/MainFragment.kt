package net.lc.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import kotlinx.android.synthetic.main.fragment_main.*
import net.lc.R
import net.lc.adapters.MainPagerAdapter

/**
 * Created by HP on 11/28/2016.
 */
class MainFragment : BaseFragmentStack(){
    val size = 5
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.fragment_main, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
    }

    fun initUI(){
        if(vp_main!=null){
            vp_main.adapter = MainPagerAdapter(activity, fragmentManager, size)
            nts.setViewPager(vp_main, 2)
        }


    }
}
