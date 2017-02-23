package net.lc.models

import android.content.Context
import android.text.TextUtils
import android.widget.EditText
import net.lc.holders.TYPE_MILESTONE
import net.lc.holders.TYPE_SUBSCRIBER_AWAY
import net.live.sub.R

/**
 * Created by tusi on 2/2/17.
 */
class ToggleNotis(val mContext: Context, val labelResId: Int, val rSearchResult: RSearchResult,
                  val type: String, var checked: Boolean) {
    var editText: EditText? = null
    var value: Long? = 0
    //    fun getMilestone():Long{
//        rSearchResult.apply {
//            return getMilestone()
//        }
//        return -1
//    }
    fun validate(): Boolean {
        editText?.apply {
            if (TextUtils.isEmpty(text)) {
                error = mContext.getString(R.string.required_field)
                return false
            }
            val x = text.toString().toLong()
            when (type) {
                TYPE_MILESTONE -> {
                    rSearchResult.subscriberCount?.apply {
                        if (x <= toLong()) {
                            error = mContext.getString(R.string.required_greater, this)
                            return false
                        }
                        value = x
                        return true
                    }
                }
                TYPE_SUBSCRIBER_AWAY -> {
                    if (x <= 0) {
                        error = mContext.getString(R.string.required_greater, "0")
                        return false
                    }
                    value = x
                    return true
                }
            }

        }
        return false
    }
}
