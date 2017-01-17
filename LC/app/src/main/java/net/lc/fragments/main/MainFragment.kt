package net.lc.fragments.main

import android.content.Context
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
import net.lc.activities.MainActivity
import net.lc.adapters.MainPagerAdapter
import net.lc.models.*


import net.lc.views.gigamole.NavigationTabBar
import java.util.*

/**
 * Created by HP on 11/28/2016.
 */
class MainFragment(mContext: Context) : BaseFragmentStack(), ICallbackSearchResult,
        ICallbackFollowing, ICallbackSearchResultRealm {
    val SIZE = 5
    val PIVOT = 2
    private var index = PIVOT
    private val color = Color.RED
    var mCallbackSearchResult: ICallbackSearchResult? = null
    var mCallbackFollowing: ICallbackFollowing? = null
    var mCallbackSearchResultRealm: ICallbackSearchResultRealm? = null
    var created: Boolean? = false
    fun move2Index(i: Int) {
        vp_main.currentItem = i
//        vp_main.offscreenPageLimit=5
        index = i
    }

    override fun onSearchResultReceived(searchResult: SearchResult) {
        move2Index(PIVOT)
        mCallbackSearchResult?.onSearchResultReceived(searchResult)
    }

    override fun onSearchResultRealmReceived(searchResultRealm: SearchResultRealm) {
        move2Index(PIVOT)
        mCallbackSearchResultRealm?.onSearchResultRealmReceived(searchResultRealm)
    }

    override fun onFollowing() {
        mCallbackFollowing?.onFollowing()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        created = true
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
    }

    fun initUI() {
        vp_main.adapter = MainPagerAdapter(activity, this, fragmentManager, SIZE)
        setupNTB()
    }

    private val ICON_COLOR = android.R.color.white
    fun setupNTB() {
        val models = ArrayList<NavigationTabBar.Model>()
        addModels(models, SIZE)

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

    companion object {
        fun newInstance(mContext: Context): MainFragment {
            val fragment = MainFragment(mContext)
            if (mContext is MainActivity) {
                mContext.mCallbackSearchResult = fragment
                mContext.mCallbackFollowing = fragment
            }
            return fragment
        }
    }
}
