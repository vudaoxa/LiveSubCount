package net.lc.presenters

import android.content.Context
import android.content.SharedPreferences
import com.tieudieu.util.DebugLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import net.lc.R
import net.lc.fragments.main.SettingsFragment
import net.lc.models.ChildSetting
import net.lc.models.GroupSettings
import net.lc.utils.Constants
import java.util.*

/**
 * Created by mrvu on 1/23/17.
 */
class SettingsPresenter(val mContext: Context) {
    private var sharedPrefrs: SharedPreferences? = null
    val childSettingsMap = HashMap<Int, ChildSetting>()
    val sharedPreKeys = listOf<String>("night_mode", "sound_on_change", "transition_sub_count", "thumb_channel",
            "screenshot_sharing", "thumb_favorites", "thumb_noti")
    val nameResIds = listOf<Int>(R.string.night_mode, R.string.sound_on_change, R.string.transition_sub_count, R.string.thumb_channel,
            R.string.screenshot_sharing, R.string.thumb_favorites, R.string.thumb_noti)
    val defaultVals = listOf<Boolean>(false, false, true, true, true, false, false)
    var groupSettings: MutableList<GroupSettings>? = null

    fun initSharedPrefrs(mSettingsFragment: SettingsFragment) {
        sharedPrefrs?.apply {
            groupSettings?.apply {
                if (isNotEmpty()) {
                    mSettingsFragment.initSettings(this)
                    return
                }
            }
        }
        sharedPrefrs = mContext.getSharedPreferences(Constants.LC, Context.MODE_PRIVATE)
        val disposableObserver = object : DisposableObserver<MutableList<GroupSettings>>() {
            override fun onComplete() {
                DebugLog.e("initSharedPrefrs------------onComplete")
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

            override fun onNext(t: MutableList<GroupSettings>?) {

                t?.apply {
                    mSettingsFragment.initSettings(t)
                }
            }
        }

        initSettings().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver)
        mSettingsFragment.mDisposables.add(disposableObserver)
    }

    fun getSettingsMapItem(i: Int): ChildSetting = childSettingsMap.get(nameResIds[i]) as ChildSetting
    //    fun getSettingsMapItem(nameResId: Int): ChildSetting = childSettingsMap.get(nameResId) as ChildSetting
    fun initSettings(): Observable<MutableList<GroupSettings>> {
        try {
            for (i in nameResIds.indices) {
                childSettingsMap.put(nameResIds[i], ChildSetting(sharedPreKeys[i], nameResIds[i], getVal(sharedPreKeys[i], defaultVals[i])))
            }

            val general = GroupSettings(R.string.general, mutableListOf(getSettingsMapItem(0)))
            val livecounts = GroupSettings(R.string.livecounts,
                    mutableListOf(getSettingsMapItem(1), getSettingsMapItem(2), getSettingsMapItem(3), getSettingsMapItem(4)))
            val favorites = GroupSettings(R.string.favorites, mutableListOf(getSettingsMapItem(5)))
            val noti = GroupSettings(R.string.noti, mutableListOf(getSettingsMapItem(6)))
            groupSettings = mutableListOf(general, livecounts, favorites, noti)
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
        }
    }
}