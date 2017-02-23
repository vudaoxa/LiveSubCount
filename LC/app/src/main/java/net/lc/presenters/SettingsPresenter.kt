package net.lc.presenters

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.Observable
import net.lc.fragments.main.SettingsFragment
import net.lc.models.ChildSetting
import net.lc.models.GroupSettings
import net.lc.models.ICallbackToggleSettings
import net.lc.utils.ACTION_TOGGLE
import net.lc.utils.CATEGORY_ACTION
import net.lc.utils.Constants
import net.lc.utils.sendHit
import net.live.sub.R
import java.util.*

/**
 * Created by mrvu on 1/23/17.
 */
object SettingsPresenter {
    private var sharedPrefrs: SharedPreferences? = null
    private val childSettingsMap = HashMap<Int, ChildSetting>()
    private val sharedPreKeys = listOf<String>("night_mode", "sound_on_change", "more_statistic", "transition_sub_count",
            "thumb_channel", "view_description",
            /*"screenshot_sharing", */"thumb_favorites", "thumb_noti")
    private val nameResIds = listOf<Int>(R.string.night_mode, R.string.sound_on_change, R.string.more_statistic,
            R.string.transition_sub_count, R.string.thumb_channel, R.string.show_description,
            /*R.string.screenshot_sharing,*/ R.string.thumb_favorites, R.string.thumb_noti)
    private val defaultVals = listOf<Boolean>(false, false, false, true, true, true, /*true,*/ false, false)
    private var groupSettings: MutableList<GroupSettings>? = null

    fun initSharedPrefrs(mSettingsFragment: SettingsFragment) {
        sharedPrefrs?.apply {
            groupSettings?.apply {
                if (isNotEmpty()) {
                    mSettingsFragment.initSettings(this)
                    return
                }
            }
        }
    }

    fun getSettingsMapItem(i: Int): ChildSetting = childSettingsMap.get(nameResIds[i]) as ChildSetting
    //    fun getSettingsMapItem(nameResId: Int): ChildSetting = childSettingsMap.get(nameResId) as ChildSetting
    fun initSettings(mContext: Context): Observable<MutableList<GroupSettings>> {
        sharedPrefrs = mContext.getSharedPreferences(Constants.LC, Context.MODE_PRIVATE)
        try {
            for (i in nameResIds.indices) {
                childSettingsMap.put(nameResIds[i], ChildSetting(sharedPreKeys[i], nameResIds[i], getVal(sharedPreKeys[i], defaultVals[i])))
            }
//            var i=0
            var i = 1
//            val general = GroupSettings(R.string.general, mutableListOf(getSettingsMapItem(i++)), false)
            val livecounts = GroupSettings(R.string.livecounts,
                    mutableListOf(getSettingsMapItem(i++), getSettingsMapItem(i++), getSettingsMapItem(i++), getSettingsMapItem(i++),
                            getSettingsMapItem(i++)/*, getSettingsMapItem(i++)*/))
            val favorites = GroupSettings(R.string.favorites, mutableListOf(getSettingsMapItem(i++)))
            val noti = GroupSettings(R.string.noti, mutableListOf(getSettingsMapItem(i)))
//            groupSettings = mutableListOf(general, livecounts, favorites, noti)
            groupSettings = mutableListOf(livecounts, favorites, noti)
            return Observable.just(groupSettings)
        } catch (e: Exception) {
            e.printStackTrace()
            return Observable.just(null)
        }
    }

    fun getVal(key: String, defaultVal: Boolean): Boolean = sharedPrefrs!!.getBoolean(key, defaultVal)
    fun commitValue(key: String, value: Boolean) {
        sharedPrefrs?.apply {
            val editor = edit()
            editor.putBoolean(key, value)
            editor.commit()
        }
    }

    fun toggleChildSetting(nameResId: Int) {
        val childSetting = childSettingsMap.get(nameResId) as ChildSetting
        childSetting.apply {
            checked = !checked
            commitValue(label, checked)
            sendHit(CATEGORY_ACTION, ACTION_TOGGLE + "__" + label)
        }

        when (nameResId) {
            R.string.thumb_noti -> {
                notificationsFragment?.apply {
                    onToggleSettings()
                }
            }
            R.string.thumb_favorites -> {
                followingChannelsFragment?.apply {
                    onToggleSettings()
                }
            }
        }
    }

    var notificationsFragment: ICallbackToggleSettings? = null
    var followingChannelsFragment: ICallbackToggleSettings? = null
}