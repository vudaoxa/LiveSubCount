package net.lc

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import android.view.View
import com.tieudieu.fragmentstackmanager.BaseActivityFragmentStack
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.item_main_action_btns.*
import kotlinx.android.synthetic.main.layout_input_text.*
import net.lc.fragments.MainFragment
import net.lc.utils.Constants
import net.lc.utils.InputUtil

/**
 * Created by HP on 11/28/2016.
 */

 abstract class ActionBarActivity : BaseActivityFragmentStack() {
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
        btn_search.setOnClickListener {  }
        btn_twitter.setOnClickListener {  }
        btn_star.setOnClickListener {  }

    }
    fun goSearch(){}
    fun  showBtnSearch(visible:Boolean?=false){
        btn_search.visibility=if(visible!!)  View.VISIBLE else View.GONE
    }
    fun  showBtnTwitter(visible:Boolean?=false){
        btn_twitter.visibility=if(visible!!)  View.VISIBLE else View.GONE
    }
    fun  showBtnStar(visible:Boolean?=false){
        btn_star.visibility=if(visible!!)  View.VISIBLE else View.GONE
    }
    override fun getResLayout(): Int {
        //throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
        return R.layout.activity_main
    }

    override fun getContentFrameId(): Int {
        //throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
        return R.id.container
    }

    override fun getHomeClass(): Class<*> {
        //throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                layout_input_text.visibility =View.GONE
            }
        }
    }

    fun setToolbarTitle(title:String){
        toolbar_title.text = if(!TextUtils.isEmpty(title)) title else getString(R.string.app_name)
    }
    fun showBtnBack(){
        toolbar_back.visibility=View.VISIBLE
    }
    fun signalOnClickBtnSearch(){
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Constants.ACTION_ONCLICK_BTN_SEARCH))
    }
    fun signalOnClickBtnTwitter(){
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Constants.ACTION_ONCLICK_BTN_TWITTER))
    }
    fun signalBackFragment(){
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Constants.ACTION_ONCLICK_BACK_FRAGMENT))
    }

}