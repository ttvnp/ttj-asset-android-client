package com.ttvnp.ttj_asset_android_client.data.service.adapter

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception

class DateAdapter : JsonAdapter<Date?>() {

    companion object {
        val FACTORY: Factory = object : Factory {
            override fun create(type: Type?, annotations: MutableSet<out Annotation>?, moshi: Moshi?): JsonAdapter<*>? {
                if (type === Date::class.java) {
                    return DateAdapter()
                }
                return null
            }
        }
    }

    override fun fromJson(reader: JsonReader): Date? {
        val dateString = reader.nextString()
        if (dateString == null) {
            return null
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.UK)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        try {
            return dateFormat.parse(dateString)
        } catch (e: Exception) {
            Log.e("MoshiDateAdapter", "could not parse date string: " + dateString)
            return null
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
