package com.falcon.coach77


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_ticket.*

class TicketFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }


    private lateinit var viewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.title_fragment_ticket)

        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        viewModel.getTickets(activity!!)
        val ticketId = viewModel.selectedIndex.value
        viewModel.tickets.observe(this, Observer { tickets ->
            if (tickets != null && ticketId != null) {
                BitmapTool().loadImageFromStorage(activity!!, imageView, tickets[ticketId].imageName)
                ticketsLeftTextView.text = tickets[ticketId].numberLeft.toString() + " Left"
                if (ticketId < tickets.size / 2) {
                    locationTextView.text = "West Orange"
                } else {
                    locationTextView.text = "Livingston"
                }
                if (tickets[ticketId].numberLeft < 10) {
                    unuseButton.visibility = View.VISIBLE
                } else {
                    unuseButton.visibility = View.GONE
                }
                useButton.isEnabled = tickets[ticketId].numberLeft > 0
            }
        })

        useButton.setOnClickListener {
            viewModel.useTicket(activity!!, ticketId!!)
        }

        unuseButton.setOnClickListener {
            viewModel.unuseTicket(activity!!, ticketId!!)
        }


    }


}
