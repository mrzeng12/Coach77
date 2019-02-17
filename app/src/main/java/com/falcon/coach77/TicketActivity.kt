package com.falcon.coach77

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.View

import kotlinx.android.synthetic.main.activity_ticket.*

class TicketActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getTickets(this)
        val ticketId = intent.getIntExtra("ticket-id", -1)
        viewModel.tickets.observe(this, Observer { tickets ->
            if (tickets != null && ticketId != -1) {
                BitmapTool().loadImageFromStorage(this, imageView, tickets[ticketId].imageName)
                ticketsLeftTextView.text = tickets[ticketId].numberLeft.toString() + " Left"
                if (ticketId < tickets.size / 2) {
                    locationTextView.text = "West Orange"
                } else {
                    locationTextView.text = "Livingston"
                }
                if (tickets[ticketId].numberLeft < 10){
                    unuseButton.visibility = View.VISIBLE
                }
                else {
                    unuseButton.visibility = View.GONE
                }
                if (tickets[ticketId].numberLeft > 0){
                    useButton.isEnabled = true
                }
                else {
                    useButton.isEnabled = false
                }
            }
        })

        useButton.setOnClickListener {
            viewModel.useTicket(this, ticketId)
        }

        unuseButton.setOnClickListener {
            viewModel.unuseTicket(this, ticketId)
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
