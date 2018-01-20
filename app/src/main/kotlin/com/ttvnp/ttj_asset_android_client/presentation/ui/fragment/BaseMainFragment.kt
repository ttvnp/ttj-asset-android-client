package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.graphics.BitmapFactory



abstract class BaseMainFragment : BaseFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    fun decodeUri(uri: Uri, requiredSize: Int): Bitmap {
        // Make a bitmap to get original sizes
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options)

        var tmpWidth = options.outWidth
        var tmpHeight = options.outHeight
        var scale = 1

        while (true) {
            if (tmpWidth / 2 < requiredSize || tmpHeight / 2 < requiredSize)
                break
            tmpWidth /= 2
            tmpHeight /= 2
            scale *= 2
        }

        val secondOptions = BitmapFactory.Options()
        secondOptions.inSampleSize = scale
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, secondOptions)
    }
}
