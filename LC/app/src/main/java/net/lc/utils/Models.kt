package net.lc.utils

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import java.io.Serializable
import java.util.*

/**
 * Created by HP on 11/26/2016.
 */
object Models {
    class ChannelListResponse :Serializable{
        @SerializedName("kind")
        val kind : String?=null
        @SerializedName("etag")
        val etag : String?=null
        @SerializedName("pageInfo")
        val pageInfo:PageInfo?=null
        @SerializedName("items")
        val items: ArrayList<ItemInfo>?=null

    }
    class ItemInfo :Serializable{
        @SerializedName("kind")
        val kind : String?=null
        @SerializedName("etag")
        val etag : String?=null
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
        @SerializedName("subcriberCount")
        val subcriberCount: String?=null
        @SerializedName("hiddenSubcriberCount")
        val hiddenSubcriberCount:String?=null
        @SerializedName("videoCount")
        val videoCount:String?=null
    }
    class PageInfo :Serializable{
        @SerializedName("totalResults")
        val totalResults: Int =0
        @SerializedName("resultsPerPage")
        val resultsPerPage: Int =0
    }



    class OnResponse: Serializable {
        @SerializedName("status_code")
        public val statusCode: Int = 0
        @SerializedName("message")
        public val message: String? = null
        @SerializedName("detail")
        public val detail: String? = null
        @SerializedName("errors")
        public val errors: ResponseErrors? = null
    }

    class ResponseErrors : Serializable {
        @SerializedName("email")
        val emailError: ArrayList<String>? = null
        @SerializedName("password")
        val passwordErrorr: ArrayList<String>? = null
        @SerializedName("code")
        val code: String? = null
        @SerializedName("title")
        val title: String? = null
        @SerializedName("detail")
        val detail: String? = null
    }

    class ReturnResponse<R, E> {
        fun returnResponse(onResponseSuccess: OnResponseSuccess<R, E>?, tag: String, response: Response<R>, extraData: E) {
            if (onResponseSuccess != null) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        onResponseSuccess.onResponseSuccess(tag, response.body(), extraData)
                    } else {
                        onResponseSuccess.onResponseError(tag, NetConstants.RESPONSE_NULL)
                    }
                } else {
                    try {
                        val onResponse = Gson().fromJson<OnResponse>(response.errorBody().string(), OnResponse::class.java)
                        onResponseSuccess.onResponseFailure(tag, onResponse, extraData)

                    } catch (e: Exception) {
                        e.printStackTrace()
                        onResponseSuccess.onResponseError(tag, NetConstants.RESPONSE_NULL)
                        return
                    }
                }
            }
        }
    }

    interface OnResponseSuccess<R, E> {
        fun onResponseSuccess(tag: String, response: R, extraData: E)
        fun onResponseFailure(tag: String, response: OnResponse, extraData: E)
        fun onResponseError(tag: String, message: String)
    }
}