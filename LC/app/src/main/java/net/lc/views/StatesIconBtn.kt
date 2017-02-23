package net.lc.views

import android.content.Context
import android.widget.ImageButton
import com.joanzapata.iconify.IconDrawable

/**
 * Created by mrvu on 1/29/17.
 */
class StatesIconBtn(val context: Context, val img: ImageButton, val icons: List<IconDrawable>, var enabled: Int) {
    init {
        initFields()
    }

    var position = 0
    fun initFields() {
        img.setImageDrawable(icons[enabled])
    }

    fun onClicked() {
        enabled = 1 - enabled
        img.setImageDrawable(icons[enabled])
    }
}