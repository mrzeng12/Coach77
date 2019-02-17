package com.falcon.coach77

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_ticket.*

class TicketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        BitmapTool().loadImageFromStorage(this, imageView, "image1")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
