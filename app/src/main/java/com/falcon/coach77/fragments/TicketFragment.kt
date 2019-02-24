package com.falcon.coach77.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.falcon.coach77.R
import com.falcon.coach77.model.MainViewModel
import com.falcon.coach77.tool.BitmapTool
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

        val layout = activity?.window?.attributes
        layout?.screenBrightness = 1f
        activity?.window?.attributes = layout

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.title_fragment_ticket)

        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        viewModel.getTickets(activity!!)
        val ticketId = arguments?.getInt("selectedTicketIndex")
        viewModel.tickets.observe(this, Observer { tickets ->
            if (tickets != null && ticketId != null) {
                BitmapTool().loadImageFromStorage(activity!!, imageView, tickets[ticketId].imageName)
                ticketsLeftTextView.text = tickets[ticketId].numberLeft.toString() + " Left"

                if (tickets[ticketId].numberLeft < 3) {
                    ticketsLeftTextView.setTextColor(Color.RED)
                } else {
                    ticketsLeftTextView.setTextColor(Color.BLACK)
                }

                if (ticketId < tickets.size / 2) {
                    locationTextView.text = viewModel.zone1Name.value
                } else {
                    locationTextView.text = viewModel.zone2Name.value
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
