package com.falcon.coach77


import android.app.Activity
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
import android.widget.Toast
import androidx.navigation.Navigation
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

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = "Coach 77"

        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        viewModel.getTickets(activity!!)

        viewModel.tickets.observe(this, Observer { tickets ->
            if (tickets != null) {
                if (!tickets[0].isAvailable){
                    imageButton1.setImageResource(R.drawable.ic_add_circle_outline_black_24dp)
                    ticketsLeft1.visibility = View.GONE
                    imageButton1.setOnClickListener {
                        content_loading_progress_bar.show()
                        val pickPhoto = Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
                        viewModel.selectedIndex.value = 0
                        Navigation.findNavController(activity!!, R.id.my_nav_host_fragment).navigate(R.id.ticketFragment)
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
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, selectedImage)
                    BitmapTool().saveToInternalStorage(activity!!, bitmap, "image1")
                    withContext(Dispatchers.Main) {
                        viewModel.addTicket(activity!!, 0)
                        content_loading_progress_bar.hide()
                        Toast.makeText(activity!!, "Load ticket successfully!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
