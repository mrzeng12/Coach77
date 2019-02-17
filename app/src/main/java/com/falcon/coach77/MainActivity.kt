package com.falcon.coach77

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getTickets(this)

        viewModel.tickets.observe(this, Observer { tickets ->
            if (tickets != null) {
                if (!tickets[0].isAvailable){
                    imageButton1.setImageResource(R.drawable.ic_add_circle_outline_black_24dp)
                    ticketsLeft1.visibility = View.GONE
                    imageButton1.setOnClickListener {
                        content_loading_progress_bar.show()
                        val pickPhoto = Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(pickPhoto, 1)
                    }
                }
                else {
                    if (tickets[0].numberLeft >0){
                        imageButton1.setImageResource(R.drawable.ic_barcode)
                    }
                    else {
                        imageButton1.setImageResource(R.drawable.ic_barcode_not_available)
                    }
                    ticketsLeft1.visibility = View.VISIBLE
                    imageButton1.setOnClickListener {
                        val intent = Intent(this, TicketActivity::class.java)
                        intent.putExtra("ticket-id",0)
                        startActivity(intent)
                    }
                }
                ticketsLeft1.text = tickets[0].numberLeft.toString() + " Left"
            }
        })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        when (requestCode) {
            1 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data

                GlobalScope.launch(Dispatchers.IO) {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                        BitmapTool().saveToInternalStorage(this@MainActivity, bitmap, "image1")
                    withContext(Dispatchers.Main) {
                        viewModel.addTicket(this@MainActivity, 0)
                        content_loading_progress_bar.hide()
                        Toast.makeText(this@MainActivity, "Load ticket successfully!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
