package net.lc.utils

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by HP on 11/26/2016.
 */
object Models {
    open class YtbResponse : Serializable {
        @SerializedName("kind")
        val kind : String?=null
        @SerializedName("etag")
        val etag : String?=null
    }

    open class Response : YtbResponse() {
        @SerializedName("pageInfo")
        val pageInfo:PageInfo?=null
    }

    class ChannelListResponse : Response() {
        @SerializedName("items")
        val items: ArrayList<ItemInfo>?=null

    }

    class
    SearchListResponse : Response() {
        @SerializedName("nextPageToken")
        val nextPageToken: String? = null
        @SerializedName("prevPageToken")
        val prevPageToken: String? = null
        @SerializedName("items")
        val items: ArrayList<SearchItemInfo>? = null
    }

    class SearchItemInfo : YtbResponse() {
        @SerializedName("id")
        val idInfo: IdInfo? = null
        //snippet
    }

    class Snippet : Serializable {
        @SerializedName("publishedAt")
        val publishedAt: String? = null
        @SerializedName("channelId")
        val channelId: String? = null
        @SerializedName("title")
        val title: String? = null
        @SerializedName("description")
        val description: String? = null
        @SerializedName("thumbnails")
        val thumbnails: Thumbnails? = null
        @SerializedName("channelTitle")
        val channelTitle: String? = null
        @SerializedName("liveBroadcastContent")
        val liveBroadcastContent: String? = null
    }

    class Thumbnails : Serializable {
        @SerializedName("default")
        val default: LinkInfo? = null
        @SerializedName("medium")
        val medium: LinkInfo? = null
        @SerializedName("high")
        val high: LinkInfo? = null
    }

    class LinkInfo : Serializable {
        @SerializedName("url")
        val url: String? = null
    }

    class IdInfo : Serializable {
        @SerializedName("kind")
        val kind : String?=null
        @SerializedName("channelId")
        val channelId: String? = null
    }

    class ItemInfo : YtbResponse() {
        @SerializedName("id")
        val id:String?=null
        @SerializedName("statistics")
        val statistics: ItemStatistics?=null
    }
    class ItemStatistics :Serializable{
        @SerializedName("viewCount")
        val viewCount: String?=null
        @SerializedName("commentCount")
        val commentCount:String?=null
        @SerializedName("subscriberCount")
        val subscriberCount: String? = null
        @SerializedName("hiddenSubscriberCount")
        val hiddenSubscriberCount: String? = null
        @SerializedName("videoCount")
        val videoCount:String?=null
    }
    class PageInfo :Serializable{
        @SerializedName("totalResults")
        val totalResults: Int =0
        @SerializedName("resultsPerPage")
        val resultsPerPage: Int =0
    }


}
