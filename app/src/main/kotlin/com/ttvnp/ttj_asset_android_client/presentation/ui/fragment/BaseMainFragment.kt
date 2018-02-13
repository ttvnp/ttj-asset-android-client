package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
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
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/jpeg"
        startActivityForResult(intent, requestCode)
    }

    fun launchCamera(requestCode: Int): Uri {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val pictureUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
        startActivityForResult(intent, requestCode)

        return pictureUri
    }

    fun checkPermission(requestCode: Int, isCamera: Boolean): Uri? {
        if (hasSelfPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (isCamera) {
                return launchCamera(requestCode = requestCode)
            }
            openGallery(requestCode = requestCode)
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

        return null
    }

    fun checkGrantResults(grantResults: Collection<Int>): Boolean {
        if (grantResults.isEmpty()) throw IllegalArgumentException("grantResults is empty.")
        return grantResults.none { it != PackageManager.PERMISSION_GRANTED }
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

    fun getResultImage(bitmap: Bitmap, path: String): Bitmap {
        val exif = ExifInterface(path)
        val rotation = exif.getAttribute(ExifInterface.TAG_ORIENTATION)
        if (!TextUtils.isEmpty(rotation)) {
            val param = rotation.toInt()
            when (param) {
                ExifInterface.ORIENTATION_NORMAL -> {
                    return getRotatedImage(bitmap, 0f)
                }
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    return getRotatedImage(bitmap, 90f)
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    return getRotatedImage(bitmap, 180f)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    return getRotatedImage(bitmap, 270f)
                }
            }
        }

        return bitmap
    }

    fun getPath(uri: Uri): String {
        var cursor: Cursor? = null
        return try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    private fun getRotatedImage(bitmap: Bitmap, rotate: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(rotate)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun hasSelfPermissions(vararg permissions: String): Boolean {
        return permissions.none { ActivityCompat.checkSelfPermission(this.context, it) != PackageManager.PERMISSION_GRANTED }
    }

}