package net.lc.fragments.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.IoniconsIcons
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import kotlinx.android.synthetic.main.fragment_main.*
import net.lc.R
import net.lc.adapters.MainPagerAdapter
import net.lc.views.gigamole.NavigationTabBar
import java.util.*

/**
 * Created by HP on 11/28/2016.
 */
class MainFragment : BaseFragmentStack() {
    val size = 5
    private var index = 2
    private val color = Color.RED
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
    }

    fun initUI() {
        vp_main.adapter = MainPagerAdapter(activity, fragmentManager, size)
        setupNTB()
    }

    private val ICON_COLOR = android.R.color.white
    fun setupNTB() {
        val models = ArrayList<NavigationTabBar.Model>()
        addModels(models, size)

        ntb_horizontal.models = models
        ntb_horizontal.setViewPager(vp_main, index)

        ntb_horizontal.post {
            (vp_main.layoutParams as ViewGroup.MarginLayoutParams).topMargin = (-ntb_horizontal.badgeMargin).toInt()
            vp_main.requestLayout()
        }
    }

    private fun addModels(models: ArrayList<NavigationTabBar.Model>, size: Int) {
        val titles = resources.getStringArray(R.array.titles)
        val icons = listOf<IoniconsIcons>(IoniconsIcons.ion_ios_bell, IoniconsIcons.ion_ios_star, IoniconsIcons.ion_arrow_graph_up_right,
                IoniconsIcons.ion_gear_b, IoniconsIcons.ion_ios_information)
        for (i in 0..size - 1) {
            models.add(
                    NavigationTabBar.Model.Builder(
                            IconDrawable(activity,
                                    icons[i])
                                    .colorRes(ICON_COLOR)
                                    .actionBarSize(), color).title(titles[i]).build())
        }


    }
}
