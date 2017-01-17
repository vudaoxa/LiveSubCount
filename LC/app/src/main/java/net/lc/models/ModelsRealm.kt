package net.lc.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by mrvu on 1/5/17.
 */
open class SearchResultRealm(
        @PrimaryKey
        open var channelId: String? = null,
        open var channelTitle: String? = null,
        open var thumbnailUrl: String? = null,
        open var time: Long? = 0,
        open var following: Int? = 0
) : RealmObject()

open class SearchQueryRealm(@PrimaryKey open var query: String? = null,
                            open var time: Long? = 0) : RealmObject()

