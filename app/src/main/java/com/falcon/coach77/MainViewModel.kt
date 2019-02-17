package com.falcon.coach77

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainViewModel : ViewModel() {
    var tickets = MutableLiveData<ArrayList<TicketObject>>()
    var selectedIndex = MutableLiveData<Int>()

    fun getTickets(context: Context) {
        if (tickets.value == null) {
            getTicketsFromSP(context)?.let {
                tickets.value = it
            } ?: run {
                val initialTickets = ArrayList<TicketObject>()
                initialTickets.add(TicketObject("image1", 0, false))
                initialTickets.add(TicketObject("image2", 0, false))
                initialTickets.add(TicketObject("image3", 0, false))
                initialTickets.add(TicketObject("image4", 0, false))
                updateTickets(context, initialTickets)
            }

        }
    }

    private val SP_NAME = "coach_tickets_sp"

    private val TICKETS_KEY = "tickets"

    fun getTicketsFromSP(context: Context): ArrayList<TicketObject>? {
        val pref = context.getSharedPreferences(SP_NAME, 0)
        val ticketsString = pref.getString(TICKETS_KEY, null)
        return Gson().fromJson(ticketsString, object : TypeToken<java.util.ArrayList<TicketObject>>() {}.type)
    }

    fun updateTickets(context: Context, updatedTickets: ArrayList<TicketObject>) {
        tickets.value = updatedTickets
        val pref = context.getSharedPreferences(SP_NAME, 0)
        val editor = pref.edit()
        editor.putString(TICKETS_KEY, Gson().toJson(updatedTickets))
        editor.apply()
    }

    fun addTicket(context: Context, index: Int) {
        val currentTickets = tickets.value
        if (currentTickets != null) {
            currentTickets[index].numberLeft = 10
            currentTickets[index].isAvailable = true
            updateTickets(context, currentTickets)
        }
    }

    fun useTicket(context: Context, index: Int) {
        val currentTickets = tickets.value
        if (currentTickets != null) {
            if (currentTickets[index].numberLeft > 0) {
                currentTickets[index].numberLeft--
                updateTickets(context, currentTickets)
            }
        }
    }

    fun unuseTicket(context: Context, index: Int) {
        val currentTickets = tickets.value
        if (currentTickets != null) {
            if (currentTickets[index].numberLeft < 10) {
                currentTickets[index].numberLeft++
                updateTickets(context, currentTickets)
            }
        }
    }

    fun removeTicket(context: Context, index: Int) {
        val currentTickets = tickets.value
        if (currentTickets != null) {
            currentTickets[index].numberLeft = 0
            currentTickets[index].isAvailable = false
            updateTickets(context, currentTickets)
        }
    }

}