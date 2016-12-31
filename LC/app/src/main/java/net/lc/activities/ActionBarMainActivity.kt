package net.lc.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tieudieu.fragmentstackmanager.BaseActivityFragmentStack
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.item_main_action_btns.*
import kotlinx.android.synthetic.main.layout_input_text.*
import net.lc.R
import net.lc.fragments.main.MainFragment
import net.lc.utils.InputUtil

/**
 * Created by HP on 11/28/2016.
 */

abstract class ActionBarMainActivity : BaseActivityFragmentStack() {
    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setup()
    }
    fun setup(){
        if(toolbar != null){
            setSupportActionBar(toolbar)
            supportActionBar!!.elevation=0f
            supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        }
        toolbar_back.setOnClickListener {
//            val fragment = fragmentStackManager.currentFragment
//            if(fra)
        }
        btn_search.setOnClickListener { goSearch() }
        btn_twitter.setOnClickListener {  }
        btn_star.setOnClickListener {  }

    }

    fun goSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivityForResult(intent, 123)
    }

    override fun getResLayout(): Int {
        return R.layout.activity_main
    }

    override fun getContentFrameId(): Int {
        return R.id.container
    }

    override fun getHomeClass(): Class<*> {
        return MainFragment::class.java
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(layout_input_text.visibility == View.VISIBLE){
            InputUtil.hideKeyboard(this)
            if(!TextUtils.isEmpty(edt_search.text.toString().trim())){
                edt_search.text=null
                return
            }else{
                layout_input_text.visibility = View.GONE
            }
        }
    }

    fun setToolbarTitle(title:String){
        toolbar_title.text = if(!TextUtils.isEmpty(title)) title else getString(R.string.app_name)
    }
    fun showBtnBack(){
        toolbar_back.visibility = View.VISIBLE
    }
//    fun signalOnClickBtnSearch(){
//        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Constants.ACTION_ONCLICK_BTN_SEARCH))
//    }
//    fun signalOnClickBtnTwitter(){
//        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Constants.ACTION_ONCLICK_BTN_TWITTER))
//    }
//    fun signalBackFragment(){
//        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Constants.ACTION_ONCLICK_BACK_FRAGMENT))
//    }

}