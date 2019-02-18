package com.falcon.coach77.fragments


import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.falcon.coach77.tool.BitmapTool
import com.falcon.coach77.model.MainViewModel
import com.falcon.coach77.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val imageButtonList = ArrayList<ImageButton>()
        imageButtonList.add(imageButton1)
        imageButtonList.add(imageButton2)
        imageButtonList.add(imageButton3)
        imageButtonList.add(imageButton4)

        val ticketsLeftList = ArrayList<TextView>()
        ticketsLeftList.add(ticketsLeft1)
        ticketsLeftList.add(ticketsLeft2)
        ticketsLeftList.add(ticketsLeft3)
        ticketsLeftList.add(ticketsLeft4)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.app_name)

        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        viewModel.getTickets(activity!!)

        viewModel.tickets.observe(this, Observer { tickets ->
            if (tickets != null) {
                for ((index, ticket) in tickets.withIndex()) {
                    if (!ticket.isAvailable) {
                        imageButtonList[index].setImageResource(R.drawable.ic_add_circle_outline_black_24dp)
                        ticketsLeftList[index].visibility = View.GONE
                        imageButtonList[index].setOnClickListener {
                            val pickPhoto = Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            startActivityForResult(pickPhoto, index)
                        }
                    } else {
                        if (ticket.numberLeft > 0) {
                            imageButtonList[index].setImageResource(R.drawable.ic_barcode)
                        } else {
                            imageButtonList[index].setImageResource(R.drawable.ic_barcode_not_available)
                        }
                        ticketsLeftList[index].visibility = View.VISIBLE
                        imageButtonList[index].setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt("selectedTicketIndex", index)
                            Navigation.findNavController(activity!!, R.id.my_nav_host_fragment).navigate(R.id.ticketFragment, bundle)
                        }

                        imageButtonList[index].setOnLongClickListener {
                            val builder = AlertDialog.Builder(activity)
                            builder
                                    .setTitle("Remove ticket")
                                    .setMessage("Are you sure to remove this ticket?")
                                    .setPositiveButton(android.R.string.yes) { _, _ ->
                                        content_loading_progress_bar.show()
                                        GlobalScope.launch(Dispatchers.IO) {
                                            viewModel.removeTicket(activity!!, index)
                                            withContext(Dispatchers.Main) {
                                                content_loading_progress_bar.hide()
                                            }
                                        }
                                    }
                                    .setNegativeButton(android.R.string.no) { _, _ ->
                                    }
                                    .setIcon(R.drawable.ic_delete_forever_black_24dp)
                                    .show()
                            true
                        }
                    }
                    ticketsLeftList[index].text = ticket.numberLeft.toString() + " Left"

                    if (!tickets[0].isAvailable && !tickets[1].isAvailable) {
                        imageButton2.visibility = View.INVISIBLE
                    } else {
                        imageButton2.visibility = View.VISIBLE
                    }

                    if (!tickets[2].isAvailable && !tickets[3].isAvailable) {
                        imageButton4.visibility = View.INVISIBLE
                    } else {
                        imageButton4.visibility = View.VISIBLE
                    }
                }

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)

        if (resultCode == Activity.RESULT_OK) {
            val selectedImage = imageReturnedIntent?.data
            content_loading_progress_bar.show()
            GlobalScope.launch(Dispatchers.IO) {
                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, selectedImage)
                val imageName = viewModel.tickets.value!![requestCode].imageName
                BitmapTool().saveToInternalStorage(activity!!, bitmap, imageName)
                withContext(Dispatchers.Main) {
                    viewModel.addTicket(activity!!, requestCode)
                    content_loading_progress_bar.hide()
                    Toast.makeText(activity!!, "Load ticket successfully!", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}
