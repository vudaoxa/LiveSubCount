//package net.lc.holders
//
//import android.content.Context
//import android.support.v7.widget.RecyclerView
//import android.view.View
//import net.lc.models.ICallbackOnClick
//
//
///**
// * Created by mrvu on 12/29/16.
// */
//abstract class BaseSettingsViewHolder(protected var context: Context,
//                              itemView: View) : RecyclerView.ViewHolder(itemView) {
//    abstract fun bindView(obj: Any, position: Int)
//    var mPosition: Int = 0
//
//    companion object {
//        val ACTION_CLICK_CHANNEL = 0
//    }
//}