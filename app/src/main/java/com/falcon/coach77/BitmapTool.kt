package com.falcon.coach77

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.*


class BitmapTool {

    fun saveToInternalStorage(context: Context, bitmapImage: Bitmap, imageName: String): String {

        val resizedBitmap = getResizedBitmap(bitmapImage, 1920)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = context.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, imageName)

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return directory.absolutePath
    }

    fun loadImageFromStorage(context: Context, imageView: ImageView, imageName: String) {

        try {
            // path to /data/data/yourapp/app_data/imageDir
            val directory = context.getDir("imageDir", Context.MODE_PRIVATE)
            val f = File(directory, imageName)
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            imageView.setImageBitmap(b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }

        return Bitmap.createScaledBitmap(image, width, height, true)
    }
}