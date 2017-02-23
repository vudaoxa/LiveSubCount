package net.lc.fragments.main


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.IoniconsIcons
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_ads_smart_banner.*
import net.lc.activities.MainActivity
import net.lc.adapters.MainPagerAdapter
import net.lc.models.*
import net.lc.utils.loadBannerAds
import net.lc.views.gigamole.NavigationTabBar
import net.live.sub.R
import java.util.*

/**
 * Created by HP on 11/28/2016.
 */
class MainFragment(mContext: Context) : BaseFragmentStack(), ICallbackSearchResult,
        ICallbackFollowing, ICallbackSearchResultRealm, ICallbackEditFav, ICallBackBadge, ICallbackSharing {
    companion object {
        fun newInstance(mContext: Context): MainFragment {
            val fragment = MainFragment(mContext)
            if (mContext is MainActivity) {
                mContext.mCallbackSearchResult = fragment
                mContext.mCallbackFollowing = fragment
                mContext.mCallbackEditFav = fragment
                mContext.mCallbackSharing = fragment
                fragment.mCallbackTabMove = mContext
            }
            return fragment
        }
    }

    private val ICON_COLOR = android.R.color.white
    val SIZE = 5
    val PIVOT = 2
    var index = PIVOT
    private val color = Color.TRANSPARENT
    //    var mCallbackCheckFollowing:ICallbackCheckFollowing?=null
    var mCallbackEditFav: ICallbackEditFav? = null
    var mCallbackTabMove: ICallbackTabMove? = null
    var mCallbackSearchResult: ICallbackSearchResult? = null
    var mCallbackFollowing: ICallbackFollowing? = null
    var mCallbackSharing: ICallbackSharing? = null
    var mCallbackSearchResultRealm: ICallbackSearchResultRealm? = null
    var created = false
    fun move2Index(i: Int) {
        vp_main.currentItem = i
        index = i
    }

    override fun onSharing() {
        mCallbackSharing?.onSharing()
    }

    override fun onEditClicked(edit: Int) {
        mCallbackEditFav?.onEditClicked(edit)
    }

    override fun onSearchResultReceived(searchResult: SearchResult) {
        move2Index(PIVOT)
        mCallbackSearchResult?.onSearchResultReceived(searchResult)
    }

    override fun onSearchResultRealmReceived(searchResultRealm: RSearchResult) {
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
        vp_main.offscreenPageLimit = 5
        setupNTB()
        loadBannerAds(ad_view)
    }

    fun setupNTB() {
        val models = ArrayList<NavigationTabBar.Model>()
        addModels(models, SIZE)

        ntb_horizontal.models = models
        ntb_horizontal.setViewPager(vp_main, index)

        ntb_horizontal.post {
            (vp_main.layoutParams as ViewGroup.MarginLayoutParams).topMargin = (-ntb_horizontal.badgeMargin).toInt()
            vp_main.requestLayout()
        }
        vp_main.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mCallbackTabMove?.onTabMove(position)
                index = position
            }
        })

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

    override fun updateBadge() {

    }

    private var countNotis: Int = 0
    val BADGE_DURATION = 100L
    fun setBadge(x: Int) {
//        if(x == countNotis) return
        ntb_horizontal?.apply {
            val model = models[0]
            countNotis = x
            postDelayed({
                model.apply {
                    if (countNotis <= 0) {
                        hideBadge()
                    } else {
                        val title = countNotis.toString()
                        if (!isBadgeShowed) {
                            badgeTitle = title
                            showBadge()
                        } else
                            updateBadgeTitle(title)
                    }
                }
            }, BADGE_DURATION)
        }
    }

    fun increBadge(incre: Int) {
        ntb_horizontal?.apply {
            val model = models[0]
            countNotis += incre
            postDelayed({
                model.apply {
                    if (countNotis <= 0) {
                        hideBadge()
                    } else {
                        val title = this.toString()
                        if (!isBadgeShowed) {
                            badgeTitle = title
                            showBadge()
                        } else
                            updateBadgeTitle(title)
                    }
                }
            }, BADGE_DURATION)
        }
    }


}
