package com.falcon.coach77

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.provider.MediaStore


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val pref = act.getSharedPreferences(getString(com.falcon.kitchenbuddy.R.string.sp_name), 0)
//        val foodListHistoryString = pref.getString(getString(com.falcon.kitchenbuddy.R.string.sp_key_selectedFoodList), null)
//
//        val editor = pref.edit()


        imageButton1.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, 1)
        }
        imageButton2.setOnClickListener {
//            val pickPhoto = Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(pickPhoto, 2)
            val intent = Intent(this, TicketActivity::class.java)
            startActivity(intent)
        }
        imageButton3.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, 3)
        }
        imageButton4.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, 4)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        when (requestCode) {
            1 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                BitmapTool().saveToInternalStorage(this, bitmap, "image1")
//                BitmapTool().loadImageFromStorage(this, imageButton1, "image1")
            }
            2 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                BitmapTool().saveToInternalStorage(this, bitmap, "image2")
//                BitmapTool().loadImageFromStorage(this, imageButton1, "image1")
            }
            3 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                BitmapTool().saveToInternalStorage(this, bitmap, "image3")
//                BitmapTool().loadImageFromStorage(this, imageButton1, "image1")
            }
            4 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                BitmapTool().saveToInternalStorage(this, bitmap, "image4")
//                BitmapTool().loadImageFromStorage(this, imageButton1, "image1")
            }
        }
    }
}
