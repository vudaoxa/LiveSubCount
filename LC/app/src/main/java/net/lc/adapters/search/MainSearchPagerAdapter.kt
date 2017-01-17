package net.lc.adapters.search

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import net.lc.fragments.search.FirstSuggestionFragment
import net.lc.fragments.search.MainSearchFragment

/**
 * Created by HP on 10/20/2016.
 */

class MainSearchPagerAdapter(val mMainSearchFragment: MainSearchFragment, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    val SIZE = 1
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return FirstSuggestionFragment.newInstance(mMainSearchFragment)
//            1 -> return SearchSubmitFragment.newInstance()
//            2 -> return SearchDoctorFilterFragment.newInstance()
        }
        return null
    }

    override fun getCount(): Int {
        return SIZE
    }
}
