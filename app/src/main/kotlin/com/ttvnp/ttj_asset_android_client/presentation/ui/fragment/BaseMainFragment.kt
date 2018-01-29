package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


abstract class BaseMainFragment : BaseFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    fun decodeUri(uri: Uri, requiredSize: Int): Bitmap {
        // Make a bitmap to get original sizes
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, options)

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
        return BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, secondOptions)
    }

    fun openGallery(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/jpeg"
        startActivityForResult(intent, requestCode)
    }

    fun launchCamera(requestCode: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(context.packageManager) != null) {
            startActivityForResult(takePictureIntent, requestCode)
        }
    }

    fun checkCameraPermission(requestCode: Int) {
        if (hasSelfPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
            launchCamera(requestCode = requestCode)
        } else {
            val permissions = arrayListOf<String>()
            if (!hasSelfPermissions(Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA)
            }
            if (!hasSelfPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (!hasSelfPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            requestPermissions(permissions.toTypedArray(), requestCode)
        }
    }

    fun createUploadFile(context: Context, bitmap: Bitmap, tmpFileName: String): File {
        val file = File(context.externalCacheDir, tmpFileName)
        var fos: FileOutputStream? = null
        try {
            file.createNewFile()
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: IOException) {
        } finally {
            try {
                fos?.let {
                    it.flush()
                    it.close()
                }
            } catch (e: IOException) {
            }
        }
        return file
    }

    fun getRotatedImage(bitmap: Bitmap, photoPath: String): Bitmap {
        val ei = ExifInterface(photoPath)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotateImage(bitmap, 90.toFloat())
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotateImage(bitmap, 180.toFloat())
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotateImage(bitmap, 270.toFloat())
            }
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                flipImage(bitmap, false, true)
            }
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> {
                flipImage(bitmap, true, false)
            }
            else -> {
                bitmap
            }
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height,
                matrix, true)
    }

    private fun flipImage(source: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
        val matrix = Matrix()
        matrix.preScale(if (horizontal) (-1).toFloat() else (1).toFloat(), if (vertical) (-1).toFloat() else (1).toFloat())
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height,
                matrix, true)
    }

    private fun hasSelfPermissions(vararg permissions: String): Boolean {
        return permissions.none { ActivityCompat.checkSelfPermission(this.context, it) != PackageManager.PERMISSION_GRANTED }
    }

}
