package net.lc.utils

import net.live.sub.BuildConfig


/**
 * Created by HP on 11/26/2016.
 */
object Constants {
    const val LC = "LC"
    const val KEY_AUTHORIZATION = "Authorization"
    const val PART_STATISTICS = "statistics"
    const val PART_SNIPPET = "snippet"
    const val TYPE_CHANNEL = "channel"

    const val API_KEY = BuildConfig.GOOGLE_VERIFICATION_API_KEY
    const val ACTION_ONCLICK_BTN_SEARCH="btn_search"
    const val ACTION_ONCLICK_BTN_TWITTER="btn_twitter"
    const val ACTION_ONCLICK_BTN_STAR="btn_star"
    const val ACTION_ONCLICK_BTN_SUBMIT = "btn_submit"
    const val ACTION_ONCLICK_BACK_FRAGMENT="back_fragment"
    const val ACTION_SEARCH = "SEARCH"
    const val KEY_SEARCH_QUERY = "SEARCH_QUERY"
    const val ACTION_CLICK_TOPIC_SUGGESTION = "SUGGEST"
    const val ACTION_CLICK_SEARCH_RESULT = "SEARCH_RESULT"
    const val ACTION_SEARCH_SUGGESTION = "SEARCH_SUGGESTION"
    const val KEY_SEARCH_SUGGESTION_QUERY = "SEARCH_SUGGESTION_QUERY"
    const val ACTION_BACK_SEARCH_SUGGESTION = "back_SUGGESTION"
    const val ACTIVITY_SEARCH_CODE = 123
    const val AUTO_LOAD_DURATION = 1500L
    const val ANIM_DURATION = 1500L
    const val ANIM_DURATION_SHORT = 300L
    const val TYPE_LOADING = -21
    const val TYPE_ITEM = 0
    const val TYPE_ADS = -1
}