package net.lc.views

import android.content.Context
import android.widget.TextView
import net.lc.models.ICallbackEditFav
import net.lc.utils.anim

/**
 * Created by mrvu on 1/27/17.
 */
class ActionBarTextBtn(val context: Context, val tv: TextView, val textResId: List<Int>, var clicked: Int) {
    init {
        initFields()
    }

    var mCallbackEditFav: ICallbackEditFav? = null
    fun initFields() {
        tv.setText(textResId[clicked])
        tv.setOnClickListener {
            clicked = 1 - clicked
            tv.startAnimation(anim)
            tv.setText(textResId[clicked])
            mCallbackEditFav?.apply {
                onEditClicked(clicked)
            }
        }
    }
}