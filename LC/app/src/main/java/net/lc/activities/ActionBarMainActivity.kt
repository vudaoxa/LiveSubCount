package net.lc.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tieudieu.fragmentstackmanager.BaseActivityFragmentStack
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.item_main_action_btns.*
import kotlinx.android.synthetic.main.layout_input_text.*
import net.lc.fragments.main.MainFragment
import net.lc.models.ICallbackEditFav
import net.lc.utils.*
import net.lc.views.ActionBarTextBtn
import net.live.sub.R

/**
 * Created by HP on 11/28/2016.
 */

abstract class ActionBarMainActivity : BaseActivityFragmentStack() {
    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setup()
    }

    var tvEdit: ActionBarTextBtn? = null
    var mCallbackEditFav: ICallbackEditFav? = null
    fun setup(){
        toolbar?.let {
            setSupportActionBar(toolbar)
            supportActionBar?.elevation = 0f
            supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        } 
        toolbar_back.setOnClickListener {
//            val fragment = fragmentStackManager.currentFragment
//            if(fra)
        }

        btn_search.setImageDrawable(icon_search)
        btn_twitter.setImageDrawable(icon_share)
        btn_star.setImageDrawable(icon_star)
        btn_submit.setImageDrawable(icon_send)
        btn_search.setOnClickListener { goSearch() }
        btn_twitter.setOnClickListener { onSharing() }
        btn_star.setOnClickListener { onFollowing() }
        btn_submit.setOnClickListener { onSubmit() }

        layout_btns_fixed.startAnimation(anim)
        tvEdit = ActionBarTextBtn(this, tv_edit, listOf(R.string.edit, R.string.done), 0)
        tvEdit?.apply {
            this.mCallbackEditFav = this@ActionBarMainActivity.mCallbackEditFav
        }
    }

    abstract fun goSearch()
    abstract fun onSharing()
    abstract fun onFollowing()
    abstract fun onSubmit()
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
            InputUtils.hideKeyboard(this)
            if(!TextUtils.isEmpty(edt_search.text.toString().trim())){
                edt_search.text=null
                return
            }else{
                layout_input_text.visibility = View.GONE
            }
        }
    }

    fun setToolbarTitle(title: String?) {
        toolbar_title.text = if(!TextUtils.isEmpty(title)) title else getString(R.string.app_name)
    }

    fun showBtnBack(visi: Boolean) {
        toolbar_back.visibility = if (visi) View.VISIBLE else View.GONE
    }
}