package com.ttvnp.ttj_asset_android_client.data.service.adapter

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception

class DateAdapter : JsonAdapter<Date?>() {

    companion object {
        val FACTORY: Factory = Factory { type, _, _ ->
            if (type === Date::class.java) {
                return@Factory DateAdapter()
            }
            null
        }
    }

    override fun fromJson(reader: JsonReader): Date? {
        val dateString = reader.nextString() ?: return null
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            Log.e("MoshiDateAdapter", "could not parse date string: " + dateString)
            null
        }
    }

    override fun toJson(writer: JsonWriter, value: Date?) {
        var dateString = ""
        value?.let {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.UK)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            dateString = dateFormat.format(value)
        }
        writer.value(dateString)
    }
}
