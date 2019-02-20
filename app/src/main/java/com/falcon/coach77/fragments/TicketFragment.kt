package com.falcon.coach77.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.falcon.coach77.R
import com.falcon.coach77.model.MainViewModel
import com.falcon.coach77.tool.BitmapTool
import kotlinx.android.synthetic.main.fragment_ticket.*


class TicketFragment : Fragment() {

    //Variable to store brightness value
    private var brightness: Int = 0
    //Content resolver used as a handle to the system's settings
    private var cResolver: ContentResolver? = null
    //Window object, that will store a reference to the current window
    private var window: Window? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }


    private lateinit var viewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Get the content resolver
            cResolver = activity?.contentResolver

            //Get the current window
            window = activity?.window

            //Set the system brightness using the brightness variable value
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
            //Get the current window attributes
            val layoutpars = window?.attributes
            //Set the brightness of this window
            layoutpars?.screenBrightness = 1.0f
            //Apply attribute changes to this window
            window?.attributes = layoutpars
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + activity!!.packageName)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.title_fragment_ticket)

        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        viewModel.getTickets(activity!!)
        val ticketId = arguments?.getInt("selectedTicketIndex")
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
