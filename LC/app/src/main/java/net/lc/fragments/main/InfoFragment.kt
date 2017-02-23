package net.lc.fragments.main

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import kotlinx.android.synthetic.main.fragment_info.*
import net.lc.models.ICallbackOnClick
import net.lc.utils.ACTION_EMAIL
import net.lc.utils.CATEGORY_ACTION
import net.lc.utils.getTitleHeader
import net.lc.utils.sendHit
import net.live.sub.R


/**
 * Created by HP on 11/28/2016.
 */
class InfoFragment : BaseFragmentStack(), ICallbackOnClick {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_live_subs_count.text = getTitleHeader(activity, R.string.live_subs_count, R.string.live_subs_count_content)
        tv_sharing.text = getTitleHeader(activity, R.string.sharing, R.string.sharing_content)
        tv_search.text = getTitleHeader(activity, R.string.text_search, R.string.text_search_content)
        tv_fav.text = getTitleHeader(activity, R.string.text_fav, R.string.text_fav_content)
        tv_notis.text = getTitleHeader(activity, R.string.text_notis, R.string.text_notis_content)
        tv_email.text = getTitleEmail(activity, R.string.text_email, R.string.text_email_content)
        tv_email.setMovementMethod(LinkMovementMethod.getInstance())
    }

    fun getTitleEmail(context: Context, headerResId: Int, contentResId: Int): SpannableString {
        val header = context.getString(headerResId)
        val content = context.getString(contentResId)
        val r = header + " " + content
        val res = SpannableString(r)
//        res.apply {
//            setSpan(StyleSpan(Typeface.BOLD), 0,
//                    header.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        }
        val clickDoctor = object : ClickableSpan() {
            override fun onClick(view: View) {
                onClick(ACTION_CLICK_EMAIL, 0)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

        }
        val start = header.length + 1
        val end = r.length
        try {
            res.setSpan(clickDoctor, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            res.setSpan(ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.blue)), start,
                    end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            res.setSpan(StyleSpan(Typeface.BOLD), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    val ACTION_CLICK_EMAIL = 0
    override fun onClick(position: Int, event: Int) {
        when (event) {
            ACTION_CLICK_EMAIL -> {
                sendEmail()
            }
        }
    }

    fun sendEmail() {
        sendHit(CATEGORY_ACTION, ACTION_EMAIL)
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", getString(R.string.text_email_content), null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body")
        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }
}
