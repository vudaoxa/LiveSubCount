package net.lc.adapters

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import net.lc.fragments.main.*

/**
 * Created by HP on 11/28/2016.
 */
class MainPagerAdapter(val context: Context, val mMainFragment: MainFragment, fm: FragmentManager, val size: Int) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return size
    }

    override fun getItem(position: Int): BaseFragmentStack {
        val fragment = when (position) {
            0 -> NotificationsFragment.newInstance(mMainFragment)
            1 -> FollowingChannelsFragment.newInstance(mMainFragment)
            2 -> LiveCountFragment.newInstance(mMainFragment)
            3 -> SettingsFragment()
            4 -> InfoFragment()
            else->null
        }
        return fragment!!
    }
}
