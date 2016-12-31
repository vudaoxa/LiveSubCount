package net.lc.adapters

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import net.lc.fragments.main.*

/**
 * Created by HP on 11/28/2016.
 */
class MainPagerAdapter(val context: Context, fm: FragmentManager, val size: Int) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        //throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
        return size
    }

    override fun getItem(position: Int): BaseFragmentStack {
        //throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
        val fragment = when (position) {
            0 -> NotificationsFragment()
            1 -> FavoritesFragment()
            2 -> LiveCountFragment()
            3 -> SettingsFragment()
            4 -> InfoFragment()
            else->null
        }
        return fragment!!
    }

}
